import java.io.Serializable;

public class Message implements Serializable {

    private String pseudo;
    private String textMessage;

    public Message(String pseudo, String textMessage) {
        this.pseudo = pseudo;
        this.textMessage = textMessage;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public String getTextMessage() {
        return this.textMessage;
    }

}
