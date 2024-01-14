package com.diamonddagger590.communitygoals.listener.goal;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.api.event.goal.GoalCompleteEvent;
import com.diamonddagger590.communitygoals.goal.GoalManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class GoalCompletedListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleGoalCompleted(GoalCompleteEvent goalCompleteEvent) {
        GoalManager goalManager = CommunityGoals.getInstance().getGoalManager();
        goalManager.stopTrackingGoal(goalCompleteEvent.getGoal());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void monitorGoalCompleted(GoalCompleteEvent goalCompleteEvent) {
        Audience audience = CommunityGoals.getInstance().getAdventure().all();
        MiniMessage miniMessage = CommunityGoals.getInstance().getMiniMessage();
        Component parsed = miniMessage.deserialize("<gold>The goal has been completed!.</gold>");
        audience.sendMessage(parsed);
    }
}
