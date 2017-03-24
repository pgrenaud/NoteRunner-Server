# Network

## Request

### Register

* You MUST send this request once you have establish a connection.
* You WILL receive a `registered` response if a player slot has been granted to you.
* You WILL receive a `kick` response if no player slot are available.

```json
{"request":"register"}
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
* You MUST supply a `config` in the payload that respect the following rules:
  * You MUST define a integer `sequence_length` with a value between 3 and 15.
  * You MUST define a integer `player_health` with a value between 1 and 20.
  * You MUST define a list `notes_enabled` with at least 3 **unique** elements from A, B, C, D, E, F and G.

```json
{"request":"update_config","payload":{"config":{"sequence_length":3,"player_health":10,"notes_enabled":["A","B","C","D","E","F","G"]}}}
```

## Response

### Config rejected

* You WILL receive this response if one of the match setting you sent is invalid.
* Various reason MAY cause this response to be sent to you.
* You WILL be be supplied with a `message` to provide a reason, that can be displayed to the end user.

```json
{"response":"config_rejected","payload":{"message":"One or more notes are invalid"}}
```

### Config updated

* You WILL receive this response once a player slot has been granted to you.
* You WILL also receive this response when you or the other player update the match settings.

```json
{"response":"config_updated","payload":{"config":{"sequence_length":3,"player_health":10,"notes_enabled":["A","B","C","D","E","F","G"]}}}
```

### Registered

* You WILL receive this response if a player slot has been granted to you.

```json
{"response":"registered"}
```

### Unregistered

* You WILL receive this response if you ask to free your player slot.

```json
{"response":"unregistered"}
```

### Kick

* You WILL receive this response before the connection is closed.
* Various reason MAY cause this response to be sent to you.
* You WILL be be supplied with a `message` to provide a reason, that can be displayed to the end user.

```json
{"response":"kick","payload":{"message":"Server is shutting down"}}
```
