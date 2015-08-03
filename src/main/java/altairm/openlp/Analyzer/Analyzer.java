package altairm.openlp.Analyzer;

import java.io.IOException;

/**
 * @author altair
 * @since 7/30/15.
 */
public interface Analyzer<T> {
    T[] analyze() throws IOException;
}
