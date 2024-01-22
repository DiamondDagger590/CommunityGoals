package com.diamonddagger590.communitygoals.player;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.database.table.CGPlayerDAO;
import com.diamonddagger590.communitygoals.goal.Goal;
import com.diamonddagger590.mccore.player.CorePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class CGPlayer extends CorePlayer {

    private Map<Integer, PlayerContribution> contributions;
    private Set<Integer> idsLookedUp;

    public CGPlayer(@NotNull Player player) {
        this(player.getUniqueId());
        contributions = new HashMap<>();
        idsLookedUp = new HashSet<>();
    }

    public CGPlayer(@NotNull UUID uuid) {
        super(uuid);
    }

    public void addContribution(@NotNull Goal goal, int contribution) {
        addContribution(goal.getId(), contribution);
    }

    public void addContribution(int goalId, int contribution) {
        if (contribution > 0) {
            if (contributions.containsKey(goalId)) {
                contribution += contributions.get(goalId).contributionAmount();
            }
            PlayerContribution playerContribution = new PlayerContribution(goalId, getUUID(), contribution);
            contributions.put(goalId, playerContribution);
            saveContribution(playerContribution);
        }
    }

    @NotNull
    public Optional<PlayerContribution> getPlayerContribution(int goalId) {
        if (contributions.containsKey(goalId)) {
            return Optional.of(contributions.get(goalId));
        }

        if (!idsLookedUp.contains(goalId)) {
            idsLookedUp.add(goalId);
            Connection connection = CommunityGoals.getInstance().getDatabaseManager().getDatabase().getConnection();
            CGPlayerDAO.getPlayerContribution(connection, getUUID(), goalId).thenAccept(playerContribution -> {
                contributions.put(goalId, playerContribution);
            }).exceptionally(throwable -> {
                throwable.printStackTrace();
                return null;
            });
        }
        return Optional.empty();
    }

    @Override
    public boolean useMutex() {
        return false;
    }

    private void saveContribution(@NotNull PlayerContribution contribution) {
        Connection connection = CommunityGoals.getInstance().getDatabaseManager().getDatabase().getConnection();
        CGPlayerDAO.savePlayerContribution(connection, contribution).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }
}
