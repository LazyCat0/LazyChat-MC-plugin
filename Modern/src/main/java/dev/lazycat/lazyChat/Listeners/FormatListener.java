package dev.lazycat.lazyChat.Listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
//import org.bukkit.event.player.PlayerEditBookEvent;
//import org.bukkit.inventory.meta.BookMeta;
//
//import java.util.ArrayList;
//import java.util.List;

public class FormatListener implements Listener {
    private final MiniMessage mm = MiniMessage.miniMessage();

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("l-chat.advance_format")) {
            return;
        }

        for (int i = 0; i < 4; i++) {
            Component lineComponent = event.line(i);

            if (lineComponent == null) continue;

            String rawText = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(lineComponent);

            if (rawText.isEmpty()) continue;

            if (rawText.contains("<newline>") && !player.hasPermission("l-chat.full-format-use")) {
                continue;
            }

            Component formattedLine = mm.deserialize(rawText);
            event.line(i, formattedLine);
        }
    }
//    @EventHandler
//    public void onBookChange(PlayerEditBookEvent e) {
//        Player player = e.getPlayer();
//        if (!player.hasPermission("l-chat.advance_format")) return;
//
//        BookMeta meta = e.getNewBookMeta();
//
//        List<Component> pages = meta.pages();
//
//        List<Component> formattedPages = new ArrayList<>();
//        for (Component pageComponent : pages) {
//            String rawText = MiniMessage.miniMessage().serialize(pageComponent);
//            Component formatted = MiniMessage.miniMessage().deserialize(rawText);
//            formattedPages.add(formatted);
//        }
//
//        meta.pages(formattedPages);
//        e.setNewBookMeta(meta);
//
//
//    }
}
