package com.diamonddagger590.communitygoals.task;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.player.CGPlayer;
import com.diamonddagger590.mccore.task.PlayerLoadTask;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

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
        return true;
    }

    @Override
    protected void onPlayerLoadSuccessfully() {
        super.onPlayerLoadSuccessfully();
        getPlugin().getLogger().log(Level.INFO, "Player data has been loaded for player: " + getCorePlayer().getUUID());
        getPlugin().getPlayerManager().addPlayer(getCorePlayer());
    }

    @Override
    protected void onPlayerLoadFail() {
        super.onPlayerLoadFail();
        getPlugin().getLogger().log(Level.SEVERE, ChatColor.RED + "There was an issue loading in the CommunityGoals player data for player with UUID: " + getCorePlayer().getUUID());
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
