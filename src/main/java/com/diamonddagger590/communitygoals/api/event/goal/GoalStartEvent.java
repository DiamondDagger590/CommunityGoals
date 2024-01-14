package com.diamonddagger590.communitygoals.api.event.goal;

import com.diamonddagger590.communitygoals.goal.Goal;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public class GoalStartEvent extends GoalEvent implements Cancellable {

    private boolean cancelled;

    public GoalStartEvent(@NotNull Goal goal) {
        super(goal);
        cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }


}
