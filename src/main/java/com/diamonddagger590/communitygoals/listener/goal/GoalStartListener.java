package com.diamonddagger590.communitygoals.listener.goal;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.api.event.goal.GoalStartEvent;
import com.diamonddagger590.communitygoals.config.FileType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class GoalStartListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void handleGoalStart(GoalStartEvent goalStartEvent) {
        CommunityGoals.getInstance().getGoalManager().trackGoal(goalStartEvent.getGoal());
        Audience audience = CommunityGoals.getInstance().getAdventure().all();
        MiniMessage miniMessage = CommunityGoals.getInstance().getMiniMessage();
        FileConfiguration fileConfiguration = CommunityGoals.getInstance().getFileManager().getCustomFile(FileType.GOALS_CONFIG).getFileConfiguration();
        String path = "goals." + goalStartEvent.getGoal().getCriteriaConfigId() + ".start_message";
        if (!fileConfiguration.contains(path) || fileConfiguration.getString(path).isEmpty()) {
            Component parsed = miniMessage.deserialize("<green>Goal " + goalStartEvent.getGoal().getName()
                    + " has started!</green>");
            audience.sendMessage(parsed);
        }
        else {
            audience.sendMessage(miniMessage.deserialize(fileConfiguration.getString(path)));
        }
    }

}
