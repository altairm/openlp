package altairm.openlp;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @author altair
 * @since 7/21/15.
 */
public class Analyzer {
    public Set<String> nameAnalyzer(String text) throws IOException {
        Tokenizer tokenizer = null;
        String tokens[] = null;
        InputStream modelIn = null;
        Set<String> res = new HashSet<>();
        try {
            modelIn = getClass().getResourceAsStream("/en-token.bin");
            TokenizerModel model = new TokenizerModel(modelIn);
            tokenizer = new TokenizerME(model);
            tokens = tokenizer.tokenize(text);
            modelIn = getClass().getResourceAsStream("/en-ner-organization.bin");
            TokenNameFinderModel model1 = new TokenNameFinderModel(modelIn);
            NameFinderME finder = new NameFinderME(model1);
            Span names[] = finder.find(tokens);
            for (Span name: names) {
                res.add(tokens[name.getStart()]);
            }
            return res;
        } finally {
            if (modelIn != null) {
                modelIn.close();
            }
        }
    }
}
