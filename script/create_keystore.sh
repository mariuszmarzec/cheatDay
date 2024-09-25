#!/bin/bash

# Define the keystore details
KEYSTORE_DIR="./app"
KEYSTORE_FILE="debug.keystore"
ALIAS="androiddebugkey"
KEYSTORE_PASSWORD="android"
KEY_PASSWORD="android"
DNAME="CN=Android Debug,O=Android,C=US"
VALIDITY_DAYS=10000

# Create the keystore directory if it doesn't exist
mkdir -p $KEYSTORE_DIR

# Generate the debug keystore if it doesn't exist
if [ ! -f "$KEYSTORE_DIR/$KEYSTORE_FILE" ]; then
    echo "Creating Android debug keystore..."
    keytool -genkeypair \
        -v \
        -keystore "$KEYSTORE_DIR/$KEYSTORE_FILE" \
        -alias $ALIAS \
        -keyalg RSA \
        -keysize 2048 \
        -validity $VALIDITY_DAYS \
        -storepass $KEYSTORE_PASSWORD \
        -keypass $KEY_PASSWORD \
        -dname "$DNAME"
    echo "Keystore created at: $KEYSTORE_DIR/$KEYSTORE_FILE"
else
    echo "Debug keystore already exists at: $KEYSTORE_DIR/$KEYSTORE_FILE"
fi