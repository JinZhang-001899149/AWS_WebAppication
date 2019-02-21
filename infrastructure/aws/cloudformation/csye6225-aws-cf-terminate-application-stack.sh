#!/usr/bin/env bash


echo "Enter The Stack Name:"
read stackname

echo "Terminating!  Please wait until done"

aws cloudformation delete-stack --stack-name ${stackname}&&
aws cloudformation wait stack-delete-complete --stack-name ${stackname}



echo "Done"