package com.diamonddagger590.communitygoals.task;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.player.CGPlayer;
import com.diamonddagger590.mccore.task.PlayerLoadTask;
import org.jetbrains.annotations.NotNull;

public class CGPlayerLoadTask extends PlayerLoadTask {

    public CGPlayerLoadTask(@NotNull CommunityGoals plugin, @NotNull CGPlayer corePlayer) {
        super(plugin, corePlayer);
    }

    @NotNull
    @Override
    public CommunityGoals getPlugin() {
        return (CommunityGoals) super.getPlugin();
    }

    @Override
    public CGPlayer getCorePlayer() {
        return (CGPlayer) super.getCorePlayer();
    }

    @Override
    protected boolean loadPlayer() {
        return false;
    }

    @Override
    protected void onPlayerLoadFail() {

    }

    @Override
    protected void onTaskExpire() {

    }

    @Override
    protected void onDelayComplete() {

    }

    @Override
    protected void onIntervalStart() {

    }

    @Override
    protected void onIntervalPause() {

    }

    @Override
    protected void onIntervalResume() {

    }
}
