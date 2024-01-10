package com.diamonddagger590.communitygoals.database.table;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.database.CommunityGoalsDatabaseManager;
import com.diamonddagger590.mccore.database.table.impl.TableVersionHistoryDAO;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class CGPlayerDAO {

    static final String TABLE_NAME = "goal_player_data";
    private static final int CURRENT_TABLE_VERSION = 1;

    /**
     * Attempts to create a new table for this DAO provided that the table does not already exist.
     *
     * @param connection      The {@link Connection} to use to attempt the creation
     * @param databaseManager The {@link CommunityGoalsDatabaseManager} being used to attempt to create the table
     * @return A {@link CompletableFuture} containing a {@link Boolean} that is {@code true} if a new table was made,
     * or {@code false} otherwise.
     */
    @NotNull
    public static CompletableFuture<Boolean> attemptCreateTable(@NotNull Connection connection, @NotNull CommunityGoalsDatabaseManager databaseManager) {

        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        databaseManager.getDatabaseExecutorService().submit(() -> {

            //Check to see if the table already exists
            if (databaseManager.getDatabase().tableExists(TABLE_NAME)) {
                completableFuture.complete(false);
                return;
            }

            /*****
             ** Table Description:
             ** Contains data about a goal
             *
             *
             * uuid is the {@link java.util.UUID} of the goal being stored
             **
             ** Reasoning for structure:
             ** PK is the `uuid` field, as each goal only has one uuid
             *****/
            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE `" + TABLE_NAME + "`" +
                    "(" +
                    "`player_uuid` varchar(36) NOT NULL," +
                    "`goal_uuid` varchar(36) NOT NULL," +
                    "`contribution` int(11) NOT NULL DEFAULT 0," +
                    "PRIMARY KEY (`player_uuid`, `goal_uuid`)," +
                    "CONSTRAINT fk_goal_uuid FOREIGN KEY (`goal_uuid`) REFERENCES " + GoalDAO.TABLE_NAME + " (goal_uuid)" +
                    ");")) {
                statement.executeUpdate();
            }
            catch (SQLException e) {
                e.printStackTrace();
                completableFuture.completeExceptionally(e);
            }


            completableFuture.complete(true);
        });

        return completableFuture;
    }

    /**
     * Checks to see if there are any version differences from the live version of this SQL table and then current version.
     * <p>
     * If there are any differences, it will iteratively go through and update through each version to ensure the database is
     * safe to run queries on.
     *
     * @param connection The {@link Connection} that will be used to run the changes
     * @return The {@link  CompletableFuture} that is running these changes.
     */
    @NotNull
    public static CompletableFuture<Void> updateTable(@NotNull Connection connection) {

        CommunityGoalsDatabaseManager databaseManager = CommunityGoals.getInstance().getDatabaseManager();
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        databaseManager.getDatabaseExecutorService().submit(() -> {

            TableVersionHistoryDAO.getLatestVersion(connection, TABLE_NAME).thenAccept(lastStoredVersion -> {

                if (lastStoredVersion >= CURRENT_TABLE_VERSION) {
                    completableFuture.complete(null);
                    return;
                }

                //Adds table to our tracking
                if (lastStoredVersion == 0) {
                    TableVersionHistoryDAO.setTableVersion(connection, TABLE_NAME, 1);
                    lastStoredVersion = 1;
                }

            });

            completableFuture.complete(null);
        });

        return completableFuture;
    }
}
