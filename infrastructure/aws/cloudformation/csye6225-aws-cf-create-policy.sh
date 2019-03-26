#!/usr/bin/env bash
stackname=$1
username=$2
domain=$3
s3bucket1="arn:aws:s3:::code-deploy.csye6225-spring2019-"$domain".me"
s3bucket2="arn:aws:s3:::csye6225-spring2019-"$domain".me.csye6225.com"

aws cloudformation create-stack --stack-name $stackname --capabilities CAPABILITY_NAMED_IAM --template-body file://./csye6225-cf-policy.json --parameters ParameterKey=circleci,ParameterValue=$username ParameterKey=s3bucket1,ParameterValue=$s3bucket1 ParameterKey=s3bucket2,ParameterValue=$s3bucket2|| { echo "command failed"; exit 1; }
echo "Creating!Please wait until done!"
aws cloudformation wait stack-create-complete --stack-name $stackname
echo "Done!"
