echo off

SET ANT_HOME=D:/usr/apache-ant-1.9.2
SET JAVA_HOME=C:/Progra~1/Java/jdk1.7.0_55

:Execute
echo "Testing XRadar Version %XRADAR_VERSION% con %JAVA_HOME%"
REM cleaning 
if "%1" == "clean" goto :Clean
goto :NoClean

:Clean
REM cleaning 
%ANT_HOME%/bin/ant cleans
goto :End

:NoClean
SET ANT_OPTS=%ANT_OPTS% -Dfile.encoding=UTF-8
@REM %ANT_HOME%/bin/ant statics_current 2>error.log 1>log.log

%ANT_HOME%/bin/ant dynamics_all 2>error.log 1>log.log


pause

:End;