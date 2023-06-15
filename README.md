# Hardcore+ Plugin

The Hardcore Plugin is a custom Minecraft server plugin that adds hardcore gameplay elements to your server. It provides features such as player revival, banning on death, and configuration customization.

## Features

- Revive players from spectator mode or unban them
- Ban players on death
- Configurable ban length and messages
- Permissions support for fine-grained control
- Configuration reload

## Requirements

- Java 11, 16, or 17
- Minecraft Server (Spigot or Paper) 1.16 or higher

## Installation

1. Download the latest release of the Hardcore Plugin from the [Releases](https://github.com/sea12314/Hardcore/releases/) page.
2. Copy the downloaded plugin JAR file to the `plugins` folder of your Minecraft server.
3. Restart or reload your Minecraft server.
4. Configure the plugin by editing the `config.yml` file located in the `plugins/Hardcore` folder.

## Usage

- Revive a player: `/revive <player>`
- Reload the configuration (Operator only): `/hardcore reload`

## Configuration

The `config.yml` file allows you to customize various aspects of the plugin, including ban length, messages, and more. Refer to the comments in the configuration file for detailed explanations of each option.

## Permissions

- `hardcore.revive` - Allows players to use the revive command.
- `hardcore.reload` - Allows players to use the reload command.
- `hardcore.lives` - Allows players to use the lives command.

## Contributing

Contributions to the Hardcore Plugin are welcome! If you find any bugs or have suggestions for new features, please create an issue on the [Issue Tracker](https://github.com/sea12314/Hardcore/issues) or submit a pull request with your changes.


