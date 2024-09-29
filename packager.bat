@echo off

:: Get the current directory
set "BASE_DIR=%~dp0"
set "SRC_DIR=%BASE_DIR%src"
set "OUTPUT_DIR=%BASE_DIR%installer"
set "APP_NAME=BookStoreApp"
set "JAR_NAME=bookStoreApp.jar"
set "MAIN_CLASS=Welcome"

:: Create output directory if it doesn't exist
if not exist "%OUTPUT_DIR%" (
    mkdir "%OUTPUT_DIR%"
)

:: Remove existing .class files
echo Removing existing .class files...
del /Q "%SRC_DIR%\*.class"

:: Compile Java files
echo Compiling Java files...
javac "%SRC_DIR%\Admin.java" "%SRC_DIR%\Book.java" "%SRC_DIR%\BookStoreApp.java" "%SRC_DIR%\CartItem.java" "%SRC_DIR%\Login.java" "%SRC_DIR%\Payment.java" "%SRC_DIR%\SignUp.java" "%SRC_DIR%\Welcome.java" -d "%SRC_DIR%"

:: Check if compilation succeeded
if errorlevel 1 (
    echo Compilation failed. Exiting...
    exit /b 1
)

:: Create JAR file for Welcome (now named bookStoreApp.jar)
echo Creating JAR file for BookStoreApp...
jar cfe "%OUTPUT_DIR%\%JAR_NAME%" "%MAIN_CLASS%" -C "%SRC_DIR%" .

:: Check if JAR creation succeeded
if errorlevel 1 (
    echo JAR creation failed for BookStoreApp. Exiting...
    exit /b 1
)

:: Package into EXE installer for BookStoreApp
echo Creating EXE installer for BookStoreApp...
jpackage --input "%OUTPUT_DIR%" --name "%APP_NAME%" --main-jar "%JAR_NAME%" --main-class "%MAIN_CLASS%" --type exe --dest "%OUTPUT_DIR%"

:: Check if jpackage succeeded
if errorlevel 1 (
    echo EXE creation failed for BookStoreApp. Exiting...
    exit /b 1
)

:: Remove all .class files after creating JAR and EXE
echo Removing .class files...
del /Q "%SRC_DIR%\*.class"

echo Installer created at %OUTPUT_DIR%\%APP_NAME%.exe
