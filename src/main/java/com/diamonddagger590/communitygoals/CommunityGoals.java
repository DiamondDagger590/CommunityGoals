package com.diamonddagger590.communitygoals;

import com.diamonddagger590.communitygoals.database.CommunityGoalsDatabaseManager;
import com.diamonddagger590.communitygoals.goal.criteria.CriteriaManager;
import com.diamonddagger590.communitygoals.listener.PlayerJoinListener;
import com.diamonddagger590.mccore.CorePlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class CommunityGoals extends CorePlugin {

    private CriteriaManager criteriaManager;

    @Override
    public void onEnable() {
        super.onEnable();

        criteriaManager = new CriteriaManager();

        constructCommands();
        initializeDatabase();
        registerListeners();
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public void initializeDatabase() {
        this.databaseManager.initializeDatabase();
    }

    @Override
    protected void constructCommands() {
        super.constructCommands();
    }

    @Override
    protected void registerListeners() {
        super.registerListeners();
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @NotNull
    public CriteriaManager getCriteriaManager() {
        return criteriaManager;
    }

    @NotNull
    @Override
    public CommunityGoalsDatabaseManager getDatabaseManager() {
        return (CommunityGoalsDatabaseManager) super.getDatabaseManager();
    }

    @NotNull
    public static CommunityGoals getInstance() {
        return (CommunityGoals) CorePlugin.getInstance();
    }
}
