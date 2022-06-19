package ifce.ppd.finalproject.spaces.model;

import net.jini.core.entry.Entry;

import java.util.Objects;
import java.util.UUID;

public class User implements Entry {
    private static final long serialVersionUID = -8557220653367915055L;

    public UUID id;
    public String name;

    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "id:" + id.toString().substring(0, 5) + " - name: " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
