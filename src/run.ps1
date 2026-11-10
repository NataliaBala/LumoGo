$projectDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $projectDir
Write-Output "Building backend..."
$wrapperJar = Join-Path $projectDir ".mvn\wrapper\maven-wrapper.jar"
$javaArgs = @(
    "-cp", $wrapperJar,
    "-Dmaven.multiModuleProjectDirectory=$projectDir",
    "org.apache.maven.wrapper.MavenWrapperMain",
    "clean",
    "package",
    "-DskipTests",
    "spring-boot:repackage"
)
& java @javaArgs
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
Write-Output "Starting backend..."
java -jar "target\lumogo-backend-0.0.1-SNAPSHOT.jar"
