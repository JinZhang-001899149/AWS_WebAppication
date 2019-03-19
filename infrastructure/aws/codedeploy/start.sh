#!/bin/bash
cd /home/centos
url=$(cat mysqlsetting.txt | sed -r 's/.*"(.+)".*/\1/')
echo $url

cd /home/centos/webapp/csye6225/target
pwd
java -jar assignment01-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev --spring.datasource.username=csye6225master --spring.datasource.password=csye6225password --spring.datasource.url=jdbc:mysql://$url/csye6225 --server.port=8080 >/dev/null 2>&1 &

