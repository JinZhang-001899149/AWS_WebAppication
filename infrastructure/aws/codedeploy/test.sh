#!/usr/bin/env bash
sudo service tomcat stop
kill -9 $(lsof -t -i:8080 | awk '{print $2}' | tail -n 2)
cd /home/centos
sudo mkdir -p CloudWebApp/target/BOOT-INF/classes
sudo chmod 777 -R CloudWebApp/target/BOOT-INF/classes
