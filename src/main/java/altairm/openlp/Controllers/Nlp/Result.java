package altairm.openlp.Controllers.Nlp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author altair
 * @since 7/21/15.
 */
public class Result {

    public static class Item {
        private String text;
        private String type;
        private double prob;

        public Item() {}

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getProb() {
            return prob;
        }

        public void setProb(double prob) {
            this.prob = prob;
        }
    }

    private List<Item> list;

    private boolean status = true;

    public Result() {
        this(new ArrayList<>());
    }

    public Result(List<Item> list) {
        this.list = list;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Item> getList() {
        return list;
    }

    public void setList(List<Item> list) {
        this.list = list;
    }
}
