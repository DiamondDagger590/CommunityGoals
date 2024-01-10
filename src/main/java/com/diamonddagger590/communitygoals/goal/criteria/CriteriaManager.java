package com.diamonddagger590.communitygoals.goal.criteria;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class CriteriaManager {

    private final Set<Criteria> registeredCriteria;

    public CriteriaManager() {
        registeredCriteria = new HashSet<>();
    }

    public void registerCriteria(@NotNull Criteria criteria) {
        registeredCriteria.add(criteria);
    }

    public boolean isCriteriaRegistered(@NotNull Criteria criteria) {
        return registeredCriteria.contains(criteria);
    }
}
