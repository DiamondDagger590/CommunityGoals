package com.diamonddagger590.communitygoals.goal;

import com.diamonddagger590.communitygoals.exception.GoalAlreadyStartedException;
import com.diamonddagger590.communitygoals.goal.criteria.CriteriaType;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.UUID;

public class Goal {

    private final UUID uuid;
    private final CriteriaType criteriaType;
    private final int requiredContribution;
    private int currentContribution;
    private long startTime;
    private long endTime;

    public Goal(@NotNull CriteriaType criteriaType, int requiredContribution) {
        this.uuid = UUID.randomUUID();
        this.criteriaType = criteriaType;
        this.requiredContribution = requiredContribution;
        this.currentContribution = 0;
        this.startTime = -1;
        this.endTime = -1;
    }

    @NotNull
    public UUID getGoalUUID() {
        return uuid;
    }

    @NotNull
    public CriteriaType getCriteriaType() {
        return criteriaType;
    }

    public int getRequiredContribution() {
        return requiredContribution;
    }

    public void updateCurrentContribution(int amount) {
        currentContribution += amount;
    }

    public int getCurrentContribution() {
        return currentContribution;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void startGoal() {
        if (startTime != -1) {
            throw new GoalAlreadyStartedException(String.format("Goal with UUID %s has already started and another attempt was made to start it.", uuid.toString()));
        }
        startTime = Calendar.getInstance().getTimeInMillis();
    }

    public void completeGoal() {
        endTime = Calendar.getInstance().getTimeInMillis();
    }

    public void endGoal() {
        endTime = Calendar.getInstance().getTimeInMillis();
    }
}
