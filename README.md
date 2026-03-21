# LazyChat | [![](https://jitpack.io/v/LazyCat0/LazyChat-MC-plugin.svg)](https://jitpack.io/#LazyCat0/LazyChat-MC-plugin)

This is just another lazy-plugin which can made your chat experience better

<img src="images_GitHub/img.png" alt="img" width="600" height="52">

## Additional info

**Available [on modrinth](https://modrinth.com/plugin/lazychat)**

**LATEST AT THIS MOMENT PUBLIC VERSION - v2.3**

*Recommended use this plugin with LuckPerms* 

# What's this plugin adds?

Only fully configurable chat-system with formating through MiniMessage
```yml
config version: 2 # DO NOT CHANGE IT

# Plugin language (English by default). Available: English, Russia, Ukrainian
# After changing plugin language, you need restart server.
lang: English
# Local chat radius
local-chat-radius: 100
# Prefix for global chat (By default and most popular - "!")
global-chat-prefix: "!"
# log messages to console (by default on true)
enable-console-logging: true

# Messages examples. Placeholders: {player} - player-nickname, {message} - message, {prefix} - prefix from LuckPerms, {suffix} - suffix from LuckPerms.
# Example for global message.
global-chat-format: "<dark_gray>|<green>G</green>|</dark_gray> {prefix}<reset><gold>{player}</gold>{suffix} <gray>>>><reset> {message}"
# Example for local message.
local-chat-format: "<dark_gray>|<blue>L</blue>|</dark_gray> {prefix}<reset><gold>{player}</gold>{suffix} <gray>>>><reset> {message}"
```


### Do is it support LuckyPerms prefixes?
**Yes!**

**For change prefix color you need use this command - `/lp user <player> meta setprefix "<red>[example_prefix]</red> "`**

### What is "MiniMessage"?
**This is a formating api which provided from Adventure api (Or just Paper feature)**
### How to build it?

#### In Inteljl IDEA
`Just import the configuration at runConfigurations/Package plugin to jar.run.xml and run it`
#### In CLI
`just go to project folder and open it with your command-line, after type "mvn package"`

## For Developers...
**You can use this plugin as depend/softdepend for easier manage chat via your plugin.**
### For maven
* Repository
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
* Depend
```xml
<dependency>
    <groupId>com.github.LazyCat0</groupId>
    <artifactId>LazyChat-MC-plugin</artifactId>
    <version>X.X</version> <!-- Change to actual version -->
</dependency>
```
### For Gradle
#### Groovy
* Repository
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```
* Depend
```groovy
dependencies {
    implementation 'com.github.LazyCat0:LazyChat-MC-plugin:X.X' // Replace with actual version
}
```
#### KTS
* Repository
```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}
```
* Depend
```kotlin
dependencies {
    implementation("com.github.LazyCat0:LazyChat-MC-plugin:Tag")
}
```
