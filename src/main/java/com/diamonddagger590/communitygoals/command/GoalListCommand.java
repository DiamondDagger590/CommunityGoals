package com.diamonddagger590.communitygoals.command;

import cloud.commandframework.CommandManager;
import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.goal.Goal;
import com.diamonddagger590.communitygoals.goal.GoalManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GoalListCommand {

    public static void registerCommand() {
        CommunityGoals communityGoals = CommunityGoals.getInstance();
        CommandManager<CommandSender> commandManager = communityGoals.getBukkitCommandManager();

        commandManager.command(commandManager.commandBuilder("goal")
                .literal("list")
                .permission("communitygoals.list")
                .senderType(Player.class)
                .handler(commandContext -> {
                            Player player = (Player) commandContext.getSender();

                            GoalManager goalManager = communityGoals.getGoalManager();
                            Audience audience = CommunityGoals.getInstance().getAdventure().player(player);
                            MiniMessage miniMessage = CommunityGoals.getInstance().getMiniMessage();
                            Component parsed = miniMessage.deserialize("<gray>All active goals:</gray>");
                            for (Goal goal : goalManager.getActiveGoals()) {
                                parsed = parsed.append(miniMessage.deserialize(String.format("\n<gray>    - ID: <gold>%d</gold> Name: <gold>%s</gold> Contribution: <gold>%d/%d</gold></gray>",
                                        goal.getId(), goal.getName(), goal.getCurrentContribution(), goal.getRequiredContribution())));
                            }

                            audience.sendMessage(parsed);
                        }
                ));
    }

}
