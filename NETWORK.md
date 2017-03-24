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

## Response

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
* A `message` WILL be supplied to provide a reason, that can be displayed to the end user.

```json
{"response":"kick","payload":{"message":"reason"}}
```
