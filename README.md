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

    mvn clean install

Usage
-----

Launch the server:

    java -jar target/noterunner-server-0.0.1-SNAPSHOT.jar

To launch the server with more verbosity, use the `--verbose` option:

    java -jar target/noterunner-server-0.0.1-SNAPSHOT.jar --verbose

Commands
--------

* `help`: Display list of available command.
* `add`: Test command.
* `list`: Test command.
* `dump`: Test command.
* `stop`: Stop the server.
