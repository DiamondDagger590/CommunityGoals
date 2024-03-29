package com.diamonddagger590.communitygoals.database;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.database.table.CGPlayerDAO;
import com.diamonddagger590.communitygoals.database.table.GoalDAO;
import com.diamonddagger590.mccore.database.DatabaseManager;
import com.diamonddagger590.mccore.database.builder.Database;
import com.diamonddagger590.mccore.database.builder.DatabaseBuilder;
import com.diamonddagger590.mccore.database.builder.DatabaseDriver;
import com.diamonddagger590.mccore.database.function.DatabaseInitializationFunction;
import com.diamonddagger590.mccore.exception.CoreDatabaseInitializationException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CommunityGoalsDatabaseManager extends DatabaseManager {

    private final DatabaseDriver driver;
    private final DatabaseInitializationFunction databaseInitializationFunction = (driver) -> {

        DatabaseBuilder dbBuilder = new CommunityGoalsDatabaseBuilder(driver);

        File databaseFolder = new File(plugin.getDataFolder().getPath() + File.separator + "database");

        if(!databaseFolder.exists()){
            boolean result = databaseFolder.mkdirs();
        }

        dbBuilder.setPath(databaseFolder.getPath() + File.separator + "communitygoals");

        Optional<Database> initializedDatabase = Optional.empty();

        try {
            initializedDatabase = Optional.of(dbBuilder.build());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return initializedDatabase;
    };

    public CommunityGoalsDatabaseManager(@NotNull CommunityGoals plugin) {
        super(plugin);
        this.driver = DatabaseDriver.SQLITE;

        populateCreateFunctions();
        populateUpdateFunctions();
    }

    /**
     * Adds the required {@link com.diamonddagger590.mccore.database.function.CreateTableFunction CreateTableFunctions} for McRPG to properly run.
     */
    private void populateCreateFunctions() {

        addCreateTableFunction((databaseManager) -> {

            CompletableFuture<Void> tableCreationFuture = new CompletableFuture<>();
            Database database = databaseManager.getDatabase();

            if (database == null) {
                tableCreationFuture.completeExceptionally(new CoreDatabaseInitializationException("The database for CommunityGoals is null, please report this to the plugin developer."));
                return tableCreationFuture;
            }

            Connection connection = databaseManager.getDatabase().getConnection();

            getDatabaseExecutorService().submit(() -> {

                CompletableFuture.allOf(GoalDAO.attemptCreateTable(connection, this),
                                CGPlayerDAO.attemptCreateTable(connection, this))
                        .thenAccept(tableCreationFuture::complete)
                        .exceptionally(throwable -> {
                            tableCreationFuture.completeExceptionally(throwable);
                            return null;
                        });

            });

            return tableCreationFuture;
        });
    }

    /**
     * Adds the required {@link com.diamonddagger590.mccore.database.function.UpdateTableFunction UpdateTableFunctions} for McRPG to properly run.
     */
    private void populateUpdateFunctions() {

        addUpdateTableFunction((databaseManager -> {

            CompletableFuture<Void> tableUpdateFuture = new CompletableFuture<>();
            Database database = databaseManager.getDatabase();

            if (database == null) {
                tableUpdateFuture.completeExceptionally(new CoreDatabaseInitializationException("The database for McRPG is null, please report this to the plugin developer."));
                return tableUpdateFuture;
            }

            Connection connection = databaseManager.getDatabase().getConnection();

            getDatabaseExecutorService().submit(() -> {
                CompletableFuture.allOf(GoalDAO.updateTable(connection), CGPlayerDAO.updateTable(connection))
                        .thenAccept(tableUpdateFuture::complete);
            });

            return tableUpdateFuture;
        }));
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public DatabaseInitializationFunction getDatabaseInitializationFunction() {
        return databaseInitializationFunction;
    }

    @NotNull
    @Override
    public DatabaseDriver getDriver() {
        return this.driver;
    }
}

