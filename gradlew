#!/bin/sh
echo "Gradle wrapper placeholder - if this fails, Actions will create a wrapper and run gradle"

if [ -f ./gradle/wrapper/gradle-wrapper.jar ]; then
    java -jar ./gradle/wrapper/gradle-wrapper.jar "$@"
else
    echo "No gradle wrapper jar present; use system gradle to run commands instead."
    gradle "$@"
fi
