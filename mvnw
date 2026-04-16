#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

set -euf

MVNW_REPOURL="https://repo.maven.apache.org/maven2"
WRAPPER_VERSION="3.2.0"
WRAPPER_DIR=".mvn/wrapper"
WRAPPER_JAR="$WRAPPER_DIR/maven-wrapper.jar"
WRAPPER_PROPERTIES="$WRAPPER_DIR/maven-wrapper.properties"

BASE_DIR=$(cd "$(dirname "$0")"; pwd -P)

download() {
  url="$1"
  target="$2"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$url" -o "$target"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$url" -O "$target"
  else
    echo "ERROR: Need curl or wget to download Maven Wrapper." >&2
    exit 1
  fi
}

mkdir -p "$BASE_DIR/$WRAPPER_DIR"

if [ ! -f "$BASE_DIR/$WRAPPER_PROPERTIES" ]; then
  cat > "$BASE_DIR/$WRAPPER_PROPERTIES" <<EOF
distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip
wrapperUrl=$MVNW_REPOURL/org/apache/maven/wrapper/maven-wrapper/$WRAPPER_VERSION/maven-wrapper-$WRAPPER_VERSION.jar
EOF
fi

if [ ! -f "$BASE_DIR/$WRAPPER_JAR" ]; then
  WRAPPER_URL=$(grep '^wrapperUrl=' "$BASE_DIR/$WRAPPER_PROPERTIES" | cut -d= -f2-)
  download "$WRAPPER_URL" "$BASE_DIR/$WRAPPER_JAR"
fi

exec java -classpath "$BASE_DIR/$WRAPPER_JAR" -Dmaven.multiModuleProjectDirectory="$BASE_DIR" org.apache.maven.wrapper.MavenWrapperMain "$@"

