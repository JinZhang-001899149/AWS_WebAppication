#!/usr/bin/env bash
stackname=$1
UserName=$2
s3=$3
s3bucket="arn:aws:s3:::code-deploy.csye6225-spring2019-"$s3".me/*"


export REGION=us-east-1

#aws cloudformation create-stack --stack-name $stackname
#--capabilities CAPABILITY_NAMED_IAM --template-body
#file://./csye6225-cf-policy.json --parameters
#ParameterKey=circleci,ParameterValue=$UserName
#ParameterKey=bucketArn,ParameterValue=$s3bucket||{"command failed";exit 1;}


aws cloudformation create-stack --stack-name $STACK_NAME --template-body file://csye6225-cf-policy.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=circleci,ParameterValue=$UserName ParameterKey=bucketArn,ParameterValue=$s3bucket

aws cloudformation wait stack-create-complete --stack-name $stackname
