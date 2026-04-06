-keep public class lazy.dev.lazyChat.LazyChat {
    public <init>();
}
-keepclassmembers class * {
    @org.bukkit.event.EventHandler <methods>;
}

# Сохраняем события
-keep class org.bukkit.event.** { *; }
-keep class org.bukkit.event.EventHandler { *; }

# Сохраняем команды
-keep class org.bukkit.command.** { *; }

# Сохраняем Entity, Player и другие часто используемые классы
-keep class org.bukkit.entity.Player { *; }
-keep class org.bukkit.entity.Player$* { *; }

# Сохраняем конфигурацию
-keep class org.bukkit.configuration.file.YamlConfiguration { *; }
-keep class org.bukkit.configuration.file.FileConfiguration { *; }

# Сохраняем утилиты
-keep class org.bukkit.Bukkit { *; }

-keep class * extends org.bukkit.command.CommandExecutor
-keep class * extends org.bukkit.command.TabCompleter
-keep class * extends org.bukkit.plugin.java.JavaPlugin

# Если используете какие-то API, например, для команд
-keep class net.luckperms.api.** { *; }
-keep class net.luckperms.api.** { public protected *; }

# Сохраняем внутренние классы, которые использует плагин для получения экземпляра
-keep class net.luckperms.api.LuckPermsProvider { *; }
-keep class net.luckperms.api.LuckPerms { *; }

# Сохраняем метаданные, префиксы, суффиксы
-keep class net.luckperms.api.cacheddata.CachedMetaData { *; }
-keep class net.luckperms.api.platform.PlayerAdapter { *; }

# Сохраняем все классы из пакета model
-keep class net.luckperms.api.model.** { *; }

# Сохраняем всё API Adventure (MiniMessage, Component, Placeholder)
-keep class net.kyori.adventure.** { *; }
-keep class net.kyori.adventure.text.** { *; }
-keep class net.kyori.adventure.text.minimessage.** { *; }
-keep class net.kyori.adventure.text.minimessage.tag.resolver.Placeholder { *; }

# Сохраняем текстовые сериализаторы
-keep class net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer { *; }

# Сохраняем все реализации Component
-keep class net.kyori.adventure.text.Component { *; }
-keep class net.kyori.adventure.text.TextComponent { *; }