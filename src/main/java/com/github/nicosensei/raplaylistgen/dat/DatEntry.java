package com.github.nicosensei.raplaylistgen.dat;

public final class DatEntry {

    private String name;
    private String description;

    public DatEntry(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public DatEntry setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DatEntry setDescription(String description) {
        this.description = description;
        return this;
    }

}
