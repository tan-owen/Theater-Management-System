@echo off
setlocal

:: --- Configuration ---
set SRC_ROOT=%~dp0
set BUILD_DIR=%SRC_ROOT%build

:: --- Compile ---

dir /s /b "%SRC_ROOT%*.java" > "%TEMP%\sources.txt" 2>nul
javac -d "%BUILD_DIR%" @"%TEMP%\sources.txt"
if errorlevel 1 (

    del "%TEMP%\sources.txt" 2>nul
    pause
    exit /b 1
)
del "%TEMP%\sources.txt" 2>nul

:: --- Run ---
cd /d "%SRC_ROOT%"
java -cp "%BUILD_DIR%" main %*

endlocal
