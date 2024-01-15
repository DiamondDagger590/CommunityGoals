package com.diamonddagger590.communitygoals.goal.criteria;

import org.jetbrains.annotations.NotNull;

public abstract class Criteria {

    protected String configSetting;

    public Criteria(@NotNull String configSetting) {
        this.configSetting = configSetting;
    }

    @NotNull
    public abstract CriteriaType getCriteriaType();

    public abstract void loadCriteria();
}
