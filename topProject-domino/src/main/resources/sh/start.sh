#!/bin/sh

export LANG=en_US.UTF-8
export JAVA_HOME=/opt/daou/TMA2/3rd/java

JAVA=/opt/daou/TMA2/3rd/java/bin/java
DOMINO_ROOT=/opt/daou/TMA2

$JAVA -jar -server -Xms1024M -Xmx2048M -Dlogback.configurationFile=$DOMINO_ROOT/config/logback.xml -Djournal.configurationFile=$DOMINO_ROOT/config/domino.config $DOMINO_ROOT/bin/domino-crawler.jar &
