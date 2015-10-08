package altairm.openlp.Analyzer;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author altair
 * @since 10/7/15.
 */
public class PartOfSpeechAnalyzer implements Analyzer<String> {

    public static final Logger LOGGER = LoggerFactory.getLogger(PartOfSpeechAnalyzer.class);

    private String[] tokens;

    public String[] getTokens() {
        return tokens;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    public PartOfSpeechAnalyzer(String[] tokens) {
        this.tokens = tokens;
    }

    @Override
    public String[] analyze() throws IOException {
        InputStream modelIn = null;
        try {
            modelIn = getClass().getResourceAsStream("/en-pos-maxent.bin");
            final POSModel posModel = new POSModel(modelIn);
            modelIn.close();

            POSTaggerME posTagger = new POSTaggerME(posModel);
            return posTagger.tag(tokens);
        } finally {
            if (modelIn != null) {
                modelIn.close();
            }
        }
    }
}
