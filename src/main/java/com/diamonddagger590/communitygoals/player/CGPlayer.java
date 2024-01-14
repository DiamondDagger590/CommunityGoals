package com.diamonddagger590.communitygoals.player;

import com.diamonddagger590.mccore.player.CorePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CGPlayer extends CorePlayer {

    public CGPlayer(@NotNull Player player) {
        this(player.getUniqueId());
    }

    public CGPlayer(@NotNull UUID uuid) {
        super(uuid);
    }

    @Override
    public boolean useMutex() {
        return false;
    }
}
