#!/bin/bash
sudo service tomcat stop
kill -9 $(lsof -i:8080 | awk '{print $2}' | tail -n 2)
cd /home/centos
sudo chmod 777 -R webapp/csye6225/target
