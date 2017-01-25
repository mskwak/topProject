#!/bin/sh

JAVA_HOME=/opt/java
SMTP_CLIENT_HOME=/opt/data/smtpClient

if [ -e "/usr/sbin/smtp-sink" ]; then
	pidof smtp-sink | xargs kill -9
	smtp-sink -u root 0.0.0.0:25 1024 &
fi

$JAVA_HOME/bin/java -jar  -Dproperties.file.path=$SMTP_CLIENT_HOME/smtpClient.properties -Dlogback.configurationFile=$SMTP_CLIENT_HOME/logback.xml smtpClient.jar
