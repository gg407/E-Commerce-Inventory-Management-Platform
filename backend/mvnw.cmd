@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.2.0
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
@SET __MVNW_CMD__=
@SET __MVNW_ERROR__=
@SET __MVNW_PSMODULEP_SAVE=%PSModulePath%
@SET PSModulePath=
@FOR /F "usebackq tokens=1* delims==" %%A IN (`powershell -noprofile "& {$out=(Get-Command -ErrorAction Ignore -Type Application java).Path; if ($out) { print $out }}"`) DO (
  IF "%%A"=="MVNW_repourl" (SET MVNW_repourl=%%B) ELSE IF "%%A"=="MVNW_username" (SET MVNW_username=%%B) ELSE IF "%%A"=="MVNW_password" (SET MVNW_password=%%B) ELSE IF "%%A"=="MVNW_verbose" (SET MVNW_verbose=%%B)
)
@SET PSModulePath=%__MVNW_PSMODULEP_SAVE%
@SET __MVNW_PSMODULEP_SAVE=

@SET WRAPPER_JAR="%~dp0\.mvn\wrapper\maven-wrapper.jar"
@SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@IF NOT EXIST %WRAPPER_JAR% (
  @IF "%MVNW_VERBOSE%"=="true" (
    ECHO Downloading Maven Wrapper JAR ...
  )
  powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object System.Net.WebClient).DownloadFile((Get-Content '%~dp0\.mvn\wrapper\maven-wrapper.properties' | Select-String 'wrapperUrl').ToString().Split('=')[1], '%~dp0\.mvn\wrapper\maven-wrapper.jar')}"
)

@java %JAVA_OPTS% %MAVEN_OPTS% ^
  "-Dmaven.multiModuleProjectDirectory=%~dp0" ^
  -classpath %WRAPPER_JAR% ^
  %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*
@IF ERRORLEVEL 1 GOTO error
@GOTO end

:error
@SET ERROR_CODE=1

:end
@cmd /C exit /B %ERROR_CODE%
