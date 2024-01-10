package com.diamonddagger590.communitygoals.goal.criteria;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class Criteria {

    @NotNull
    public abstract CriteriaType getCriteriaType();

    public abstract boolean canCriteriaProcessEvent(@NotNull Class<? extends Event> event);
}
