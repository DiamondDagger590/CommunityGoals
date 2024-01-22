package com.diamonddagger590.communitygoals.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.IntegerArgument;
import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.goal.Goal;
import com.diamonddagger590.communitygoals.goal.GoalManager;
import com.diamonddagger590.communitygoals.player.CGPlayer;
import com.diamonddagger590.mccore.player.CorePlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class GoalInfoCommand {

    public static void registerCommand() {
        CommunityGoals communityGoals = CommunityGoals.getInstance();
        CommandManager<CommandSender> commandManager = communityGoals.getBukkitCommandManager();

        commandManager.command(commandManager.commandBuilder("goal")
                .literal("info")
                .argument(IntegerArgument.of("goal_id"))
                .permission("communitygoals.info")
                .senderType(Player.class)
                .handler(commandContext -> {
                            Player player = (Player) commandContext.getSender();
                            Optional<CorePlayer> playerOptional = communityGoals.getPlayerManager().getPlayer(player.getUniqueId());
                            Audience audience = CommunityGoals.getInstance().getAdventure().player(player);
                            MiniMessage miniMessage = CommunityGoals.getInstance().getMiniMessage();
                            int goalId = commandContext.get("goal_id");

                            if (playerOptional.isPresent() && playerOptional.get() instanceof CGPlayer cgPlayer) {
                                GoalManager goalManager = communityGoals.getGoalManager();
                                Goal goal = goalManager.getGoal(goalId).orElseGet(() -> goalManager.getRetiredGoal(goalId).orElse(null));
                                if (goal != null) {
                                    Component parsed = miniMessage.deserialize(String.format("\n<gray>ID: <gold>%d</gold> Name: <gold>%s</gold> Contribution: <gold>%d/%d</gold></gray>",
                                            goal.getId(), goal.getName(), goal.getCurrentContribution(), goal.getRequiredContribution()));

                                    AtomicInteger contribution = new AtomicInteger(0);
                                    cgPlayer.getPlayerContribution(goal.getId()).ifPresent(playerContribution -> contribution.set(playerContribution.contributionAmount()));

                                    parsed = parsed.append(miniMessage.deserialize(String.format("\n<hover:show_text:'<gray>You have contributed <gold>" + contribution.get() + "</gold> to this goal.</gray>'><gray>    - ID: <gold>%d</gold> Name: <gold>%s</gold> Contribution: <gold>%d/%d</gold></gray></hover>",
                                            goal.getId(), goal.getName(), goal.getCurrentContribution(), goal.getRequiredContribution())));
                                    if (player.hasPermission("communitygoals.end")) {
                                        parsed = parsed.append(miniMessage.deserialize("<click:run_command:/goal end " + goal.getId() + "><hover:show_text:'Runs command to end this goal'>" +
                                                "<red> [END]</red></hover></click>"));

                                    }
//
                                    audience.sendMessage(parsed);
                                } else {
                                    Component parsed = miniMessage.deserialize("<red>There is no active goal with that ID." +
                                            " We might be trying to fetch a retired goal so please try the command again in a few seconds." +
                                            " If you get this same message, then there is no goal with that id.");
                                    audience.sendMessage(parsed);
                                }
                            } else {
                                audience.sendMessage(miniMessage.deserialize("<red>Your data has not loaded yet, please wait a second before running this command."));
                            }
                        }
                ));
    }
}
