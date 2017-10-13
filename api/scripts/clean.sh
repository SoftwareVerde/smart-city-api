#!/bin/bash

SCRIPTS_DIR="$(dirname $0)"
cd $SCRIPTS_DIR/..

./gradlew clean

rm -rf out 2>/dev/null

