#!/bin/bash

if ! command -v openssl &> /dev/null; then
    echo "OpenSSL is not installed" >&2
    exit 1
fi

readonly OUTPUT_DIR="${1:-.secrets/keys}"
readonly PRIVATE_KEY_DIR="$OUTPUT_DIR/app.key"
readonly PUBLIC_KEY_DIR="$OUTPUT_DIR/app.pub"

openssl genpkey -algorithm RSA -out "$PRIVATE_KEY_DIR" -pkeyopt rsa_keygen_bits:2048 &> /dev/null
if [ ! -f "$PRIVATE_KEY_DIR" ]; then
    echo "Error generating the private key" >&2
    exit 1
fi

openssl rsa -pubout -in "$PRIVATE_KEY_DIR" -out "$PUBLIC_KEY_DIR" &> /dev/null
if [ ! -f "$PUBLIC_KEY_DIR" ]; then
    echo "Error generating the public key" >&2
    exit 1
fi

chmod 644 "$PRIVATE_KEY_DIR" "$PUBLIC_KEY_DIR" &> /dev/null
echo "RSA Keys generated successfully"
