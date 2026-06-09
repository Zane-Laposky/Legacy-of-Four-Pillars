package controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the SQLite database connection and table setup for saved game data.
 * <p>
 * This is part of the Data section of the project.
 *
 * @author Ryan Nguyen
 * @version 1.0 Spring 2026
 */
public class Database {
    // Utility class, so no objects need to be made.
    private static final String SAVE_FOLDER = "saves";
    private static final String DB_URL = "jdbc:sqlite:" + SAVE_FOLDER + "/legacy_save.db";


    /**
     * Opens a connection to the SQLite save database.
     *
     * @return database connection
     * @throws SQLException if the database cannot be opened
     */
    public static Connection getConnection() throws SQLException {
        try {
            Files.createDirectories(Path.of(SAVE_FOLDER));
        } catch (Exception e) {
            throw new SQLException("Could not create save folder.", e);
        }

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver is missing. Add sqlite-jdbc.jar to the project libraries.", e);
        }

        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Creates the save table if it does not already exist.
     */
    public static void createTables() {
        final String sql = """
                CREATE TABLE IF NOT EXISTS player_save (
                    id INTEGER PRIMARY KEY,
                    player_name TEXT NOT NULL,
                    hero_type TEXT NOT NULL,
                    hit_points INTEGER NOT NULL,
                    min_damage INTEGER NOT NULL,
                    max_damage INTEGER NOT NULL,
                    attack_speed INTEGER NOT NULL,
                    chance_to_hit REAL NOT NULL,
                    chance_to_block REAL NOT NULL,
                    room_x INTEGER,
                    room_y INTEGER,
                    inventory_items TEXT DEFAULT '',
                    updated_at TEXT DEFAULT CURRENT_TIMESTAMP
                );
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

            try {
                stmt.execute("ALTER TABLE player_save ADD COLUMN inventory_items TEXT DEFAULT '';");
            } catch (SQLException ignored) {

            }

        } catch (SQLException e) {
            System.out.println("Database setup failed: " + e.getMessage());
        }
    }
}
