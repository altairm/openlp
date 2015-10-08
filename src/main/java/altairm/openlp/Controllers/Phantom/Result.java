package altairm.openlp.Controllers.Phantom;

import java.util.ArrayList;
import java.util.List;

/**
 * @author altair
 * @since 7/21/15.
 */
public class Result {

    public static class Item {
        private String url;
        private String link;
        private String time;
        private String error;

        public Item() {}

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
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
