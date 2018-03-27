package chiroito.sandbox.accs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {

    public Item() {
    }

    public Item(long id, String name) {
        this.id = id;
        this.name = name;
    }

    private long id;
    private String name;

    @JsonProperty
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
