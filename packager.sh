#!/bin/bash
set -e

# Get the current directory
BASE_DIR="$(dirname "$(realpath "$0")")"
SRC_DIR="$BASE_DIR/src"         # Adjust this to your source files directory
OUTPUT_DIR="$BASE_DIR/installer" # Output directory for the JAR
APP_NAME="BookStoreApp"
JAR_NAME="$APP_NAME.jar"
MAIN_CLASS="Welcome" # Change to Welcome to run this class first

# Create output directory if it doesn't exist
if [ ! -d "$OUTPUT_DIR" ]; then
    mkdir "$OUTPUT_DIR"
fi

# Remove existing .class files
echo "Removing existing .class files..."
rm -f "$SRC_DIR"/*.class

# Compile Java files
echo "Compiling Java files..."
javac "$SRC_DIR"/*.java -d "$SRC_DIR"

# Create JAR file
echo "Creating JAR file..."
jar cfe "$OUTPUT_DIR/$JAR_NAME" "$MAIN_CLASS" -C "$SRC_DIR" .

# Run the JAR file
echo "Running the JAR file..."
java -jar "$OUTPUT_DIR/$JAR_NAME"

echo "Done. JAR file created at $OUTPUT_DIR/$JAR_NAME"
