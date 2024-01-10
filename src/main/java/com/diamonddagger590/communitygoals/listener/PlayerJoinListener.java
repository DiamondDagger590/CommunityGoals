package com.diamonddagger590.communitygoals.listener;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.player.CGPlayer;
import com.diamonddagger590.communitygoals.task.CGPlayerLoadTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void handleLogin(PlayerJoinEvent playerJoinEvent) {
        CommunityGoals communityGoals = CommunityGoals.getInstance();
        Player player = playerJoinEvent.getPlayer();
        CGPlayer cgPlayer = new CGPlayer(player);

        new CGPlayerLoadTask(communityGoals, cgPlayer).run();
    }
}
