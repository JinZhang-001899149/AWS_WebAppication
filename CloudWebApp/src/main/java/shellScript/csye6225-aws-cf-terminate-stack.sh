#!/usr/bin/env bash

set -e

echo "The stack you want to delete : "

#Delete stack
aws cloudformation delete-stack --stack-name STACK_NAME-csye6225-vpc
aws cloudformation delete-stack --stack-name STACK_NAME-csye6225-rt

#Success
echo "Delete successfully!"

