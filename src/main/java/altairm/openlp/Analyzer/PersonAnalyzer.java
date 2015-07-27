package altairm.openlp.Analyzer;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author altair
 * @since 7/21/15.
 */
public class PersonAnalyzer {

    public static final Logger LOGGER = LoggerFactory.getLogger(PersonAnalyzer.class);

    private String[] tokens;

    public PersonAnalyzer(String[] tokens) {
        this.tokens = tokens;
    }

    public String[] getTokens() {
        return tokens;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    public Span[] analyze() throws IOException {
        InputStream modelIn = null;
        try {
            modelIn = getClass().getResourceAsStream("/en-ner-person.bin");
            TokenNameFinderModel model1 = new TokenNameFinderModel(modelIn);
            NameFinderME finder = new NameFinderME(model1);
            return finder.find(this.getTokens());
        } finally {
            if (modelIn != null) {
                modelIn.close();
            }
        }
    }
}
