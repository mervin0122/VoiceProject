#!/usr/bin/env bash
DIR=$(cd `dirname $0`; pwd) #current directory
java -version # please check java version >= 1.8

SDK_VERSION=0.3.0
FILE=$1
if [ -z "$FILE" ] ; then
	FILE="MainTest"
fi
rm -rf run 
mkdir run
cp -r src/main/java/* run
CLASSPATH='run' #for main class
CLASSPATH=$CLASSPATH':src/main/resources' #for logback.xml
JAR_LIBS="libs/ht-java-talker-${SDK_VERSION}-out.jar:libs/runtime-libs/commons-codec-1.9.jar:libs/runtime-libs/httpclient-4.5.3.jar:libs/runtime-libs/httpcore-4.4.6.jar:libs/runtime-libs/jackson-annotations-2.8.9.jar:libs/runtime-libs/jackson-core-2.8.9.jar:libs/runtime-libs/jackson-databind-2.8.9.jar:libs/runtime-libs/jcl-over-slf4j-1.7.25.jar:libs/runtime-libs/logback-classic-1.2.3.jar:libs/runtime-libs/logback-core-1.2.3.jar:libs/runtime-libs/slf4j-api-1.7.25.jar"
CLASSPATH=${CLASSPATH}:${JAR_LIBS}

javac run/com/baidu/aip/demotest/*.java  -classpath $JAR_LIBS > /dev/null
java -Xmx1024m -Dfile.encoding=UTF-8 -Djava.library.path=$DIR/libs -classpath ${CLASSPATH}  com.baidu.aip.demotest.${FILE}  #-Daip.talker.conf.filename=conf/sdk.properties  -Dlogback.configurationFile=/path/to/logback.xml

#logback configuration manual https://logback.qos.ch/manual/configuration.html  In production environment, remove <appender-ref ref="STDOUT"/> in logback.xml
