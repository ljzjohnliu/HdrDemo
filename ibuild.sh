#!/bin/bash

./gradlew :app:clean
./gradlew :app:build

adb install -r app/build/outputs/apk/debug/app-debug.apk