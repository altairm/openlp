package altairm.openlp;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author altair
 * @since 7/21/15.
 */
@RestController
public class NlpController {
    @RequestMapping("/openlp")
    public Result get(@RequestParam(value="text", defaultValue="") String text) {

        return new Result(new String[]{"a"});
    }
}
