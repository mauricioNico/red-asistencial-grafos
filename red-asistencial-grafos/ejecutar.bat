@echo off
cd /d "%~dp0"

call mvn clean package

if errorlevel 1 (
    echo.
    echo Error durante la compilacion o las pruebas.
    pause
    exit /b 1
)

java -jar target/red-asistencial-grafos-dijkstra.jar

pause
