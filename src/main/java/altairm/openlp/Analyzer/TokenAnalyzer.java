package altairm.openlp.Analyzer;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author altair
 * @since 7/27/15.
 */
public class TokenAnalyzer {

    public static final Logger LOGGER = LoggerFactory.getLogger(TokenAnalyzer.class);

    private String text;

    public TokenAnalyzer(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] analyze() throws IOException {
        InputStream modelIn = null;
        try {
            modelIn = getClass().getResourceAsStream("/en-token.bin");
            TokenizerModel model = new TokenizerModel(modelIn);
            Tokenizer tokenizer = new TokenizerME(model);
            return tokenizer.tokenize(this.getText());
        } finally {
            if (modelIn != null) {
                modelIn.close();
            }
        }
    }
}
