package controller;

import model.Dungeon;
import model.Hero;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Saves and loads player and dungeon data.
 *
 * This class is meant to be called by the controller or GUI.
 */
public class Persistence {

    private static final int MAIN_SAVE_ID = 1;

    private static final String SAVE_FOLDER = "saves";
    private static final String DUNGEON_SAVE_FILE = SAVE_FOLDER + "/dungeon_save.dat";

    /**
     * Saves the current hero to the SQLite database.
     *
     * @param theHero hero to save
     */
    public void savePlayer(final Hero theHero) {
        if (theHero == null) {
            System.out.println("No hero to save.");
            return;
        }

        Database.createTables();

        final PlayerWrapper saveData = PlayerWrapper.fromHero(theHero);

        final String sql = """
                INSERT OR REPLACE INTO player_save
                (id, player_name, hero_type, hit_points, min_damage, max_damage,
                 attack_speed, chance_to_hit, chance_to_block, room_x, room_y,
                 inventory_items, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP);
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, MAIN_SAVE_ID);
            stmt.setString(2, saveData.getPlayerName());
            stmt.setString(3, saveData.getHeroType());
            stmt.setInt(4, saveData.getHitPoints());
            stmt.setInt(5, saveData.getMinDamage());
            stmt.setInt(6, saveData.getMaxDamage());
            stmt.setInt(7, saveData.getAttackSpeed());
            stmt.setDouble(8, saveData.getChanceToHit());
            stmt.setDouble(9, saveData.getChanceToBlock());
            stmt.setInt(10, saveData.getRoomX());
            stmt.setInt(11, saveData.getRoomY());
            stmt.setString(12, saveData.getInventoryItems());

            stmt.executeUpdate();
            System.out.println("Game saved for " + saveData.getPlayerName() + ".");

        } catch (SQLException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    /**
     * Loads the saved player data from the SQLite database.
     *
     * @return Optional containing loaded save data, or empty if no save exists
     */
    public Optional<PlayerWrapper> loadPlayerData() {
        Database.createTables();

        final String sql = """
                SELECT player_name, hero_type, hit_points, min_damage, max_damage,
                       attack_speed, chance_to_hit, chance_to_block, room_x, room_y,
                       inventory_items
                FROM player_save
                WHERE id = ?;
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, MAIN_SAVE_ID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PlayerWrapper saveData = new PlayerWrapper(
                            rs.getString("player_name"),
                            rs.getString("hero_type"),
                            rs.getInt("hit_points"),
                            rs.getInt("min_damage"),
                            rs.getInt("max_damage"),
                            rs.getInt("attack_speed"),
                            rs.getDouble("chance_to_hit"),
                            rs.getDouble("chance_to_block"),
                            rs.getInt("room_x"),
                            rs.getInt("room_y"),
                            rs.getString("inventory_items")
                    );

                    return Optional.of(saveData);
                }
            }

        } catch (SQLException e) {
            System.out.println("Load failed: " + e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Loads the saved player as a Hero object.
     *
     * @return Optional containing the rebuilt hero, or empty if no save exists
     */
    public Optional<Hero> loadPlayer() {
        Optional<PlayerWrapper> saveData = loadPlayerData();

        if (saveData.isPresent()) {
            return Optional.of(saveData.get().toHero());
        }

        return Optional.empty();
    }

    /**
     * Deletes the current save file entry from the database.
     */
    public void deleteSave() {
        Database.createTables();

        final String sql = "DELETE FROM player_save WHERE id = ?;";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, MAIN_SAVE_ID);
            stmt.executeUpdate();
            System.out.println("Save deleted.");

        } catch (SQLException e) {
            System.out.println("Delete save failed: " + e.getMessage());
        }
    }

    /**
     * Saves the current dungeon object.
     *
     * @param theDungeon the dungeon to save
     */
    public void saveDungeon(final Dungeon theDungeon) {
        if (theDungeon == null) {
            System.out.println("No dungeon to save.");
            return;
        }

        new File(SAVE_FOLDER).mkdirs();

        try (ObjectOutputStream output =
                     new ObjectOutputStream(new FileOutputStream(DUNGEON_SAVE_FILE))) {

            output.writeObject(theDungeon);
            System.out.println("Dungeon saved.");

        } catch (Exception e) {
            System.out.println("Dungeon save failed: " + e.getMessage());
        }
    }

    /**
     * Loads the saved dungeon object.
     *
     * @return the saved dungeon, or empty if it cannot be loaded
     */
    public Optional<Dungeon> loadDungeon() {
        File saveFile = new File(DUNGEON_SAVE_FILE);

        if (!saveFile.exists()) {
            return Optional.empty();
        }

        try (ObjectInputStream input =
                     new ObjectInputStream(new FileInputStream(saveFile))) {

            Dungeon loadedDungeon = (Dungeon) input.readObject();
            return Optional.of(loadedDungeon);

        } catch (Exception e) {
            System.out.println("Dungeon load failed: " + e.getMessage());
            return Optional.empty();
        }
    }
}
