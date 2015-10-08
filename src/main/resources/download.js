/**********************************************
 * Script for render pages to PNG files.
 * Usage:
 *     phantomjs  $proxy $file_to_run $url $cookies $image_file
 *     $proxy           - --proxy=IP:PORT
 *     $file_to_run     - path/to/file.js
 *     $url             - URL to render. Escaped by ""
 *     $cookies         - JSON string
 *     $image_file      - path/to/file.png
 *
 * Example:
 *     /usr/local/bin/phantomjs --ignore-ssl-errors=yes --proxy=IP:PORT download.js URL [{"name":"NAME","value":"VALUE"},{"name":"NAME","value":"VALUE"}] /PATH/TO/DESTINATION
 *
 * Important:
 *     Recommended to use --ssl-protocol=any --ignore-ssl-errors=yes parameter.
 **********************************************/


var system = require('system');
// read arguments from CLI
if (system.args.length < 4) {
    console.log('Try to pass some args when invoking this script!');
    phantom.exit(1);
} else {
    // parse arguments
    try {
        var url = system.args[1].replace(/\\/g, ""),
            destination = system.args[3].replace(/\\/g, ""),
            domain = parseDomain(url.replace(/\\/g, ""));

        addCookies(system.args[2].replace(/\\/g, ""));
        renderPage(url);
    } catch ( e ) {
        console.log(e.message);
        phantom.exit(1);
    }
}
/**
 * render page to PNG
 * @param  url
 * @return void
 */
function renderPage(url ) {
    // create page
    var page = require('webpage').create();
    // set user agent
    page.settings.userAgent = 'Mozilla/5.0 (Unknown; Linux x86_64) AppleWebKit/534.34 (KHTML, like Gecko) Safari/534.34';
    // set image size
    page.viewportSize = { height:800, width:1024 };

    var redirectURL = null;
    /**
     * Callback called when page received. Need to
     * catch redirect by header "location"
     * @param  resource
     * @return void
     */
    page.onResourceReceived = function(resource) {
        if (url == resource.url && resource.redirectURL) {
            redirectURL = resource.redirectURL;
        }
    };
    /**
     * callback called when browser navigation request received. Need to
     * catch redirects by window.location.href = .....
     * @param  inurl
     * @param  type
     * @param  willNavigate
     * @param  main
     * @return void
     */
    page.onNavigationRequested = function(inurl, type, willNavigate, main) {
        if (/facebook/i.test('facebook') && main && inurl!=url) {
            redirectURL = inurl;
        }
    };
    // open page
    page.open(url, function(status) {
        if (redirectURL) {
            console.log('Go to redirect ' + redirectURL);
            page.close();
            renderPage(redirectURL);
        } else if (status == 'success') {
            console.log('Page loaded');
            // wait for timeout. need to some networks like facebook
            setTimeout(function(){
                if (/\.html/.test(destination)) {
                    saveToFile(destination, page.content.replace("<body", "<body style=\"pointer-events:none;\" "));
                    page.close();
                    phantom.exit();
                } else {
                    // try to render page
                    if ( page.render(destination) ) {
                        console.log('Page rendered to ' + destination);
                        page.close();
                        phantom.exit();
                    } else {
                        console.log('Page render failed');
                        phantom.exit(1);
                    }
                }
            }, getTimeout(domain));
        } else {
            console.log('Page load failed');
            phantom.exit(1);
        }
    });
}
/**
 * parse domain from URL
 * @param  url
 * @return string
 */
function parseDomain( url ) {
    var parser = document.createElement('a');
    parser.href = url;
    return parser.hostname;
}
/**
 * return timeout by domain
 * @param  domain
 * @return number of milliseconds
 */
function getTimeout( domain ) {
    return 1000;
}
/**
 * add cookies to page
 * @param cookiesJsonStr
 */
function addCookies( cookiesJsonStr ) {
    if ( typeof cookiesJsonStr === "string" ) {
        console.log(cookiesJsonStr);
        try {
            var cookies = JSON.parse(cookiesJsonStr);
        } catch (e) {
            console.log(e.message);
            phantom.exit(1);
        }
        if ( typeof cookies === "object" ) {
            for (var i = cookies.length - 1; i >= 0; i--) {
                cookies[i].domain = domain;
                if ( cookies[i].value.lastIndexOf(';') == cookies[i].value.length-1 ) {
                    cookies[i].value = cookies[i].value.slice(0, cookies[i].value.length-1);
                }
                var r = phantom.addCookie(cookies[i]);
                console.log("cookie "+cookies[i].name+" add: "+r);
            }
        }
    }
}
/**
 * save to local file
 * @param path
 * @param content
 */
function saveToFile(path, content) {
    var fs = require('fs');
    fs.write(path, content, 'w');
}

/**
 * verify page is ID present there
 * @param page
 * @param verifyId
 * @param domain
 */
function verifyPage(page, verifyId, domain) {
    if (/facebook/.test(domain)) {
        var tester = new RegExp("profile_id[=\":]{1,2}("+verifyId+")", "g");
        if (!tester.test(page.content)) {
            return false;
        }
    }
    console.log("page verification success!");
    return true;
}