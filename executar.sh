#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mvn -q -f demo/pom.xml compile
if [[ $# -eq 0 ]]; then
  exec java -cp demo/target/classes com.example.Main
elif [[ $1 == todos ]]; then
  for arquivo in exemplos/validos/*.pamu exemplos/invalidos/*.pamu; do
    printf '\n=== %s ===\n' "$arquivo"
    java -cp demo/target/classes com.example.Main "$arquivo"
  done
else
  exec java -cp demo/target/classes com.example.Main "$1"
fi
