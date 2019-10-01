package com.honydev.hotsquesttracker.data.herodata;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Thomas on 28.10.2018.
 */
public class Talent {
    @JsonProperty("tier")
    private Integer tier;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("quest")
    private Boolean isQuest;

    public Talent() {

    }

    public Talent(int tier, String name, String description, boolean isQuest) {
        this.tier = tier;
        this.name = name;
        this.description = description;
        this.isQuest = isQuest;
    }

    public int getTier() {
        return tier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isQuest() {
        return isQuest;
    }

    @Override
    public String toString() {
        return "Talent{" +
                "tier=" + tier +
                ", name='" + name + '\'' +
                // ", description='" + description + '\'' +
                ", isQuest=" + isQuest +
                '}';
    }
}
