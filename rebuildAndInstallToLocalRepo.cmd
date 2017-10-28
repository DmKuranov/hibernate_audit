call mvn clean package
echo ERRORLEVEL=%ERRORLEVEL%

IF %ERRORLEVEL% EQU 0 (
	mvn install:install-file -Dfile=pom.xml -DpomFile=pom.xml
	mvn install:install-file -Dfile=dependencies/target/dependencies-0.2.jar -DpomFile=dependencies/pom.xml
	mvn install:install-file -Dfile=hibernate_audit/target/hibernate_audit.jar -DpomFile=hibernate_audit/pom.xml
)
