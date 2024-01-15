package com.diamonddagger590.communitygoals.command;

import cloud.commandframework.CommandManager;
import com.diamonddagger590.communitygoals.CommunityGoals;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand {

    public static void registerCommand() {
        CommunityGoals communityGoals = CommunityGoals.getInstance();
        CommandManager<CommandSender> commandManager = communityGoals.getBukkitCommandManager();

        commandManager.command(commandManager.commandBuilder("goal")
                .literal("reload")
                .permission("communitygoals.reload")
                .senderType(Player.class)
                .handler(commandContext -> {
                            CommunityGoals.getInstance().getFileManager().reloadFiles();
                            CommunityGoals.getInstance().getGoalManager().reloadGoals();

                            Player player = (Player) commandContext.getSender();
                            Audience audience = CommunityGoals.getInstance().getAdventure().player(player);
                            MiniMessage miniMessage = CommunityGoals.getInstance().getMiniMessage();
                            audience.sendMessage(miniMessage.deserialize("<green>Reloaded plugin!</green>"));
                        }
                ));
    }
}
