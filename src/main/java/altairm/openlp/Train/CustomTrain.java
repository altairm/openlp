package altairm.openlp.Train;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;

/**
 * @author altair
 * @since 7/30/15.
 */
public class CustomTrain implements Train {

    public void train() throws IOException {
        File outFile = null;
        TokenNameFinderModel model = null;
        FileOutputStream outFileStream = null;
        try {
            Charset charset = Charset.forName("UTF-8");
            MarkableFileInputStreamFactory markableFileInputStreamFactory = new MarkableFileInputStreamFactory(new File("/home/altair/workspace/openlp/src/main/resources/train.txt"));
            ObjectStream fileStream = new PlainTextByLineStream(markableFileInputStreamFactory, charset);
            ObjectStream sampleStream = new NameSampleDataStream(fileStream);
            model = NameFinderME.train("en", "custom", sampleStream, Collections.<String, Object>emptyMap());
            NameFinderME nfm = new NameFinderME(model);

            outFile = new File("/home/altair/workspace/openlp/src/main/resources/en-custom.bin");
            outFileStream = new FileOutputStream(outFile);
            model.serialize(outFileStream);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (outFileStream != null) {
                outFileStream.close();
            }
        }
    }
}
