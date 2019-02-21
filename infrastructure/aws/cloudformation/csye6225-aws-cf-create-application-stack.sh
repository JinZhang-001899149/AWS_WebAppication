
#!/usr/bin/env bash

echo "Enter The Stack Name:"
read stackname

VpcName="$stackname-csye6225-vpc"
GatewayName="$stackname-csye6225-InternetGateway"
RouteTableName="$stackname-csye6225-rt"

aws cloudformation create-stack --stack-name ${stackname} --template-body file://./csye6225-cf-application.json --parameters ParameterKey=vpcName,ParameterValue=$VpcName ParameterKey=gatewayName,ParameterValue=$GatewayName ParameterKey=routeTableName,ParameterValue=$RouteTableName

echo "Creating! Please wait until done"

aws cloudformation wait stack-create-complete --stack-name ${stackname}

echo "Done"