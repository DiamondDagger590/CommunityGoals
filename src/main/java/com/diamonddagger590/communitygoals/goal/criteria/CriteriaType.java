package com.diamonddagger590.communitygoals.goal.criteria;

import com.diamonddagger590.communitygoals.goal.criteria.impl.ItemDonationCriteria;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public enum CriteriaType {

    ITEM_DONATION(ItemDonationCriteria.class);

    private Class<? extends Criteria> clazz;

    CriteriaType(Class<? extends Criteria> clazz) {
        this.clazz = clazz;
    }

    public Criteria getCriteria(@NotNull String configName) {
        try {
             return clazz.getConstructor(String.class).newInstance(configName);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
