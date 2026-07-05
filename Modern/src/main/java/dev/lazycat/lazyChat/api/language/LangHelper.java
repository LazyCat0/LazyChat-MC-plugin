package dev.lazycat.lazyChat.api.language;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;

public class LangHelper {
    private final LC_MiniMessageTranslator translator;

    public LangHelper(LC_MiniMessageTranslator translator) {
        this.translator = translator;
    }
    public Component translateWithPlaceholders(@NotNull Player player,
                                               @NotNull String key,
                                               @NotNull Map<String, Component> placeholders) {
        Locale locale = player.locale();
        String raw = translator.getMiniMessageString(key, locale);
        if (raw == null) {
            raw = key;
        }

        Component result = MiniMessage.miniMessage().deserialize(raw);

        for (Map.Entry<String, Component> entry : placeholders.entrySet()) {
            String placeholder = "<" + entry.getKey() + ">";
            result = result.replaceText(builder -> builder
                    .matchLiteral(placeholder)
                    .replacement(entry.getValue())
            );
        }

        return result;
    }
    public Component translate(@NotNull Player player, @NotNull String key) {
        return translateWithPlaceholders(player, key, Map.of());
    }
}
