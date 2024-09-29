@echo off
setlocal

:: Get the current directory
set BASE_DIR=%~dp0
set SRC_DIR=%BASE_DIR%src
set OUTPUT_DIR=%BASE_DIR%installer
set APP_NAME=BookStoreApp
set JAR_NAME_BOOKSTORE=%APP_NAME%.jar
set JAR_NAME_ADMIN=Admin.jar
set MAIN_CLASS_BOOKSTORE=BookStoreApp
set MAIN_CLASS_ADMIN=Admin

:: Create output directory if it doesn't exist
if not exist "%OUTPUT_DIR%" (
    mkdir "%OUTPUT_DIR%"
)

:: Remove existing .class files
echo Removing existing .class files...
del /q "%SRC_DIR%\*.class"

:: Compile Java files
echo Compiling Java files...
javac "%SRC_DIR%\Admin.java" "%SRC_DIR%\Book.java" "%SRC_DIR%\BookStoreApp.java" "%SRC_DIR%\CartItem.java" "%SRC_DIR%\Login.java" "%SRC_DIR%\Payment.java" "%SRC_DIR%\SignUp.java" "%SRC_DIR%\Welcome.java" -d "%SRC_DIR%"

:: Check if compilation succeeded
if errorlevel 1 (
    echo Compilation failed. Exiting...
    exit /b
)

:: Create JAR file for BookStoreApp
echo Creating JAR file for BookStoreApp...
jar cfe "%OUTPUT_DIR%\%JAR_NAME_BOOKSTORE%" %MAIN_CLASS_BOOKSTORE% -C "%SRC_DIR%" .

:: Check if JAR creation succeeded
if errorlevel 1 (
    echo JAR creation failed for BookStoreApp. Exiting...
    exit /b
)

:: Create JAR file for Admin
echo Creating JAR file for Admin...
jar cfe "%OUTPUT_DIR%\%JAR_NAME_ADMIN%" %MAIN_CLASS_ADMIN% -C "%SRC_DIR%" .

:: Check if JAR creation succeeded
if errorlevel 1 (
    echo JAR creation failed for Admin. Exiting...
    exit /b
)

:: Package into MSI installer for BookStoreApp
echo Creating MSI installer for BookStoreApp...
jpackage --input "%OUTPUT_DIR%" --name %APP_NAME% --main-jar %JAR_NAME_BOOKSTORE% --main-class %MAIN_CLASS_BOOKSTORE% --type msi --dest "%OUTPUT_DIR%"

:: Check if jpackage succeeded
if errorlevel 1 (
    echo MSI creation failed for BookStoreApp. Exiting...
    exit /b
)

:: Package into MSI installer for Admin
echo Creating MSI installer for Admin...
jpackage --input "%OUTPUT_DIR%" --name Admin --main-jar %JAR_NAME_ADMIN% --main-class %MAIN_CLASS_ADMIN% --type msi --dest "%OUTPUT_DIR%"

:: Check if jpackage succeeded
if errorlevel 1 (
    echo MSI creation failed for Admin. Exiting...
    exit /b
)

:: Remove all .class files after creating JAR and MSI
echo Removing .class files...
del /q "%SRC_DIR%\*.class"

echo Installer created at %OUTPUT_DIR%\%APP_NAME%.msi
echo Installer created at %OUTPUT_DIR%\Admin.msi
endlocal
