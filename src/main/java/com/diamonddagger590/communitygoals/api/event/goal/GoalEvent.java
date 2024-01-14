package com.diamonddagger590.communitygoals.api.event.goal;

import com.diamonddagger590.communitygoals.goal.Goal;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class GoalEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Goal goal;

    public GoalEvent(@NotNull Goal goal) {
        this.goal = goal;
    }

    @NotNull
    public Goal getGoal() {
        return goal;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
