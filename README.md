# LazyChat
This is just another lazy-plugin which can made your chat experience better
![img.png](img.png)
## What this plugin adds?
Only fully configurable chat-system with formating through MiniMessage

    # Local chat radius
    local-chat-radius: 100

    # Prefix for global chat (By default and most popular - "!")
    global-chat-prefix: "!"

    # log messages to console (by default on true)
    enable-console-logging: true

    # Messages examples. Placeholders: {player} - player-nickname, {message} - message, {prefix} - prefix from LuckyPerms
    # Example for global message.
    global-chat-format: "<dark_gray>[<green>G</green>]</dark_gray> {prefix} <gold>{player}</gold> <gray>>>></gray> <white>{message}</white>"
    # Example for local message.
    local-chat-format: "<dark_gray>[<blue>L</blue>]</dark_gray> {prefix} <gold>{player}</gold> <gray>>>></gray> <white>{message}</white>"


# Do it support LuckyPerms prefixes?
Yes, this is support LuckyPerms from v0.3. For format prefix you need use MM formating, like `<gold><bold>prefix</bold></gold>`. Legacy formating doesn't support anyway.

# What is "MiniMessage"?
This is a formating api which provided from Adventure api (Or just Paper feature)

# How to build it?

### In Inteljl IDEA
`click at maven icon, after on "lifecycle" and on "package"`
### In CLI
`just go to project folder and open it with your command-line, after type "mvn package"`


###### BY LAZYCAT
