package altairm.openlp.Controllers.Phantom;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author altair
 * @since 10/7/15.
 */
@RestController
public class PhantomController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhantomController.class);

    @RequestMapping(
            value = "/api/phantom",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Result process(@RequestBody Payload payload) {
        String[] urlsParsed = payload.getText().split("\n");
        Set<String> urls = new HashSet<>(Arrays.asList(urlsParsed));
        Result res = new Result();
        StopWatch global = new StopWatch();
        global.start();
        for (String url: urls) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CommandLine cmdLine = new CommandLine("/usr/local/bin/phantomjs");
            cmdLine.addArgument("--ignore-ssl-errors=yes");
            cmdLine.addArgument("/home/altair/workspace.old/openlp/src/main/resources/download.js");
            cmdLine.addArgument(url);
            cmdLine.addArgument("[]");
            String fileName = url.replaceAll("[^\\w]", "_") + ".jpeg";
            cmdLine.addArgument("/home/altair/workspace.old/openlp/public/downloads/" + fileName);
            DefaultExecutor exec = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            exec.setStreamHandler(streamHandler);
            StopWatch stopWatch = new StopWatch();
            try {
                stopWatch.start();
                LOGGER.info("executing command {}", cmdLine.toString());
                exec.execute(cmdLine);
                LOGGER.info("phantom for url {} execution success.", url);
                res.setStatus(true);
                Result.Item item = new Result.Item();
                item.setLink(fileName);
                item.setUrl(url);
                item.setTime(stopWatch.toString());
                res.addToList(item);
            } catch (IOException e) {
                res.setStatus(false);
                Result.Item item = new Result.Item();
                item.setLink("#");
                item.setUrl(url);
                item.setTime(stopWatch.toString());
                item.setError(outputStream.toString());
                res.addToList(item);
                LOGGER.info("phantom execution error {}", outputStream.toString(), e);
            } finally {
                stopWatch.stop();
                stopWatch.reset();
                try {
                    outputStream.close();
                } catch (IOException e) {
                    LOGGER.warn("Error closing output stream.", e);
                }
            }
        }
        res.setTime(global.toString());
        global.stop();
        return res;
    }
}
