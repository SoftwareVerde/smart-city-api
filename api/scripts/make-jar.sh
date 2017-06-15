#!/bin/bash

rm -rf out/bin 2>/dev/null
mkdir -p out/bin

./gradlew makeJar && cp $(ls -tr build/libs/*.jar | tail -1) out/bin/main.jar && chmod 770 out/bin/main.jar

