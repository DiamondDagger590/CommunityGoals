package com.diamonddagger590.communitygoals.api.event.goal;

import com.diamonddagger590.communitygoals.goal.Goal;
import com.diamonddagger590.communitygoals.player.CGPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class GoalContributionEvent extends GoalEvent {

    private final Optional<CGPlayer> cgPlayer;
    private int contributionAmount;

    public GoalContributionEvent(@NotNull Goal goal, int contributionAmount) {
        super(goal);
        this.cgPlayer = Optional.empty();
        this.contributionAmount = contributionAmount;
    }

    public GoalContributionEvent(@NotNull Goal goal, @NotNull CGPlayer cgPlayer, int contributionAmount) {
        super(goal);
        this.cgPlayer = Optional.of(cgPlayer);
        this.contributionAmount = contributionAmount;
    }

    public Optional<CGPlayer> getCgPlayer() {
        return cgPlayer;
    }

    public int getContributionAmount() {
        return contributionAmount;
    }

}
