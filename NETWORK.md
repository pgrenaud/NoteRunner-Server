# Network

## Request

### Damage

* You MAY send this request to remove one health point from your player.
* You CANNOT send this request when no round is running.

```json
{"request":"damage"}
```

### Finish

* You MAY send this request to let others know that you finished the round.
* You CANNOT send this request when no round is running.

```json
{"request":"finish"}
```

### Register

* You MUST send this request once you have establish a connection.
* You WILL receive a `registered` response if a player slot has been granted to you.
* You WILL receive a `kick` response if no player slot are available.

```json
{"request":"register"}
```

### Set ready

* You MUST send this request if you wish to start the match.
* You MAY send this request if you no longer wish to start the match.
* You MUST supply a `ready` boolean to tell if you are ready or not.
* You CANNOT send this request when you are not in the lobby or preparing for a round.

```json
{"request":"set_ready","payload":{"ready":true}}
```

### Unregister

* You MAY send this request if you wish to free your player slot.
* You WILL receive a `unregistered` response in return.
* You MAY also simply close the connection.

```json
{"request":"unregister"}
```

### Update config

* You MAY send this request if you wish to update the match settings.
* YOU WILL either receive a `config_updated` or a `config_rejected` response in return.
* You MUST supply a `config` object in the payload that respect the following rules:
  * You MUST define a integer `sequence_length` with a value between 4 and 15.
  * You MUST define a integer `player_health` with a value between 1 and 20.
  * You MUST define a list `notes_enabled` with at least 3 **unique** elements from A, B, C, D, E, F and G.
* You CANNOT send this request when you are not in the lobby.

```json
{"request":"update_config","payload":{"config":{"sequence_length":3,"player_health":10,"notes_enabled":["A","B","C","D","E","F","G"]}}}
```

## Response

### Config updated

* You WILL receive this response once a player slot has been granted to you.
* You WILL also receive this response when you or the other player update the match settings.
* You WILL be supplied with a `config` object containing the updated match settings.

```json
{"response":"config_updated","payload":{"config":{"sequence_length":3,"player_health":10,"notes_enabled":["A","B","C","D","E","F","G"]}}}
```

### Gameover

* You WILL receive this response when a player dies.
* You WILL be supplied with a `player` integer to tell which player died.

```json
{"response":"gameover","payload":{"player":1}}
```

### Lobby loaded

* You WILL receive this response when the match is over (a player died/gameover).
* This occurs after a round ended.

```json
{"response":"lobby_loaded"}
```

### Player connected

* You WILL receive this response when another player connect to the server.
* You WILL also receive this response when you connect to the server.
* You WILL be supplied with a `player` integer to tell which player connected.

```json
{"response":"player_connected","payload":{"player":1}}
```

### Player disconnected

* You WILL receive this response when another player disconnect from the server.
* You WILL be supplied with a `player` integer to tell which player disconnected.

```json
{"response":"player_disconnected","payload":{"player":1}}
```

### Player health

* You WILL receive this response when another player loose health.
* You WILL be supplied with a `player` integer to tell which player lost health.
* You WILL be supplied with a `health` integer to tell how much health remain.

```json
{"response":"player_health","payload":{"health":1,"player":1}}
```

### Player ready

* You WILL receive this response when a player change his ready state.
* You WILL be supplied with a `player` integer to tell which player change his ready state.
* You WILL be supplied with a `ready` boolean to tell if the player is ready or not.

```json
{"response":"player_ready","payload":{"player":1,"ready":true}}
```

### Registered

* You WILL receive this response if a player slot has been granted to you.
* You WILL be supplied with a `player` integer to provide your player slot id.

```json
{"response":"registered","payload":{"player":2}}
```

### Request rejected

* You WILL receive this response if the previous request you sent is invalid.
* Various reason MAY cause this response to be sent to you.
* You WILL be supplied with a `message` string to provide a reason, that can be displayed to the end user.

```json
{"response":"request_rejected","payload":{"message":"One or more notes are invalid"}}
```

### Round prepared

* You WILL receive this response when a round is being prepared.
* This occurs when all players in the lobby are ready or the previous round ended.
* You WILL be supplied with a `config` object containing the match settings.
* You WILL be supplied with a `note_sequence` list containing the sequence of notes for this round.

```json
{"response":"round_prepared","payload":{"config":{"sequence_length":3,"player_health":10,"notes_enabled":["A","B","C","D","E","F","G"]},"note_sequence":["A","B","C"]}}
```

### Round began

* You WILL receive this response when a round began.
* This occurs when all players are ready.

```json
{"response":"round_began"}
```

### Round ended

* You WILL receive this response when a round ended.
* This occurs when a player finishes or dies (gameover).

```json
{"response":"round_ended"}
```

### Unregistered

* You WILL receive this response if you ask to free your player slot.

```json
{"response":"unregistered"}
```

### Kick

* You WILL receive this response before the connection is closed.
* Various reason MAY cause this response to be sent to you.
* You WILL be supplied with a `message` string to provide a reason, that can be displayed to the end user.

```json
{"response":"kick","payload":{"message":"Server is shutting down"}}
```
