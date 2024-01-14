package com.diamonddagger590.communitygoals.listener.goal;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.api.event.goal.GoalEndEvent;
import com.diamonddagger590.communitygoals.goal.GoalManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class GoalEndListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleGoalEnded(GoalEndEvent goalEndEvent) {
        GoalManager goalManager = CommunityGoals.getInstance().getGoalManager();
        goalManager.stopTrackingGoal(goalEndEvent.getGoal());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void monitorGoalEnded(GoalEndEvent goalEndEvent) {
        Audience audience = CommunityGoals.getInstance().getAdventure().all();
        MiniMessage miniMessage = CommunityGoals.getInstance().getMiniMessage();
        Component parsed = miniMessage.deserialize("<gold>The goal has ended.</gold>");
        audience.sendMessage(parsed);
    }
}
