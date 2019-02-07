#!/usr/bin/env bash


<<<<<<< HEAD
date

set -e
#Usage: Deleting our networking resources such as Virtual Private Cloud (VPC), Internet Gateway, Route Table and Routes

#Usage: setting up our networking resources such as Virtual Private Cloud (VPC), Internet Gateway,
# Route Table and Routes

#Arguments: STACK_NAME

STACK_NAME=$1
#Create VPC and get its Id
vpcId=`aws ec2 create-vpc --cidr-block 10.0.0.0/16 --query 'Vpc.VpcId' --output text`
#Tag vpc
aws ec2 create-tags --resources $vpcId --tags Key=Name,Value=$STACK_NAME-csye6225-vpc
echo "Vpc created-> Vpc Id:  "$vpcId

#Create Internet Gateway
gatewayId=`aws ec2 create-internet-gateway --query 'InternetGateway.InternetGatewayId' --output text`
#Tag Internet Gateway
aws ec2 create-tags --resources $gatewayId --tags Key=Name,Value=$STACK_NAME-csye6225-InternetGateway
echo "Internet gateway created-> gateway Id: "$gatewayId

#Attach Internet Gateway to Vpc
aws ec2 attach-internet-gateway --internet-gateway-id $gatewayId --vpc-id $vpcId
echo "Attached Internet gateway: "$gatewayId" to Vpc: "$vpcId

#Create Route Table
routeTableId=`aws ec2 create-route-table --vpc-id $vpcId --query 'RouteTable.RouteTableId' --output text`
#Tag Route Table
aws ec2 create-tags --resources $routeTableId --tags Key=Name,Value=$STACK_NAME-csye6225-public-route-table
echo "Route table created -> route table Id: "$routeTableId

#Create Route
aws ec2 create-route --route-table-id $routeTableId --destination-cidr-block 0.0.0.0/0 --gateway-id $gatewayId
echo "Route created: in "$routeTableId" target to "$gatewayId
#Job Done
echo "Job Done!"
=======
  #Author: Jin Zhang
  echo "Author: Jin Zhang"
  echo "        zhang.jin2@husky.neu.edu"

   #Usage: setting up our networking resources such as Virtual Private Cloud (VPC), Subnets, Internet Gateway, Route Table and Routes

   #Arguments: STACK_NAME

   STACK_NAME=$1

  #Create VPC and get its Id
  vpcId=`aws ec2 create-vpc --cidr-block 10.0.0.0/16 --query 'Vpc.VpcId' --output text`

  #Tag vpc
  aws ec2 create-tags --resources $vpcId --tags Key=Name,Value=$STACK_NAME-csye6225-vpc
  echo "Vpc created-> Vpc Id:  "$vpcId



  #Create subnet 10.0.0.1/24(myway)
  #subnet01Id=`aws ec2 create-subnet --vpc-id $vpcId --cidr-block 10.0.1.0/24 --query 'Subnet.SubnetId' --output text`

  #Tag subnet 10.0.0.1/24
  #aws ec2 create-tags --resources $subnet01Id --tags Key=Name,Value=$STACK_NAME-csye6225-subnet01
  #echo "subnet01 created-> SubnetId: "$subnet01Id
  #aws ec2 describe-subnets --filters "$vpcId" --query 'Subnets[*].{ID:SubnetId,CIDR:CidrBlock} --output text'


  #Create subnet 10.0.0.1/24 (original)
  #aws ec2 create-subnet --vpc-id $vpcId --cidr-block 10.0.0.0/24

  #Create subnet 10.0.0.1/24(myway)
  subnet01=`aws ec2 create-subnet --vpc-id $vpcId --cidr-block 10.0.1.0/24 --query 'Subnet.SubnetId' --output text`

  #Create subnet 10.0.0.1/24(myway)
  subnet02=`aws ec2 create-subnet --vpc-id $vpcId --cidr-block 10.0.0.0/24 --query 'Subnet.SubnetId' --output text`

  #Create subnet 10.0.0.1/24(myway)
  subnet03=`aws ec2 create-subnet --vpc-id $vpcId --cidr-block 10.0.2.0/24 --query 'Subnet.SubnetId' --output text`

  #Create subnet 10.0.0.0/24
  #aws ec2 create-subnet --vpc-id $vpcId --cidr-block 10.0.1.0/24

  #Create subnet 10.0.0.024
  #aws ec2 create-subnet --vpc-id $vpcId --cidr-block 10.0.2.0/24





  #Create Internet Gateway
  gatewayId=`aws ec2 create-internet-gateway --query 'InternetGateway.InternetGatewayId' --output text`

  #Tag Internet Gateway
  aws ec2 create-tags --resources $gatewayId --tags Key=Name,Value=$STACK_NAME-csye6225-InternetGateway
  echo "Internet gateway created-> gateway Id: "$gatewayId

  #Attach Internet Gateway to Vpc
  aws ec2 attach-internet-gateway --internet-gateway-id $gatewayId --vpc-id $vpcId
  echo "Attached Internet gateway: "$gatewayId" to Vpc: "$vpcId

  #Create Route Table
  routeTableId=`aws ec2 create-route-table --vpc-id $vpcId --query 'RouteTable.RouteTableId' --output text`

  #Tag Route Table
  aws ec2 create-tags --resources $routeTableId --tags Key=Name,Value=$STACK_NAME-csye6225-rt
  echo "Route table created -> route table Id: "$routeTableId




  #Attach subnet01 to Route Table
  aws ec2 associate-route-table --subnet-id $subnet01 --route-table-id $routeTableId
  echo "Attached subnet01: "$subnet01" to routeTable: "$routeTableId

  aws ec2 associate-route-table --subnet-id $subnet02 --route-table-id $routeTableId
  echo "Attached subnet02: "$subnet02" to routeTable: "$routeTableId

  aws ec2 associate-route-table --subnet-id $subnet03 --route-table-id $routeTableId
  echo "Attached subnet03: "$subnet03" to routeTable: "$routeTableId

  #create security group
  aws ec2 create-security-group --group-name MySecurityGroup --description "My security group" --vpc-id $vpcId

  #groupid= `aws ec2 create-security-group --group-name MySecurityGroup --description "My security group" --vpc-id $vpcId --query 'MySecurityGroup.GroupId' --output text`



  #aws ec2 authorize-security-group-ingress --group-name MySecurityGroup --protocol tcp --port 22 --cidr 203.0.113.0/24


  #Create Route
  aws ec2 create-route --route-table-id $routeTableId --destination-cidr-block 0.0.0.0/0 --gateway-id $gatewayId
  echo "Route created: in "$routeTableId" target to "$gatewayId

  #Job Done
  echo "Job Done!"

>>>>>>> 9e133baba1a4391db760c27afb9ebc087ca3ad59
