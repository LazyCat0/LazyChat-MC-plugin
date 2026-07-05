package dev.lazycat.lazyChat.api.language;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class LanguageManager {
    private final Map<Locale, Map<String, String>> translations = new HashMap<>();
    private final Path langFolder;

    public LanguageManager(Path dataFolder) {
        this.langFolder = dataFolder.resolve("lang");
        loadAll();
    }

    private void loadAll() {
        if (!Files.exists(langFolder)) return;
        try (Stream<Path> files = Files.list(langFolder)) {
            files.filter(p -> p.toString().endsWith(".json")).forEach(this::loadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFile(Path file) {
        String fileName = file.getFileName().toString().replace(".json", "");
        Locale locale = Locale.forLanguageTag(fileName.replace("_", "-"));
        try (Reader reader = Files.newBufferedReader(file)) {
            JsonElement root = JsonParser.parseReader(reader);
            Map<String, String> flatMap = new HashMap<>();
            flattenJson(root, "", flatMap);
            translations.put(locale, flatMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void flattenJson(JsonElement element, String currentPath, Map<String, String> out) {
        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                String newPath = currentPath.isEmpty() ? entry.getKey() : currentPath + "." + entry.getKey();
                flattenJson(entry.getValue(), newPath, out);
            }
        } else if (element.isJsonPrimitive()) {
            out.put(currentPath, element.getAsString());
        } else if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            List<String> parts = new ArrayList<>();
            for (JsonElement elem : array) {
                if (elem.isJsonPrimitive()) parts.add(elem.getAsString());
            }
            out.put(currentPath, String.join("\n", parts));
        }
    }

    public String getTranslation(Locale locale, String key) {
        Map<String, String> localeMap = translations.get(locale);
        if (localeMap != null && localeMap.containsKey(key)) {
            return localeMap.get(key);
        }
        return null;
    }
}
