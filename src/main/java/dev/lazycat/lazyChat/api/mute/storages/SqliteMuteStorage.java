package dev.lazycat.lazyChat.api.mute.storages;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SqliteMuteStorage implements MuteStorage {
    private final Connection connection;
    private final JavaPlugin plugin;

    public SqliteMuteStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        try {
            Class.forName("org.sqlite.JDBC");
            String dbPath = plugin.getDataFolder().getAbsolutePath() + "/data/mutes.db";
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            createTable();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SQLite storage", e);
        }
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS mutes (" +
                "uuid TEXT PRIMARY KEY, " +
                "expiry INTEGER NOT NULL" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    @Override
    public Map<UUID, Long> loadMutes() {
        Map<UUID, Long> mutes = new HashMap<>();
        String sql = "SELECT uuid, expiry FROM mutes";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                long expiry = rs.getLong("expiry");
                mutes.put(uuid, expiry);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to load mutes from database: " + e.getMessage());
        }
        return mutes;
    }

    @Override
    public void saveMute(UUID uuid, long expiry) {
        String sql = "INSERT OR REPLACE INTO mutes (uuid, expiry) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setLong(2, expiry);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save mute: " + e.getMessage());
        }
    }

    @Override
    public void removeMute(UUID uuid) {
        String sql = "DELETE FROM mutes WHERE uuid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to remove mute: " + e.getMessage());
        }
    }

    @Override
    public boolean isMuted(UUID uuid) {
        Long expiry = getExpiry(uuid);
        return expiry != null && System.currentTimeMillis() <= expiry;
    }

    @Override
    public Long getExpiry(UUID uuid) {
        String sql = "SELECT expiry FROM mutes WHERE uuid = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("expiry");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to get expiry: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error closing database connection: " + e.getMessage());
        }
    }
}

