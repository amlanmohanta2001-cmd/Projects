#!/bin/bash

# Define filenames
SOURCE="game.c"
BINARY="linux_game"

echo "Checking for GCC compiler..."
if ! command -v gcc &> /dev/null
then
    echo "Error: GCC is not installed. Install it with: sudo apt install build-essential"
    exit
fi

echo "Compiling $SOURCE..."
gcc $SOURCE -o $BINARY

if [ $? -eq 0 ]; then
    echo "Compilation successful! Starting game..."
    echo "---------------------------------------"
    ./$BINARY
else
    echo "Compilation failed."
fi