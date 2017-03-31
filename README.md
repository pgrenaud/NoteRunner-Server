NoteRunner-Server
=================

[![Travis](https://img.shields.io/travis/pgrenaud/NoteRunner-Server.svg)](https://travis-ci.org/pgrenaud/NoteRunner-Server)
[![Release](https://img.shields.io/github/release/pgrenaud/NoteRunner-Server.svg)](https://github.com/pgrenaud/NoteRunner-Server/releases)
[![MIT License](https://img.shields.io/badge/license-MIT-8469ad.svg)](https://tldrlegal.com/license/mit-license)

Server for NoteRunner, the video game.

Requirements
------------

* Java SE Development Kit 8 (JDK8) or newer installed
* [Maven 3](https://maven.apache.org/download.cgi) installed

Compilation
-----------

Use Maven to build all the dependencies and the runnable jar files:

    mvn clean package

Usage
-----

Launch the server:

    java -jar target/noterunner-server-1.1.0.jar

To launch the server with more verbosity, use the `--verbose` option:

    java -jar target/noterunner-server-1.1.0.jar --verbose

Commands
--------

An interactive prompt is available once the server is started. Here's the available commands:

* `allready`: Force all players to be ready.
* `dump`: Test command.
* `help`: Display list of available command.
* `lobby`: Abort any round and return to lobby.
* `kick`: Kick a player.
* `status`: Display the current game and player status.
* `stop`: Stop the server.

Documentation
-------------

See the [network file](NETWORK.md) learn about the network protocol used to communicate.
