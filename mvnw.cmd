@echo off
setlocal
set MVNW_PROJECTBASEDIR=%~dp0
if defined JAVA_HOME (
    set JAVA_EXE=%JAVA_HOME%\bin\java.exe
) else (
    set JAVA_EXE=java
)
"%JAVA_EXE%" -cp "%MVNW_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar" -Dmaven.multiModuleProjectDirectory="%MVNW_PROJECTBASEDIR:~0,-1%" org.apache.maven.wrapper.MavenWrapperMain %*
endlocal
