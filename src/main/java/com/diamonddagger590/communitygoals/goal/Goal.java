package com.diamonddagger590.communitygoals.goal;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.api.event.goal.GoalCompleteEvent;
import com.diamonddagger590.communitygoals.api.event.goal.GoalEndEvent;
import com.diamonddagger590.communitygoals.api.event.goal.GoalStartEvent;
import com.diamonddagger590.communitygoals.database.table.GoalDAO;
import com.diamonddagger590.communitygoals.exception.GoalAlreadyStartedException;
import com.diamonddagger590.communitygoals.goal.criteria.Criteria;
import com.diamonddagger590.communitygoals.goal.criteria.CriteriaType;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class Goal {

    private int id;
    private final String name;
    private final CriteriaType criteriaType;
    private final Criteria criteria;
    private final int requiredContribution;
    private int currentContribution;
    private long startTime;
    private long endTime;

    Goal(int id, @NotNull String name, @NotNull CriteriaType criteriaType, int requiredContribution) {
        if (id <= 0) {
            throw new IllegalArgumentException(String.format("An invalid id of %d was passed in for a Goal.", id));
        }
        this.id = id;
        this.name = name;
        this.criteriaType = criteriaType;
        this.criteria = criteriaType.getCriteria();
        this.requiredContribution = requiredContribution;
        this.currentContribution = 0;
        this.startTime = -1;
        this.endTime = -1;
    }

    public Goal(int id, @NotNull String name, @NotNull CriteriaType criteriaType, int requiredContribution, int currentContribution, long startTime, long endTime) {
        this.id = id;
        this.name = name;
        this.criteriaType = criteriaType;
        this.criteria = criteriaType.getCriteria();
        this.requiredContribution = requiredContribution;
        this.currentContribution = currentContribution;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public CriteriaType getCriteriaType() {
        return criteriaType;
    }

    @NotNull
    public Criteria getCriteria() {
        return criteria;
    }

    public int getRequiredContribution() {
        return requiredContribution;
    }

    public void updateCurrentContribution(int amount) {
        currentContribution = Math.min(currentContribution + amount, requiredContribution);
        if (currentContribution == requiredContribution) {
            completeGoal();
        }
        // #completeGoal calls save, so we want to ensure we only save once
        else {
            saveGoal();
        }
    }

    public int getCurrentContribution() {
        return currentContribution;
    }

    public int getRemainingContribution() {
        return requiredContribution - currentContribution;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public boolean isRunning() {
        return startTime != -1 && endTime == -1;
    }

    public void startGoal() {
        if (startTime != -1) {
            throw new GoalAlreadyStartedException(String.format("Goal with ID %d has already started and another attempt was made to start it.", id));
        }
        GoalStartEvent goalStartEvent = new GoalStartEvent(this);
        Bukkit.getPluginManager().callEvent(goalStartEvent);
        if (!goalStartEvent.isCancelled()) {
            startTime = Calendar.getInstance().getTimeInMillis();
            saveGoal();
        }
    }

    public void completeGoal() {
        endTime = Calendar.getInstance().getTimeInMillis();
        GoalCompleteEvent goalCompleteEvent = new GoalCompleteEvent(this);
        Bukkit.getPluginManager().callEvent(goalCompleteEvent);
        saveGoal();
    }

    public void endGoal() {
        endTime = Calendar.getInstance().getTimeInMillis();
        GoalEndEvent goalEndEvent = new GoalEndEvent(this);
        Bukkit.getPluginManager().callEvent(goalEndEvent);
        saveGoal();
    }

    public void saveGoal() {
        GoalDAO.saveGoal(CommunityGoals.getInstance().getDatabaseManager().getDatabase().getConnection(), this);
    }
}
