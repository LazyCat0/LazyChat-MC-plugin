# LazyChat

This is just another lazy-plugin which can made your chat experience better

<img src="images_GitHub/img.png" alt="img" width="600" height="52">

## Additional info

**Available [on modrinth](https://modrinth.com/plugin/lazychat)**
**LATEST AT THIS MOMENT PUBLIC VERSION - v2.2**

*Recommended use this plugin with LuckPerms* 

# What's this plugin adds?

Only fully configurable chat-system with formating through MiniMessage

    # Local chat radius
    local-chat-radius: 100

    # Prefix for global chat (By default and most popular - "!")
    global-chat-prefix: "!"

    # log messages to console (by default on true)
    enable-console-logging: true

    # Messages examples. Placeholders: {player} - player-nickname, {message} - message, {prefix} - prefix from LuckyPerms
    # Example for global message.
    global-chat-format: "<dark_gray>|<green>G</green>|</dark_gray> {prefix}<gold>{player}</gold> <gray>>>></gray> <white>{message}</white>"
    # Example for local message.
    local-chat-format: "<dark_gray>|<blue>L</blue>|</dark_gray> {prefix}<gold>{player}</gold> <gray>>>></gray> <white>{message}</white>"



### Do is it support LuckyPerms prefixes?
**Yes!**

**For change prefix color you need use this command - `/lp user <player> meta setprefix "<red>[example_prefix]</red> "`**

### What is "MiniMessage"?
**This is a formating api which provided from Adventure api (Or just Paper feature)**
### How to build it?

#### In Inteljl IDEA
`click at maven icon, after on "lifecycle" and on "package"`
#### In CLI
`just go to project folder and open it with your command-line, after type "mvn package"`

