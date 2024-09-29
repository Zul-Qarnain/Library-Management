#!/bin/bash
set -e

# Get the current directory
BASE_DIR="$(dirname "$(realpath "$0")")"
SRC_DIR="$BASE_DIR/src"
OUTPUT_DIR="$BASE_DIR/installer"
APP_NAME="BookStoreApp"
JAR_NAME_BOOKSTORE="$APP_NAME.jar"
JAR_NAME_ADMIN="Admin.jar"
MAIN_CLASS_BOOKSTORE="BookStoreApp"
MAIN_CLASS_ADMIN="Admin"

# Create output directory if it doesn't exist
if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir "$OUTPUT_DIR"
fi

# Remove existing .class files
echo "Removing existing .class files..."
rm -f "$SRC_DIR"/*.class

# Compile Java files
echo "Compiling Java files..."
javac "$SRC_DIR/Admin.java" "$SRC_DIR/Book.java" "$SRC_DIR/BookStoreApp.java" "$SRC_DIR/CartItem.java" "$SRC_DIR/Login.java" "$SRC_DIR/Payment.java" "$SRC_DIR/SignUp.java" "$SRC_DIR/Welcome.java" -d "$SRC_DIR"

# Create JAR file for BookStoreApp
echo "Creating JAR file for BookStoreApp..."
jar cfe "$OUTPUT_DIR/$JAR_NAME_BOOKSTORE" "$MAIN_CLASS_BOOKSTORE" -C "$SRC_DIR" .

# Create JAR file for Admin
echo "Creating JAR file for Admin..."
jar cfe "$OUTPUT_DIR/$JAR_NAME_ADMIN" "$MAIN_CLASS_ADMIN" -C "$SRC_DIR" .

# Package into DEB installer for BookStoreApp
echo "Creating DEB installer for BookStoreApp..."
jpackage --input "$OUTPUT_DIR" --name "$APP_NAME" --main-jar "$JAR_NAME_BOOKSTORE" --main-class "$MAIN_CLASS_BOOKSTORE" --type deb --dest "$OUTPUT_DIR"

# Package into DEB installer for Admin
echo "Creating DEB installer for Admin..."
jpackage --input "$OUTPUT_DIR" --name "Admin" --main-jar "$JAR_NAME_ADMIN" --main-class "$MAIN_CLASS_ADMIN" --type deb --dest "$OUTPUT_DIR"

# Remove all .class files after creating JAR and DEB
echo "Removing .class files..."
rm -f "$SRC_DIR"/*.class

echo "Installer created at $OUTPUT_DIR/$APP_NAME.deb"
echo "Installer created at $OUTPUT_DIR/Admin.deb"
