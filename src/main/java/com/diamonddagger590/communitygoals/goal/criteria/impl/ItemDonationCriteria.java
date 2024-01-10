package com.diamonddagger590.communitygoals.goal.criteria.impl;

import com.diamonddagger590.communitygoals.goal.criteria.Criteria;
import com.diamonddagger590.communitygoals.goal.criteria.CriteriaType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class ItemDonationCriteria extends Criteria {

    @NotNull
    @Override
    public CriteriaType getCriteriaType() {
        return CriteriaType.ITEM_DONATION;
    }

    @Override
    public boolean canCriteriaProcessEvent(@NotNull Class<? extends Event> event) {
        return false;
    }
}
