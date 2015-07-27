package altairm.openlp.Controllers.Nlp;

import javax.validation.constraints.NotNull;

/**
 * @author altair
 * @since 7/21/15.
 */
public class Payload {

    public static final String TYPE_TOKENIZER = "tokenizer";
    public static final String TYPE_SENTENCE = "sentence";
    public static final String TYPE_NAME = "name";

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

    public boolean isTypeOf(String type) {
        return this.getType().equals(type);
    }
}
