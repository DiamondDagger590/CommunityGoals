package com.diamonddagger590.communitygoals.gui;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.api.event.goal.GoalContributionEvent;
import com.diamonddagger590.communitygoals.goal.Goal;
import com.diamonddagger590.communitygoals.goal.GoalManager;
import com.diamonddagger590.communitygoals.goal.criteria.impl.ItemDonationCriteria;
import com.diamonddagger590.communitygoals.player.CGPlayer;
import com.diamonddagger590.mccore.CorePlugin;
import com.diamonddagger590.mccore.exception.CorePlayerOfflineException;
import com.diamonddagger590.mccore.gui.ClosableGui;
import com.diamonddagger590.mccore.gui.Gui;
import com.diamonddagger590.mccore.gui.GuiTracker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DonationGui extends Gui implements ClosableGui {

    private final CGPlayer cgPlayer;
    private final int goalId;
    private final Inventory inventory;

    public DonationGui(@NotNull CGPlayer cgPlayer, int goalId) {
        this.cgPlayer = cgPlayer;
        assert (goalId > 0);
        this.goalId = goalId;
        Optional<Player> playerOptional = cgPlayer.getAsBukkitPlayer();
        if (playerOptional.isEmpty()) {
            throw new CorePlayerOfflineException(cgPlayer);
        }
        inventory = Bukkit.createInventory(playerOptional.get(), 54, CorePlugin.getInstance().getMiniMessage().deserialize("<gold>Donate items here"));
    }

    @NotNull
    public CGPlayer getPlayer() {
        return cgPlayer;
    }

    public int getGoalId() {
        return goalId;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(this, CommunityGoals.getInstance());
    }

    @Override
    public void unregisterListeners() {
        InventoryCloseEvent.getHandlerList().unregister(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleCloseEvent(InventoryCloseEvent inventoryCloseEvent) {
        handleClose(inventoryCloseEvent);
    }

    @Override
    public void handleClose(@NotNull InventoryCloseEvent inventoryCloseEvent) {
        if (inventoryCloseEvent.getPlayer() instanceof Player player) {
            handleClose(player, inventoryCloseEvent.getInventory());
        }
    }

    @Override
    public void handleClose(@NotNull Player player, @NotNull Inventory inventory) {
        if (this.inventory == inventory
                && player.getUniqueId().equals(cgPlayer.getUUID())) {
            CommunityGoals communityGoals = CommunityGoals.getInstance();
            GuiTracker guiTracker = communityGoals.getGuiTracker();
            GoalManager goalManager = communityGoals.getGoalManager();
            Optional<Gui> guiOptional = guiTracker.getOpenedGui(player);
            Optional<Goal> goalOptional = goalManager.getGoal(goalId);
            // Ensure they have a gui open, and if they do then make sure it's a donation gui AND there is a goal ongoing
            if (guiOptional.isPresent() && guiOptional.get() instanceof DonationGui donationGui && goalOptional.isPresent()) {
                Goal goal = goalOptional.get();
                if (goal.isRunning() && goal.getCriteria() instanceof ItemDonationCriteria itemDonationCriteria) {

                    int remainingContribution = goal.getRemainingContribution();
                    for (int i = 0; i < inventory.getSize(); i++) {
                        if (remainingContribution == 0) {
                            break;
                        }
                        ItemStack itemStack = inventory.getItem(i);
                        if (itemStack != null && itemStack.getType() != Material.AIR) {
                            // If the item is accepted as contribution
                            if (itemDonationCriteria.isItemAccepted(itemStack)) {
                                // If the item won't cover the remaining contributions
                                if (remainingContribution > itemStack.getAmount()) {
                                    remainingContribution -= itemStack.getAmount();
                                    inventory.setItem(i, new ItemStack(Material.AIR));
                                }
                                // If the item will cover the remaining contributions
                                else {
                                    int newAmount = itemStack.getAmount() - remainingContribution;
                                    remainingContribution = 0;
                                    if (newAmount == 0) {
                                        inventory.setItem(i, new ItemStack(Material.AIR));
                                    } else {
                                        itemStack.setAmount(newAmount);
                                    }
                                }
                            }
                        }
                    }

                    // If there was contribution made
                    int amountToDonate = goal.getRemainingContribution() - remainingContribution;
                    if (amountToDonate > 0) {
                        // Update contribution
                        GoalContributionEvent goalContributionEvent = new GoalContributionEvent(goal, cgPlayer, amountToDonate);
                        Bukkit.getPluginManager().callEvent(goalContributionEvent);
                        goal.updateCurrentContribution(amountToDonate);
                    }
                    // Give player leftover contents
                    for (ItemStack itemStack : inventory.getContents()) {
                        if (itemStack != null && itemStack.getType() != Material.AIR) {
                            player.getInventory().addItem(itemStack).values().forEach(leftover -> player.getWorld().dropItemNaturally(player.getLocation(), itemStack));
                        }
                    }
                    // Clear inventory
                    inventory.clear();
                }
            }
        }
    }
}
