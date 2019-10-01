package com.honydev.hotsquesttracker.data.herodata;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Thomas on 28.10.2018.
 */
public class TalentTier {

    @JsonProperty("tier")
    private int tier;
    @JsonProperty("talentList")
    private List<Talent> talentList;

    public TalentTier() {

    }

    public TalentTier(int tier, List<Talent> talentList) {
        this.tier = tier;
        this.talentList = talentList;
    }

    public int getTier() {
        return tier;
    }

    public List<Talent> getTalentList() {
        return talentList;
    }
}
