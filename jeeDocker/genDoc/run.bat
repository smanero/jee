echo off

SET ANT_HOME=D:/usr/apache-ant-1.9.2
SET JAVA_HOME=C:/Progra~1/Java/jre7

SET ANT_OPTS=%ANT_OPTS% -Dfile.encoding=UTF-8

%ANT_HOME%/bin/ant gen-erpiform-x43ws 2>out/error.log 1>out/log.log

pause
