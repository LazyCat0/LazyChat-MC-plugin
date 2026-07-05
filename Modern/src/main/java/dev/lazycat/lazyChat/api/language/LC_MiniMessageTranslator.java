package dev.lazycat.lazyChat.api.language;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.Locale;

public class LC_MiniMessageTranslator extends MiniMessageTranslator {
    private final LanguageManager languageManager;

    public LC_MiniMessageTranslator(LanguageManager languageManager) {
        super(MiniMessage.miniMessage());
        this.languageManager = languageManager;
    }

    @Override
    public @Nullable String getMiniMessageString(@NotNull String key, @NotNull Locale locale) {
        String translation = languageManager.getTranslation(locale, key);
        if (translation != null) return translation;

        Locale parent = new Locale(locale.getLanguage());
        if (!parent.equals(locale)) {
            translation = languageManager.getTranslation(parent, key);
            return translation;
        }

        return null;
    }

    @Override
    public @NonNull Key name() {
        return Key.key("lazychat", "translator");
    }
}
