package com.honydev.hotsquesttracker.data.herodata;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 28.10.2018.
 */
public class HeroData {

    @JsonProperty("name")
    private String name;
    @JsonProperty("talentTiers")
    private List<TalentTier> talentTiers;

    public HeroData() {

    }

    public HeroData(String name, List<TalentTier> talentTiers) {
        this.name = name;
        this.talentTiers = talentTiers;
    }

    public String getName() {
        return name;
    }

    public List<TalentTier> getTalentTiers() {
        return talentTiers;
    }

    public List<Talent> getQuests() {
        return talentTiers
                .stream()
                .flatMap(talentTier -> talentTier.getTalentList().stream()).filter(Talent::isQuest)
                .collect(Collectors.toList());

    }
}
