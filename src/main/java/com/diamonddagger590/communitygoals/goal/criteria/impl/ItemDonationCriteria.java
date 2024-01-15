package com.diamonddagger590.communitygoals.goal.criteria.impl;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.config.FileType;
import com.diamonddagger590.communitygoals.goal.criteria.Criteria;
import com.diamonddagger590.communitygoals.goal.criteria.CriteriaType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ItemDonationCriteria extends Criteria {

    private final Set<Material> acceptedItems;

    public ItemDonationCriteria(@NotNull String configSetting) {
        super(configSetting);
        acceptedItems = new HashSet<>();
        loadCriteria();
    }

    public boolean isItemAccepted(@NotNull ItemStack itemStack) {
        return acceptedItems.contains(itemStack.getType());
    }

    @NotNull
    @Override
    public CriteriaType getCriteriaType() {
        return CriteriaType.ITEM_DONATION;
    }

    @Override
    public void loadCriteria() {
        FileConfiguration fileConfiguration = CommunityGoals.getInstance().getFileManager().getCustomFile(FileType.GOALS_CONFIG).getFileConfiguration();
        for (String type : fileConfiguration.getStringList("goals." + configSetting + ".accepted_items")){
            Material material = Material.getMaterial(type);
            if (material != null) {
                acceptedItems.add(material);
            }
        }
    }

}
