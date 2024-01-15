package com.diamonddagger590.communitygoals;

import com.diamonddagger590.communitygoals.command.DonateItemCommand;
import com.diamonddagger590.communitygoals.command.GoalEndCommand;
import com.diamonddagger590.communitygoals.command.GoalListCommand;
import com.diamonddagger590.communitygoals.command.GoalStartCommand;
import com.diamonddagger590.communitygoals.command.ReloadCommand;
import com.diamonddagger590.communitygoals.config.FileManager;
import com.diamonddagger590.communitygoals.database.CommunityGoalsDatabaseManager;
import com.diamonddagger590.communitygoals.goal.GoalManager;
import com.diamonddagger590.communitygoals.goal.criteria.CriteriaManager;
import com.diamonddagger590.communitygoals.listener.goal.GoalCompletedListener;
import com.diamonddagger590.communitygoals.listener.goal.GoalContributionListener;
import com.diamonddagger590.communitygoals.listener.goal.GoalEndListener;
import com.diamonddagger590.communitygoals.listener.goal.GoalStartListener;
import com.diamonddagger590.communitygoals.listener.player.PlayerJoinListener;
import com.diamonddagger590.communitygoals.listener.player.PlayerQuitListener;
import com.diamonddagger590.mccore.CorePlugin;
import com.diamonddagger590.mccore.player.PlayerManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class CommunityGoals extends CorePlugin {

    private CriteriaManager criteriaManager;
    private GoalManager goalManager;
    private FileManager fileManager;

    @Override
    public void onEnable() {
        super.onEnable();

        this.fileManager = new FileManager(this);
        initializeDatabase();

        criteriaManager = new CriteriaManager();
        goalManager = new GoalManager();
        playerManager = new PlayerManager(this);

        constructCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        goalManager.shutdown();
        super.onDisable();
    }

    @Override
    public void initializeDatabase() {
        this.databaseManager = new CommunityGoalsDatabaseManager(this);
        this.databaseManager.initializeDatabase();
    }

    @Override
    protected void constructCommands() {
        super.constructCommands();
        DonateItemCommand.registerCommand();
        GoalStartCommand.registerCommand();
        GoalListCommand.registerCommand();
        GoalEndCommand.registerCommand();
        ReloadCommand.registerCommand();
    }

    @Override
    protected void registerListeners() {
        super.registerListeners();
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new GoalCompletedListener(), this);
        Bukkit.getPluginManager().registerEvents(new GoalContributionListener(), this);
        Bukkit.getPluginManager().registerEvents(new GoalEndListener(), this);
        Bukkit.getPluginManager().registerEvents(new GoalStartListener(), this);
    }

    @NotNull
    public CriteriaManager getCriteriaManager() {
        return criteriaManager;
    }

    @NotNull
    public GoalManager getGoalManager() {
        return goalManager;
    }

    @NotNull
    @Override
    public CommunityGoalsDatabaseManager getDatabaseManager() {
        return (CommunityGoalsDatabaseManager) super.getDatabaseManager();
    }

    @NotNull
    public FileManager getFileManager() {
        return fileManager;
    }

    @NotNull
    public static CommunityGoals getInstance() {
        return (CommunityGoals) CorePlugin.getInstance();
    }
}
