#!/usr/bin/env bash


stackname=$1
vpc=$2

VpcName="$vpc-csye6225-vpc"
GatewayName="$stackname-csye6225-InternetGateway"
RouteTableName="$stackname-csye6225-rt"
PublicSubnetName1="$stackname-csye6225-subnet1"
PublicSubnetName2="$stackname-csye6225-subnet2"
PublicSubnetName3="$stackname-csye6225-subnet3"

aws cloudformation create-stack --stack-name ${stackname} --template-body file://./csye6225-cf-networking.json --parameters ParameterKey=vpcName,ParameterValue=$VpcName ParameterKey=gatewayName,ParameterValue=$GatewayName ParameterKey=routeTableName,ParameterValue=$RouteTableName ParameterKey=publicsubnet1,ParameterValue=$PublicSubnetName1 ParameterKey=publicsubnet2,ParameterValue=$PublicSubnetName2 ParameterKey=publicsubnet3,ParameterValue=$PublicSubnetName3

echo "Creating! Please wait until done"

aws cloudformation wait stack-create-complete --stack-name ${stackname}

echo "Done"
