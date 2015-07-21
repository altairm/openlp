package altairm.openlp;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;

/**
 * @author altair
 * @since 7/21/15.
 */
@RestController
public class NlpController {
    @RequestMapping(
            value = "/openlp",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Result analyze(@RequestBody Payload payload) {
        Analyzer analyzer = new Analyzer();
        try {
            Set<String> names = analyzer.nameAnalyzer(payload.getText());
            return new Result(names.toArray(new String[names.size()]));
        } catch (IOException e) {
            return new Result(new String[]{});
        }
    }
}
