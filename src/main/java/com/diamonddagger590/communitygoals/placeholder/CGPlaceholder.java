package com.diamonddagger590.communitygoals.placeholder;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.goal.Goal;
import com.diamonddagger590.communitygoals.goal.GoalManager;
import com.diamonddagger590.communitygoals.player.CGPlayer;
import com.diamonddagger590.communitygoals.player.PlayerContribution;
import com.diamonddagger590.mccore.player.CorePlayer;
import com.diamonddagger590.mccore.player.PlayerManager;
import com.diamonddagger590.mccore.util.Methods;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.util.Optional;
import java.util.UUID;

public class CGPlaceholder extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "cg";
    }

    @Override
    public String getAuthor() {
        return "DiamondDagger590";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String params) {
        CommunityGoals communityGoals = CommunityGoals.getInstance();
        PlayerManager playerManager = communityGoals.getPlayerManager();
        UUID uuid = offlinePlayer.getUniqueId();
        Optional<CorePlayer> corePlayerOptional = playerManager.getPlayer(uuid);
        params = params.toLowerCase();


        String[] args = params.split("_");
        // If the last argument is an int, we assume it is a goal id
        if (Methods.isInt(args[args.length - 1])) {
            int goal_id = Integer.parseInt(args[args.length - 1]);
            // If this is player contribution
            if (params.contains("player_contribution")){
                if (corePlayerOptional.isPresent() && corePlayerOptional.get() instanceof CGPlayer cgPlayer) {
                    Optional<PlayerContribution> playerContributionOptional = cgPlayer.getPlayerContribution(goal_id);
                    if (playerContributionOptional.isPresent()) {
                        return Integer.toString(playerContributionOptional.get().contributionAmount());
                    }
                }
                return "0";
            }
            GoalManager goalManager = communityGoals.getGoalManager();
            Optional<Goal> goalOptional = goalManager.getGoal(goal_id);
            Optional<Goal> retiredGoal = goalManager.getRetiredGoal(goal_id);
            Goal goal = goalOptional.orElseGet(() -> retiredGoal.orElse(null));
            if (goal != null) {
                if (params.contains("required_contribution")) {
                    return Integer.toString(goal.getRequiredContribution());
                } else if (params.contains("current_contribution")) {
                    return Integer.toString(goal.getCurrentContribution());
                } else if (params.contains("remaining_contribution")) {
                    return Integer.toString(goal.getRemainingContribution());
                } else if (params.contains("name")) {
                    return goal.getName();
                }
            } else {
                return "INVALID_GOAL_ID";
            }
        }

        return null;
    }
}
