package dev.lazycat.lazyChat;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class Checker {
    private final JavaPlugin plugin;
    private static final String MODRINTH_SLUG = "lazychat";

    public Checker(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void getLatestVersion(Consumer<String> consumer) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL("https://modrinth.com" + MODRINTH_SLUG + "/version?loaders=[\"paper\"]");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                String currentVersion = plugin.getPluginMeta().getVersion();
                connection.setRequestProperty("User-Agent", "LazyCat0/LazyChat/" + currentVersion + " (LazyCat.de@proton.me)");

                if (connection.getResponseCode() == 200) {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

                    if (!jsonArray.isEmpty()) {
                        String latestVersion = jsonArray.get(0).getAsJsonObject().get("version_number").getAsString();
                        consumer.accept(latestVersion);
                    }
                    reader.close();
                } else {
                    plugin.getLogger().warning("Cannot check for updates. HTTP Code: " + connection.getResponseCode());
                }
                connection.disconnect();
            } catch (Exception e) {
                plugin.getLogger().warning("Cannot check for updates: " + e.getMessage());
            }
        });
    }
}
