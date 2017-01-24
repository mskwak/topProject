#!/bin/sh

JAVA_HOME=/opt/java
SMTP_CLIENT_HOME=/opt/data/smtpClient

pidof smtp-sink | xargs kill -9
smtp-sink -u root 0.0.0.0:25 1024 &

$JAVA_HOME/bin/java -jar  -Dproperties.file.path=$SMTP_CLIENT_HOME/smtpClient.properties -Dlogback.configurationFile=$SMTP_CLIENT_HOME/logback.xml smtpClient.jar
