#!/bin/bash

echo "The stack you want to delete:"
read stackname

#Delete the cloud formation stack
aws cloudformation delete-stack --stack-name ${stackname}&&
aws cloudformation wait stack-delete-complete --stack-name ${stackname}

#Delete Successfully
echo "Delete Successfully!"