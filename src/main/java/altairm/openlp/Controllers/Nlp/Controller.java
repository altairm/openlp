package altairm.openlp.Controllers.Nlp;

import altairm.openlp.Analyzer.OrganizationAnalyzer;
import altairm.openlp.Analyzer.PersonAnalyzer;
import altairm.openlp.Analyzer.TokenAnalyzer;
import opennlp.tools.util.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author altair
 * @since 7/21/15.
 */
@RestController
public class Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    @RequestMapping(
            value = "/openlp",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Result analyze(@RequestBody Payload payload) {
        Result res = new Result();
        try {
            if (payload.isTypeOf(Payload.TYPE_NAME)) {
                Set<Span> names = new HashSet<>();
                TokenAnalyzer tokenAnalyzer = new TokenAnalyzer(payload.getText());
                String tokens[] = tokenAnalyzer.analyze();
                if (tokens != null) {
                    OrganizationAnalyzer organizationAnalyzer = new OrganizationAnalyzer(tokens);
                    names.addAll(Arrays.asList(organizationAnalyzer.analyze()));
                    PersonAnalyzer personAnalyzer = new PersonAnalyzer(tokens);
                    names.addAll(Arrays.asList(personAnalyzer.analyze()));
                    for (Span span: names) {
                        String nameString = "";
                        for (int i = span.getStart(); i < span.getEnd(); i++) {
                            nameString += " " + tokens[i];
                        }
                        Result.Item item = new Result.Item();
                        item.setText(nameString);
                        item.setType(span.getType());
                        item.setProb(Math.ceil(span.getProb() * 100));
                        res.getList().add(item);
                    }
                }
                return res;
            } else if (payload.isTypeOf(Payload.TYPE_SENTENCE)) {
                return new Result();
            } else {
                TokenAnalyzer analyzer = new TokenAnalyzer(payload.getText());
                for (String token: analyzer.analyze()) {
                    Result.Item item = new Result.Item();
                    item.setText(token);
                    item.setType("token");
                    res.getList().add(item);
                }
                return res;
            }
        } catch (IOException e) {
            LOGGER.warn("Error while analyzing text", e);
        }
        return new Result();
    }
}
