#!/usr/bin/env bash

version="1.0-SNAPSHOT"
jar="banana-notification-service-$version.jar"

nohup java -jar $jar > server.log &