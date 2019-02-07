#!/usr/bin/env bash

<<<<<<< HEAD
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



  #Create Route
  aws ec2 create-route --route-table-id $routeTableId --destination-cidr-block 0.0.0.0/0 --gateway-id $gatewayId
  echo "Route created: in "$routeTableId" target to "$gatewayId

  #Job Done
  echo "Job Done!"
=======



>>>>>>> e4f4df1bf6360798012e54b6046b46a08ba5c224
