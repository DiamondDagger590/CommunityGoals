package com.diamonddagger590.communitygoals.goal.criteria.impl;

import com.diamonddagger590.communitygoals.goal.criteria.Criteria;
import com.diamonddagger590.communitygoals.goal.criteria.CriteriaType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ItemDonationCriteria extends Criteria {

    private final Set<Material> acceptedItems;

    public ItemDonationCriteria(@NotNull String configSetting) {
        super(configSetting);
        acceptedItems = Set.of(Material.DIRT);
    }

    public boolean isItemAccepted(@NotNull ItemStack itemStack) {
        return acceptedItems.contains(itemStack.getType());
    }

    @NotNull
    @Override
    public CriteriaType getCriteriaType() {
        return CriteriaType.ITEM_DONATION;
    }

}
