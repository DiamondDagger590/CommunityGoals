package com.diamonddagger590.communitygoals.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import com.diamonddagger590.communitygoals.CommunityGoals;
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
                .argument(StringArgument.quoted("goal_name"))
                .argument(StringArgument.single("goal_criteria"))
                .argument(IntegerArgument.of("required_contribution"))
                .permission("communitygoals.start")
                .senderType(Player.class)
                .handler(commandContext -> {
                            Player player = (Player) commandContext.getSender();

                            String goalName = commandContext.get("goal_name");
                            String criteria = commandContext.get("goal_criteria");
                            int required_contribution = commandContext.get("required_contribution");

                            GoalManager goalManager = communityGoals.getGoalManager();
                            Goal goal = goalManager.createGoal(goalName, required_contribution);
                            Audience audience = CommunityGoals.getInstance().getAdventure().player(player);
                            MiniMessage miniMessage = CommunityGoals.getInstance().getMiniMessage();
                            Component parsed = miniMessage.deserialize("<green>You have started a new goal!</green>");
                            audience.sendMessage(parsed);
                            goal.startGoal();
                            goalManager.trackGoal(goal);
                        }
                ));
    }
}
