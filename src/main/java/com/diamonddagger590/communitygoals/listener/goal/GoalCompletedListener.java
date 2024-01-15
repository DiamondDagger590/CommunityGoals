package com.diamonddagger590.communitygoals.listener.goal;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.api.event.goal.GoalCompleteEvent;
import com.diamonddagger590.communitygoals.config.FileType;
import com.diamonddagger590.communitygoals.goal.GoalManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
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
        FileConfiguration fileConfiguration = CommunityGoals.getInstance().getFileManager().getCustomFile(FileType.GOALS_CONFIG).getFileConfiguration();
        String path = "goals." + goalCompleteEvent.getGoal().getCriteriaConfigId() + ".completion_message";
        if (!fileConfiguration.contains(path) || fileConfiguration.getString(path).isEmpty()) {
            Component parsed = miniMessage.deserialize("<green>Goal " + goalCompleteEvent.getGoal().getName()
                    + " has been completed!</green>");
            audience.sendMessage(parsed);
        } else {
            audience.sendMessage(miniMessage.deserialize(fileConfiguration.getString(path)));
        }
    }
}
