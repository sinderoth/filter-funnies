import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Message {
    private String sender;
    private String receiver;
    private LocalDate date;
    private String message;

    public Message(String... varargs) {
        if (varargs.length != 4) {
            throw new RuntimeException("Given varargs length should be 4 where it contains date, sender, receiver, message");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        this.date = LocalDate.parse(varargs[0], dateTimeFormatter);
        this.sender = varargs[1];
        this.receiver = varargs[2];
        this.message = varargs[3];
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return sender + " -> " + receiver + " (" + date + "): " + message;
    }
}
