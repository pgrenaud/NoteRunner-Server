# Network

## Request

* You MUST send a `type` and a `payload` with each request, even if the `payload` is empty.

### Register

* You MUST send this request once you have establish a connection.
* You WILL receive a `registered` response if a player slot has been granted to you.
* You WILL receive a `kick` response if no player slot are available.

```json
{"type":"register","payload":{}}
```

### Unregister

* You MAY send this request if you wish to free your player slot.
* You WILL receive a `unregistered` response in return.
* You MAY also simply close the connection.

```json
{"type":"unregister","payload":{}}
```

## Response

### Registered

* You WILL receive this response if a player slot has been granted to you.

```json
{"type":"registered","payload":{}}
```

### Unregistered

* You WILL receive this response if you ask to free your player slot.

```json
{"type":"unregistered","payload":{}}
```

### Kick

* You WILL receive this response before the connection is closed.
* Various reason MAY cause this response to be sent to you.
* A `message` WILL be supplied to provide a reason, that can be displayed to the end user.

```json
{"type":"kick","payload":{"message":"reason"}}
```
