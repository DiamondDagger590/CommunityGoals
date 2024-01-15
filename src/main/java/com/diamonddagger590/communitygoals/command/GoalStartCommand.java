package com.diamonddagger590.communitygoals.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.exception.IllegalGoalConfigIdException;
import com.diamonddagger590.communitygoals.goal.Goal;
import com.diamonddagger590.communitygoals.goal.GoalManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GoalStartCommand {

    public static void registerCommand() {
        CommunityGoals communityGoals = CommunityGoals.getInstance();
        CommandManager<CommandSender> commandManager = communityGoals.getBukkitCommandManager();

        commandManager.command(commandManager.commandBuilder("goal")
                .literal("start")
                .argument(StringArgument.single("goal_criteria_id"))
                .permission("communitygoals.start")
                .senderType(Player.class)
                .handler(commandContext -> {
                            Player player = (Player) commandContext.getSender();
                            String criteria = commandContext.get("goal_criteria_id");
                            GoalManager goalManager = communityGoals.getGoalManager();
                            Audience audience = CommunityGoals.getInstance().getAdventure().player(player);
                            MiniMessage miniMessage = CommunityGoals.getInstance().getMiniMessage();
                            try {
                                Goal goal = goalManager.createGoal(criteria);
                                Component parsed = miniMessage.deserialize("<green>You have started a new goal!</green>");
                                audience.sendMessage(parsed);
                                goal.startGoal();
                                goalManager.trackGoal(goal);
                            } catch (IllegalGoalConfigIdException e) {
                                audience.sendMessage(miniMessage.deserialize("<red>There was an issue starting that goal. Please ensure the configuration for it is correct."));
                            }
                        }
                ));
    }
}
