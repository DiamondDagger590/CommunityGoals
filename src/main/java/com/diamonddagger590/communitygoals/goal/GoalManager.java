package com.diamonddagger590.communitygoals.goal;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.database.table.GoalDAO;
import com.diamonddagger590.communitygoals.exception.IllegalGoalConfigIdException;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GoalManager {

    private int latestGoalId;
    private Map<Integer, Goal> activeGoals;

    public GoalManager() {
        activeGoals = new HashMap<>();
        try {
            Connection connection = CommunityGoals.getInstance().getDatabaseManager().getDatabase().getConnection();
            latestGoalId = GoalDAO.getLastUsedGoalId(connection).get();
            GoalDAO.getAllUnfinishedGoals(connection).get().forEach(this::trackGoal);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public Goal createGoal(@NotNull String criteriaConfigName) throws IllegalGoalConfigIdException {
        Goal goal = new Goal(latestGoalId + 1, criteriaConfigName);
        latestGoalId = goal.getId();
        goal.saveGoal();
        return goal;
    }

    public void trackGoal(@NotNull Goal goal) {
        activeGoals.put(goal.getId(), goal);
    }

    public void stopTrackingGoal(@NotNull Goal goal) {
        stopTrackingGoal(goal.getId());
    }

    public void stopTrackingGoal(int id) {
        activeGoals.remove(id);
    }

    public Optional<Goal> getGoal(int id) {
        return Optional.ofNullable(activeGoals.get(id));
    }

    public ImmutableSet<Goal> getActiveGoals() {
        return ImmutableSet.copyOf(activeGoals.values());
    }

    public void reloadGoals() {
        for (Goal goal : ImmutableSet.copyOf(activeGoals.values())) {
            goal.reloadGoal();
        }
    }

    public void shutdown() {
        CompletableFuture<Void>[] completableFutures = new CompletableFuture[activeGoals.size()];
        List<Goal> goals = new ArrayList<>(activeGoals.values());
        Connection connection = CommunityGoals.getInstance().getDatabaseManager().getDatabase().getConnection();
        for (int i = 0; i < completableFutures.length; i++) {
            completableFutures[i] = GoalDAO.saveGoal(connection, goals.get(i));
        }
        CompletableFuture<Void> allFuture = CompletableFuture.allOf(completableFutures);
        try {
            allFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
