package ifce.ppd.finalproject.model;

import net.jini.core.entry.Entry;

import java.util.Objects;
import java.util.UUID;

public class Message implements Entry {
//    private static final long serialVersionUID = -1L;

    public UUID id;
    public User author;
    public String content;

    public Message() {}

    public Message(UUID id, User author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return id.equals(message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
