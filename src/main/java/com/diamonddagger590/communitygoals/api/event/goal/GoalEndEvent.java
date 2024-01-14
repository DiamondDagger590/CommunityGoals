package com.diamonddagger590.communitygoals.api.event.goal;

import com.diamonddagger590.communitygoals.goal.Goal;
import org.jetbrains.annotations.NotNull;

public class GoalEndEvent extends GoalEvent{

    public GoalEndEvent(@NotNull Goal goal) {
        super(goal);
    }

}
