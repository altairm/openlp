package altairm.openlp;

import javax.validation.constraints.NotNull;

/**
 * @author altair
 * @since 7/21/15.
 */
public class Payload {
    private String text;
    @NotNull
    private String type;

    public Payload() {
    }

    public Payload(String text, String type) {
        this.text = text;
        this.type = type;
    }

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
}
