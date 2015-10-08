package altairm.openlp.Controllers.Phantom;

/**
 * @author altair
 * @since 7/21/15.
 */
public class Payload {

    private String text;

    public Payload() {
    }

    public Payload(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
