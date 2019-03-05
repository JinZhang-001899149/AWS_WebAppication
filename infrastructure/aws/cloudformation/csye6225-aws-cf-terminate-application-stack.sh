#!/usr/bin/env bash


stackname=$1

echo "Terminating!  Please wait until done"

aws cloudformation delete-stack --stack-name ${stackname}&&
aws cloudformation wait stack-delete-complete --stack-name ${stackname}



echo "Done"
<<<<<<< HEAD
=======


>>>>>>> 75e71635868caa4326b644de4cdb9553d36804d9
