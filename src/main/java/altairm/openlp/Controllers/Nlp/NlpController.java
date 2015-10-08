package altairm.openlp.Controllers.Nlp;

import altairm.openlp.Analyzer.*;
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
public class NlpController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NlpController.class);

    @RequestMapping(
            value = "/api/openlp",
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
//                    CustomAnalyzer customAnalyzer = new CustomAnalyzer(tokens);
//                    names.addAll(Arrays.asList(customAnalyzer.analyze()));
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
            } else if (payload.isTypeOf(Payload.TYPE_POS)) {
                TokenAnalyzer analyzer = new TokenAnalyzer(payload.getText());
                String[] tokens = analyzer.analyze();
                PartOfSpeechAnalyzer partOfSpeechAnalyzer = new PartOfSpeechAnalyzer(tokens);
                String[] partsOfSpeech = partOfSpeechAnalyzer.analyze();
                for (int i = 0; i< tokens.length; i++) {
                    Result.Item item = new Result.Item();
                    item.setText(tokens[i]);
                    if (partsOfSpeech.length > i) {
                        item.setType(partsOfSpeech[i]);
                    } else {
                        item.setType("N/A");
                    }
                    res.getList().add(item);
                }
                return res;
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

    @RequestMapping(
            value = "/api/train",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Result train(@RequestBody Payload payload) {
        return new Result();
    }
}
