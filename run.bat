@echo off
setlocal enabledelayedexpansion
cd /d "%~dp0"
set "PROJECT_DIR=%~dp0"
set "PROJECT_DIR=%PROJECT_DIR:~0,-1%"

echo Building backend...
java -cp "%PROJECT_DIR%\.mvn\wrapper\maven-wrapper.jar" -Dmaven.multiModuleProjectDirectory="%PROJECT_DIR%" org.apache.maven.wrapper.MavenWrapperMain clean package -DskipTests spring-boot:repackage
if errorlevel 1 exit /b %errorlevel%

echo Starting backend...
java -jar "%PROJECT_DIR%\target\lumogo-backend-0.0.1-SNAPSHOT.jar"
