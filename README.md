BananoChests
==========

Repo for BananoChests, a storage security bukkit plugin for Minecraft.

## Features

BananoChests enables players to lock and unlock chests, barrels and shulker
boxes when placed in the game. This prevents unwanted access to the storage 
and helps to keep the player's items secure and away from thieving hands.

This plugin is a free software licenced under the GNU GENERAL PUBLIC LICENSE
Version 3.


## Quick guide

- For players, it is very easy: just place the storage item and toggle the locked 
state by sneaking and right-clicking the storage.
- A command is also provided that allows management of a targeted
storage block via the chat window.
- Players can also set a default autolock mode that overrides the server 
default for that player.
- No additional key items are required!

## Commands and Permissions

### `/securechest`

Displays the current plugin configuration options.

- Arguments: [lock|unlock|share|unshare|status|setautolock]
- Providing a valid argument will only display that section of the configuration.
- Omitting any arguments will display the whole configuration.
- Alias[es]: `/sc, /chest, /barrel, /shulker`
- Permission: `securechest`

Example command calls:
`/sc lock`
`/sc unlock`
`/sc share [player1] [player2] [etc]`
`/sc unshare @all`
`/sc unshare [player1] [player2] [etc]`
`/sc setautolock [chest|barrel|shulker] [status|autolock|none]`

### `/reloadbananochestsconfig`

Displays the current plugin configuration options.

- Used to reload the configuration at runtime.
- Alias[es]: `/bcreloadcfg`
- Permission: `reloadconfig`

### About the permissions

All permissions are by default granted to OP and any players with the appropriate permission
set in the permissions file.

## Configuration

```yaml
# LockMode can be one of two values: Viewable or Sealed
LockMode: Sealed

# Define whether storage should be automatically set to Locked when placed down.
AutoLockShulker: false
AutoLockChest: false
AutoLockBarrel: false
```

## Changelog

### v1.0 — First Release

- Core functionality where a player can lock and unlock their storage.

### v1.1 - Next release

- Add the ability to share access to locked storage with a specific set of players chosen
by the storage owner.