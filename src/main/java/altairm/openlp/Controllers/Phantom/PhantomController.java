package altairm.openlp.Controllers.Phantom;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

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
        Result res = new Result();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CommandLine cmdLine = new CommandLine("/usr/local/bin/phantomjs");
        cmdLine.addArgument("--ignore-ssl-errors=yes");
        cmdLine.addArgument("/home/altair/workspace.old/openlp/src/main/resources/download.js");
        cmdLine.addArgument(payload.getText());
        cmdLine.addArgument("[]");
        String fileName = payload.getText().replaceAll("[^\\w]", "_") + ".png";
        cmdLine.addArgument("/home/altair/workspace.old/openlp/public/downloads/" + fileName);
        DefaultExecutor exec = new DefaultExecutor();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        exec.setStreamHandler(streamHandler);
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            exec.execute(cmdLine);
            stopWatch.stop();
            LOGGER.debug("phantom execution success. {}", outputStream.toString());
            res.setStatus(true);
            res.setList(new ArrayList<Result.Item>(){{
                Result.Item item = new Result.Item();
                item.setLink(fileName);
                item.setUrl(payload.getText());
                item.setTime(stopWatch.toString());
                add(item);
            }});
        } catch (IOException e) {
            res.setStatus(false);
            res.setList(new ArrayList<Result.Item>(){{
                Result.Item item = new Result.Item();
                item.setLink("#");
                item.setUrl(payload.getText());
                item.setTime(stopWatch.toString());
                item.setError(outputStream.toString());
                add(item);
            }});
            LOGGER.error("phantom execution error {}", outputStream.toString(), e);
        }
        return res;
    }
}
