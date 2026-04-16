@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM   https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@echo off
setlocal EnableExtensions EnableDelayedExpansion

set "MVNW_REPOURL=https://repo.maven.apache.org/maven2"
set "WRAPPER_VERSION=3.2.0"
set "WRAPPER_DIR=.mvn\wrapper"
set "WRAPPER_JAR=%WRAPPER_DIR%\maven-wrapper.jar"
set "WRAPPER_PROPERTIES=%WRAPPER_DIR%\maven-wrapper.properties"

set "BASE_DIR=%~dp0"
@REM Remove trailing backslash to avoid escaping quotes in Java args
if "%BASE_DIR:~-1%"=="\" set "BASE_DIR=%BASE_DIR:~0,-1%"

if not exist "%BASE_DIR%\\%WRAPPER_DIR%" mkdir "%BASE_DIR%\\%WRAPPER_DIR%"

if not exist "%BASE_DIR%\\%WRAPPER_PROPERTIES%" (
  > "%BASE_DIR%\\%WRAPPER_PROPERTIES%" (
    echo distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip
    echo wrapperUrl=%MVNW_REPOURL%/org/apache/maven/wrapper/maven-wrapper/%WRAPPER_VERSION%/maven-wrapper-%WRAPPER_VERSION%.jar
  )
)

if not exist "%BASE_DIR%\\%WRAPPER_JAR%" (
  set "WRAPPER_URL=%MVNW_REPOURL%/org/apache/maven/wrapper/maven-wrapper/%WRAPPER_VERSION%/maven-wrapper-%WRAPPER_VERSION%.jar"
  powershell -NoProfile -ExecutionPolicy Bypass -Command ^
    "$u = '!WRAPPER_URL!'; $p = Join-Path '%BASE_DIR%' '%WRAPPER_JAR%'; New-Item -ItemType Directory -Force -Path (Split-Path -Parent $p) | Out-Null; Invoke-WebRequest -Uri $u -OutFile $p -UseBasicParsing"
)

java -classpath "%BASE_DIR%\\%WRAPPER_JAR%" -Dmaven.multiModuleProjectDirectory="%BASE_DIR%" org.apache.maven.wrapper.MavenWrapperMain %*

endlocal

