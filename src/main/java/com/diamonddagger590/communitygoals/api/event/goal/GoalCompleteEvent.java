package com.diamonddagger590.communitygoals.api.event.goal;

import com.diamonddagger590.communitygoals.goal.Goal;
import org.jetbrains.annotations.NotNull;

public class GoalCompleteEvent extends GoalEvent {

    public GoalCompleteEvent(@NotNull Goal goal) {
        super(goal);
    }
}
