package com.diamonddagger590.communitygoals.listener.player;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.mccore.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void handleQuit(PlayerQuitEvent playerQuitEvent) {
        PlayerManager playerManager = CommunityGoals.getInstance().getPlayerManager();
        playerManager.removePlayer(playerQuitEvent.getPlayer().getUniqueId());
    }
}
