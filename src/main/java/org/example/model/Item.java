package org.example.model;

import java.util.UUID;

public class Item {
    private String name;
    private UUID id;

    public Item(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public Item(String name, String id) {
        this.name = name;
        this.id = UUID.fromString(id);
    }


    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }
}
