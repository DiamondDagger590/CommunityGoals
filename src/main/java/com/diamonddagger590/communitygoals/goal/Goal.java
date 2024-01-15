package com.diamonddagger590.communitygoals.goal;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.api.event.goal.GoalCompleteEvent;
import com.diamonddagger590.communitygoals.api.event.goal.GoalEndEvent;
import com.diamonddagger590.communitygoals.api.event.goal.GoalStartEvent;
import com.diamonddagger590.communitygoals.config.FileType;
import com.diamonddagger590.communitygoals.database.table.GoalDAO;
import com.diamonddagger590.communitygoals.exception.GoalAlreadyStartedException;
import com.diamonddagger590.communitygoals.exception.IllegalGoalConfigIdException;
import com.diamonddagger590.communitygoals.goal.criteria.Criteria;
import com.diamonddagger590.communitygoals.goal.criteria.CriteriaType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class Goal {

    private int id;
    private String name;
    private Criteria criteria;
    private String criteriaConfigName;
    private int requiredContribution;
    private int currentContribution;
    private long startTime;
    private long endTime;

    Goal(int id, @NotNull String criteriaConfigName) throws IllegalGoalConfigIdException {
        if (id <= 0) {
            throw new IllegalArgumentException(String.format("An invalid id of %d was passed in for a Goal.", id));
        }
        this.id = id;
        this.criteriaConfigName = criteriaConfigName;
        loadCriteria(true);
        this.currentContribution = 0;
        this.startTime = -1;
        this.endTime = -1;
    }

    public Goal(int id, @NotNull String name, @NotNull String criteriaConfigName, int currentContribution, long startTime, long endTime) throws IllegalGoalConfigIdException {
        this.id = id;
        this.name = name;
        this.criteriaConfigName = criteriaConfigName;
        loadCriteria(false);
        this.currentContribution = currentContribution;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void loadCriteria(boolean loadAll) throws IllegalGoalConfigIdException {
        FileConfiguration fileConfiguration = CommunityGoals.getInstance().getFileManager().getCustomFile(FileType.GOALS_CONFIG).getFileConfiguration();
        try {
            CriteriaType criteriaType = CriteriaType.valueOf(fileConfiguration.getString("goals." + criteriaConfigName + ".contribution_type"));
            criteria = criteriaType.getCriteria(criteriaConfigName);
            requiredContribution = fileConfiguration.getInt("goals." + criteriaConfigName + ".required_contribution");
            if (loadAll) {
                name = fileConfiguration.getString("goals." + criteriaConfigName + ".name");
            }
        }
        catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalGoalConfigIdException(String.format("Could not load criteria data for goal with configuration id %s", criteriaConfigName));
        }

    }

    public int getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getCriteriaConfigId() {
        return criteriaConfigName;
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

    public void reloadGoal() {
        try {
            loadCriteria(false);
        }
        catch (IllegalGoalConfigIdException e) {
            e.printStackTrace();
        }
        // Check to see if contribution went lower than what was allowed
        if (currentContribution >= requiredContribution) {
            completeGoal();
        }
    }
}
