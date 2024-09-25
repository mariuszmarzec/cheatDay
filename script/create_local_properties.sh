#!/bin/bash

# Ścieżka do pliku local.properties
PROPERTIES_FILE="local.properties"

# Zawartość do zapisania w pliku
cat <<EOL > $PROPERTIES_FILE
storeFile=./debug.keystore
keyAlias=androiddebugkey
keyPassword=android
storePassword=android

prod.apiUrl=http://127.0.0.1
prod.authHeader=Authorization
test.apiUrl=http://127.0.0.1/test
test.authHeader=Authorization-Test
EOL

echo "$PROPERTIES_FILE created"