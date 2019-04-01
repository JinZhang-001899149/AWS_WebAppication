#!/usr/bin/env bash
sudo service tomcat stop
sudo kill -9 $(sudo lsof -t -i:8080)
sudo kill -9 $(sudo lsof -t -i:8080 | awk '{print $2}' | tail -n 2)

cd /home/centos
sudo chmod 777 -R CloudWebApp/target
sudo chmod 777 -R /opt
