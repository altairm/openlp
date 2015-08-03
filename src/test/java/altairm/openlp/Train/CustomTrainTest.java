package altairm.openlp.Train;

import org.junit.Test;

/**
 * @author altair
 * @since 8/3/15.
 */
public class CustomTrainTest {

    @Test
    public void testTrain() throws Exception {
        CustomTrain customTrain = new CustomTrain();
        customTrain.train();
    }
}