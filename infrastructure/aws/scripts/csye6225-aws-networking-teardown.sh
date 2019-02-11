#!/usr/bin/env bash

#Arguments: STACK_NAME
STACK_NAME=$1
#Get a vpc-Id using the name provided
vpcId=`aws ec2 describe-vpcs --filter "Name=tag:Name,Values=${STACK_NAME}-csye6225-vpc" --query 'Vpcs[*].{id:VpcId}' --output text`
#Get a Internet Gateway Id using the name provided
gatewayId=`aws ec2 describe-internet-gateways --filter "Name=tag:Name,Values=${STACK_NAME}-csye6225-InternetGateway" --query 'InternetGateways[*].{id:InternetGatewayId}' --output text`
#Get a route table Id using the name provided
routeTableId=`aws ec2 describe-route-tables --filter "Name=tag:Name,Values=${STACK_NAME}-csye6225-rt" --query 'RouteTables[*].{id:RouteTableId}' --output text`
#Get subnet Id using the name provided
subnetId=`aws ec2 describe-subnets --filter "Name=tag:Name,Values=${STACK_NAME}-csye6225-subnet" --query 'Subnets[*].{id:SubnetId}' --output text`
subnetId2=`aws ec2 describe-subnets --filter "Name=tag:Name,Values=${STACK_NAME}-csye6225-subnet2" --query 'Subnets[*].{id:SubnetId}' --output text`
subnetId3=`aws ec2 describe-subnets --filter "Name=tag:Name,Values=${STACK_NAME}-csye6225-subnet3" --query 'Subnets[*].{id:SubnetId}' --output text`

#Delete the route
aws ec2 delete-route --route-table-id $routeTableId --destination-cidr-block 0.0.0.0/0
echo "Deleting the route..."


#Delete the subnets
aws ec2 delete-subnet --subnet-id $subnetId
aws ec2 delete-subnet --subnet-id $subnetId2
aws ec2 delete-subnet --subnet-id $subnetId3
echo "Deleting the subnets..."

#Delete the route table
aws ec2 delete-route-table --route-table-id $routeTableId
echo "Deleting the route table-> route table id: "$routeTableId

#Detach Internet gateway and vpc
aws ec2 detach-internet-gateway --internet-gateway-id $gatewayId --vpc-id $vpcId
echo "Detaching the Internet gateway from vpc..."

#Delete the Internet gateway
aws ec2 delete-internet-gateway --internet-gateway-id $gatewayId
echo "Deleting the Internet gateway-> gateway id: "$gatewayId

#Delete the vpc
aws ec2 delete-vpc --vpc-id $vpcId
echo "Deleting the vpc-> vpc id: "$vpcId

echo "Done!"
