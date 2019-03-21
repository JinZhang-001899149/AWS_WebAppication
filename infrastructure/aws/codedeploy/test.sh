#!/bin/bash
sudo service tomcat stop
kill -9 $(lsof -i:8080 | awk '{print $2}' | tail -n 2)
cd /home/centos
<<<<<<< HEAD
sudo mkdir -p CloudWebApp/target/classes
=======
>>>>>>> 6bd1620e56e3628ca554f954c4f840ec9ac3aa24
sudo chmod 777 -R CloudWebApp/target/classes
