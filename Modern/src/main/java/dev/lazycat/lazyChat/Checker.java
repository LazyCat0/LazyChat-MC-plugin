package dev.lazycat.lazyChat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class Checker {
    private final JavaPlugin plugin;

    public Checker(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void getLatestVersion(Consumer<String> consumer) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL("https://api.github.com/repos/LazyCat0/LazyChat-MC-plugin/releases/latest");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Paper-Plugin-UpdateChecker");

                if (connection.getResponseCode() == 200) {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

                    String latestVersion = json.get("tag_name").getAsString();
                    consumer.accept(latestVersion);
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Cannot to check for updates: " + e.getMessage());
            }
        });
    }
}
