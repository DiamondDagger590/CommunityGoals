package com.diamonddagger590.communitygoals.listener.goal;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.api.event.goal.GoalContributionEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class GoalContributionListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handleContributionLowest(GoalContributionEvent goalContributionEvent) {
        goalContributionEvent.getCgPlayer().ifPresent(cgPlayer -> {
            cgPlayer.addContribution(goalContributionEvent.getGoal(), goalContributionEvent.getContributionAmount());
            cgPlayer.getAsBukkitPlayer().ifPresent(player -> {
                Audience audience = CommunityGoals.getInstance().getAdventure().player(player);
                MiniMessage miniMessage = CommunityGoals.getInstance().getMiniMessage();
                Component parsed = miniMessage.deserialize("<green>You have contributed " + goalContributionEvent.getContributionAmount() + " to the goal, we value your commitment.</green>");
                audience.sendMessage(parsed);
            });
        });
    }
}
