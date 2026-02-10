#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUTPUT_ZIP="${1:-predslidergorillatag.zip}"

cd "$ROOT_DIR"
rm -f "$OUTPUT_ZIP"

find . -type f \
  ! -path './.git/*' \
  ! -path './.gradle/*' \
  ! -path './dist/*' \
  ! -path './.idea/*' \
  ! -path './*/build/*' \
  ! -name '*.iml' \
  ! -name '*.zip' \
  -print \
  | sed 's|^./||' \
  | zip "$OUTPUT_ZIP" -@

echo "Created: $ROOT_DIR/$OUTPUT_ZIP"
