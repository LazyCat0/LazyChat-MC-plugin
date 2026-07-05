package dev.lazycat.lazyChat.api.language;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
        Locale locale = Locale.forLanguageTag(fileName.replace("_", "-")); // en_us → en-US
        try (Reader reader = Files.newBufferedReader(file)) {
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> map = new Gson().fromJson(reader, type);
            translations.put(locale, map);
        } catch (IOException e) {

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
