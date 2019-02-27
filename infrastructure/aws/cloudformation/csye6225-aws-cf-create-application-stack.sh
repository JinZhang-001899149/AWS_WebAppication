
#!/usr/bin/env bash

stackname=$1
ami=$2
instanceName="$stackname-csye6225-instance"

vpcId=$(aws ec2 describe-vpcs --filters "Name=cidr-block-association.cidr-block,Values=10.0.0.0/16" --query "Vpcs[0].VpcId" --output text)
echo $vpcId




aws cloudformation create-stack --template-body file://./csye6225-cf-application.json --stack-name ${stackname} --capabilities "CAPABILITY_NAMED_IAM" --parameters ParameterKey=AMI,ParameterValue="$ami"

echo "Creating! Please wait until done"

aws cloudformation wait stack-create-complete --stack-name ${stackname}

echo "Done"
