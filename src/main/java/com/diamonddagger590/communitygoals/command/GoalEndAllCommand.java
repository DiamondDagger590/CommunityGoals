package com.diamonddagger590.communitygoals.command;

import cloud.commandframework.CommandManager;
import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.goal.Goal;
import com.diamonddagger590.communitygoals.goal.GoalManager;
import com.google.common.collect.ImmutableSet;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GoalEndAllCommand {

    public static void registerCommand() {
        CommunityGoals communityGoals = CommunityGoals.getInstance();
        CommandManager<CommandSender> commandManager = communityGoals.getBukkitCommandManager();

        commandManager.command(commandManager.commandBuilder("goal")
                .literal("end")
                .literal("all")
                .permission("communitygoals.end")
                .senderType(Player.class)
                .handler(commandContext -> {
                            Player player = (Player) commandContext.getSender();
                            GoalManager goalManager = communityGoals.getGoalManager();
                            ImmutableSet<Goal> activeGoals = goalManager.getActiveGoals();
                            for (Goal goal : activeGoals) {
                                goal.endGoal();
                                goalManager.stopTrackingGoal(goal);
                            }
                            Audience audience = CommunityGoals.getInstance().getAdventure().player(player);
                            MiniMessage miniMessage = CommunityGoals.getInstance().getMiniMessage();
                            Component parsed = miniMessage.deserialize("<green>You have ended all active goals!</green>");
                            audience.sendMessage(parsed);
                        }
                ));
    }

}
