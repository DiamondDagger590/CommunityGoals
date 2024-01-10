package com.diamonddagger590.communitygoals.database;

import com.diamonddagger590.mccore.database.builder.DatabaseBuilder;
import com.diamonddagger590.mccore.database.builder.DatabaseDriver;
import org.jetbrains.annotations.NotNull;

public class CommunityGoalsDatabaseBuilder extends DatabaseBuilder {

    public CommunityGoalsDatabaseBuilder(DatabaseDriver databaseDriver) {
        super(databaseDriver);
    }

    @Override
    @NotNull
    public CommunityGoalsDatabaseBuilder setPath(@NotNull String path) {
        return (CommunityGoalsDatabaseBuilder) super.setPath(path);
    }
}
