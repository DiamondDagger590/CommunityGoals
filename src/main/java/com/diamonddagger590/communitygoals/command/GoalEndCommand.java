package com.diamonddagger590.communitygoals.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.IntegerArgument;
import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.goal.Goal;
import com.diamonddagger590.communitygoals.goal.GoalManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class GoalEndCommand {

    public static void registerCommand() {
        CommunityGoals communityGoals = CommunityGoals.getInstance();
        CommandManager<CommandSender> commandManager = communityGoals.getBukkitCommandManager();

        commandManager.command(commandManager.commandBuilder("goal")
                .literal("end")
                .argument(IntegerArgument.of("id"))
                .permission("communitygoals.end")
                .senderType(Player.class)
                .handler(commandContext -> {
                            Player player = (Player) commandContext.getSender();
                            int id = commandContext.get("id");

                            GoalManager goalManager = communityGoals.getGoalManager();
                            Optional<Goal> goalOptional = goalManager.getGoal(id);
                            Audience audience = CommunityGoals.getInstance().getAdventure().player(player);
                            MiniMessage miniMessage = CommunityGoals.getInstance().getMiniMessage();
                            if (goalOptional.isPresent()) {
                                Goal goal = goalOptional.get();
                                goal.endGoal();
                                goalManager.stopTrackingGoal(goal);
                                Component parsed = miniMessage.deserialize(String.format("<green>You have ended the active goal with id %d!</green>", goal.getId()));
                                audience.sendMessage(parsed);
                            } else {
                                Component parsed = miniMessage.deserialize("<red>An active goal does not exist with that id!</red>");
                                audience.sendMessage(parsed);
                            }
                        }
                ));
    }
}
