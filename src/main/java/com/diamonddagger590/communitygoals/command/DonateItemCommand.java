package com.diamonddagger590.communitygoals.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.IntegerArgument;
import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.goal.Goal;
import com.diamonddagger590.communitygoals.goal.GoalManager;
import com.diamonddagger590.communitygoals.goal.criteria.CriteriaType;
import com.diamonddagger590.communitygoals.gui.DonationGui;
import com.diamonddagger590.communitygoals.player.CGPlayer;
import com.diamonddagger590.mccore.player.PlayerManager;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class DonateItemCommand {

    public static void registerCommand() {
        CommunityGoals communityGoals = CommunityGoals.getInstance();
        CommandManager<CommandSender> commandManager = communityGoals.getBukkitCommandManager();
        commandManager.command(commandManager.commandBuilder("goal")
                .literal("donate")
                .argument(IntegerArgument.of("goal_id"))
                .permission("communitygoals.donate")
                .senderType(Player.class)
                .handler(commandContext -> {
                    Player player = (Player) commandContext.getSender();
                    CommandSender commandSender = commandContext.getSender();
                    PlayerManager playerManager = communityGoals.getPlayerManager();
                    if (playerManager.getPlayer(player.getUniqueId()).isPresent()
                            && playerManager.getPlayer(player.getUniqueId()).get() instanceof CGPlayer cgPlayer) {
                        Audience audience = CommunityGoals.getInstance().getAdventure().player(player);
                        MiniMessage miniMessage = CommunityGoals.getInstance().getMiniMessage();
                        GoalManager goalManager = communityGoals.getGoalManager();
                        int goalId = commandContext.get("goal_id");
                        Optional<Goal> goalOptional = goalManager.getGoal(goalId);
                        if (goalOptional.isPresent()) {
                            Goal goal = goalOptional.get();
                            if (goal.isRunning()) {
                                if (goal.getCriteria().getCriteriaType() == CriteriaType.ITEM_DONATION) {
                                    DonationGui donationGui = new DonationGui(cgPlayer, goalId);
                                    communityGoals.getGuiTracker().trackPlayerGui(cgPlayer, donationGui);
                                    player.openInventory(donationGui.getInventory());
                                    return;
                                }
                                else {
                                    Component parsed = miniMessage.deserialize("<red>That goal can not be donated to.</red>");
                                    audience.sendMessage(parsed);
                                    return;
                                }
                            }
                        }

                        // Handle goal not working
                        Component parsed = miniMessage.deserialize("<red>There is no active goal with that id.</red>");
                        audience.sendMessage(parsed);
                    }
                }));
    }
}
