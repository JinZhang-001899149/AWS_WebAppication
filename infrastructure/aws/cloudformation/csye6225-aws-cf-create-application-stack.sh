
#!/usr/bin/env bash


stackname=$1
ami=$2

aws cloudformation create-stack --stack-name ${stackname} --template-body file://./csye6225-cf-application.json --capabilities "CAPABILITY_NAMED_IAM" --parameters ParameterKey=AMI,ParameterValue="$ami"

echo "Creating! Please wait until done"

aws cloudformation wait stack-create-complete --stack-name ${stackname}

echo "Done"
