package coding.test.model;

import java.util.Objects;

public class ClientId {
    // added this wrapper class only for better readability purposes
    // otherwise, could just use String

    private String id;

    public ClientId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ClientId clientId = (ClientId) o;
        return Objects.equals(getId(), clientId.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
