#!/bin/bash

SCRIPTS_DIR="$(dirname $0)"
cd $SCRIPTS_DIR/..

./gradlew test

