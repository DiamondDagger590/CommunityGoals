package com.diamonddagger590.communitygoals.database.table;

import com.diamonddagger590.communitygoals.CommunityGoals;
import com.diamonddagger590.communitygoals.database.CommunityGoalsDatabaseManager;
import com.diamonddagger590.communitygoals.exception.IllegalGoalConfigIdException;
import com.diamonddagger590.communitygoals.goal.Goal;
import com.diamonddagger590.mccore.database.table.impl.TableVersionHistoryDAO;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GoalDAO {

    static final String TABLE_NAME = "goal_data";
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
                    "`goal_id` int(11) NOT NULL," +
                    "`goal_name` varchar(32) NOT NULL," +
                    "`goal_criteria_name` varchar(32) NOT NULL," +
                    "`current_contribution` int(11) NOT NULL DEFAULT 0," +
                    "`start_time` int(11) NOT NULL DEFAULT 0," +
                    "`end_time` int(11) NOT NULL DEFAULT 0," +
                    "PRIMARY KEY (`goal_id`)" +
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

    @NotNull
    public static CompletableFuture<Integer> getLastUsedGoalId(@NotNull Connection connection) {
        CommunityGoalsDatabaseManager databaseManager = CommunityGoals.getInstance().getDatabaseManager();
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();

        databaseManager.getDatabaseExecutorService().submit(() -> {

            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(goal_id) FROM " + TABLE_NAME + ";")){
                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    int result = -1;
                    while (resultSet.next()) {
                        result = resultSet.getInt(1);
                    }

                    completableFuture.complete(result);
                }
            }
            catch (SQLException e) {
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }

    @NotNull
    public static CompletableFuture<List<Goal>> getAllUnfinishedGoals(@NotNull Connection connection) {
        CommunityGoalsDatabaseManager databaseManager = CommunityGoals.getInstance().getDatabaseManager();
        CompletableFuture<List<Goal>> completableFuture = new CompletableFuture<>();

        databaseManager.getDatabaseExecutorService().submit(() -> {

            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE end_time = -1")) {

                List<Goal> goals = new ArrayList<>();

                try (ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        try {
                            Goal goal = new Goal(resultSet.getInt("goal_id"), resultSet.getString("goal_name"), resultSet.getString("goal_criteria_name"),
                                    resultSet.getInt("current_contribution"), resultSet.getLong("start_time"), resultSet.getLong("end_time"));
                            goals.add(goal);
                        }
                        catch (IllegalGoalConfigIdException e) {
                            completableFuture.completeExceptionally(e);
                        }
                    }
                }
                completableFuture.complete(goals);
            }
            catch (SQLException e) {
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }

    @NotNull
    public static CompletableFuture<Void> saveGoal(@NotNull Connection connection, @NotNull Goal goal) {
        CommunityGoalsDatabaseManager databaseManager = CommunityGoals.getInstance().getDatabaseManager();
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        databaseManager.getDatabaseExecutorService().submit(() -> {

            try (PreparedStatement preparedStatement = connection.prepareStatement("REPLACE INTO " + TABLE_NAME + " (goal_id, goal_name, goal_criteria_name, " +
                    "current_contribution, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?)")) {

                preparedStatement.setInt(1, goal.getId());
                preparedStatement.setString(2, goal.getName());
                preparedStatement.setString(3, goal.getCriteriaConfigId());
                preparedStatement.setInt(4, goal.getCurrentContribution());
                preparedStatement.setLong(5, goal.getStartTime());
                preparedStatement.setLong(6, goal.getEndTime());

                preparedStatement.executeUpdate();
                completableFuture.complete(null);
            }
            catch (SQLException e) {
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }
}
