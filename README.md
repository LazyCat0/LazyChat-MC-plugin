# LazyChat
This is just another lazy-plugin which can made your chat experience better
## What this plugin adds?
Only fully configurable chat-system with formating through MiniMessage

    # Local chat radius
    local-chat-radius: 100
    
    # Prefix for global chat
    global-chat-prefix: "!"
    
    # Example for global message. {player} - player-nickname, {message} - message
    global-chat-format: "<dark_gray>[<red>G</red>]</dark_gray> <gold>{player}</gold> <gray>>>></gray> <white>{message}</white>"
    
    # Example for local message. {player} - player-nickname, {message} - message
    local-chat-format: "<dark_gray>[<green>L</green>]</dark_gray> <gold>{player}</gold> <gray>>>></gray> <white>{message}</white>"
    
    # log messages to console (by default on true)
    enable-console-logging: true

# Do it support LuckyPerms prefixes?
Nope. You need to use /team prefixes, but it will be supported in future

# What is "MiniMessage"?
This is a formating api which provided from Adventure api (Or just Paper feature)

# How to build it?

### In Inteljl IDEA
`click at maven icon, after on "lifecycle" and on "package"`
### In CLI
`just go to project folder and open it with your command-line, after type "mvn package"`


###### BY LAZYCAT
