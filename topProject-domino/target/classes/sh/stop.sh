#!/bin/sh

/bin/ps -ef | grep /opt/daou/TMA2/bin/domino-crawler.jar | grep -v grep | awk '{print $2}' | xargs kill -15
