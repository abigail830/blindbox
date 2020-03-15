#!/usr/bin/env bash

cd /var/www/blindbox
export $(grep -v '^#' env.list | xargs -d '\n')

echo "Starting Service"

nohup /usr/bin/java -jar -Dspring.profiles.active=dev ./blindbox.jar 1 $1 > ./Server.out &