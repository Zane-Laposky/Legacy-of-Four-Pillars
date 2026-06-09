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
 * This class is responsible for creating the save folder, connecting to the
 * SQLite database, and making sure the player save table exists.
 * <p>
 * This is part of the Data section of the project.
 *
 * @author Ryan Nguyen
 */
public class Database {
    /**
     * The folder where the save database file will be stored.
     */
    private static final String SAVE_FOLDER = "saves";
    
    /**
     * The SQLite database URL.
     * <p>
     * This tells Java where the SQLite database file is located.
     */
    private static final String DB_URL = "jdbc:sqlite:" + SAVE_FOLDER + "/legacy_save.db";


    /**
     * Opens a connection to the SQLite save database.
     * <p>
     * Before opening the connection, this method makes sure the save folder
     * exists and that the SQLite JDBC driver is available.
     *
     * @return database connection
     * @throws SQLException if the save folder cannot be created, the SQLite
     *                      driver is missing, or the database cannot be opened
     */
    public static Connection getConnection() throws SQLException {

        // Make sure the saves folder exists before trying to create/open the database.
        try {
            Files.createDirectories(Path.of(SAVE_FOLDER));
        } catch (Exception e) {
            throw new SQLException("Could not create save folder.", e);
        }
        
        // Load the SQLite JDBC driver so Java can communicate with the SQLite database.
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver is missing. Add sqlite-jdbc.jar to the project libraries.", e);
        }

        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Creates the player_save table if it does not already exist.
     * <p>
     * This table stores the player's saved game data, including hero stats,
     * current room position, inventory items, and the last updated time.
     */
    public static void createTables() {

        // SQL command that creates the save table if it does not already exist.
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
        /*
         * Try-with-resources automatically closes the database connection
         * and statement after the code finishes running.
         */
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

            /*
             * This attempts to add the inventory_items column.
             *
             * If the column already exists, SQLite throws an SQLException.
             * Since that is not a serious problem here, the exception is ignored.
             */
            try {
                stmt.execute("ALTER TABLE player_save ADD COLUMN inventory_items TEXT DEFAULT '';");
            } catch (SQLException ignored) {

            }

        } catch (SQLException e) {
            System.out.println("Database setup failed: " + e.getMessage());
        }
    }
}
