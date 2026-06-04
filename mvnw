#!/usr/bin/env sh

MVNW_PROJECTBASEDIR="$(cd "$(dirname "$0")" && pwd)"

if [ -n "$JAVA_HOME" ]; then
  JAVA_EXE="$JAVA_HOME/bin/java"
else
  JAVA_EXE="java"
fi

exec "$JAVA_EXE" -cp "$MVNW_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar" -Dmaven.multiModuleProjectDirectory="$MVNW_PROJECTBASEDIR" org.apache.maven.wrapper.MavenWrapperMain "$@"
