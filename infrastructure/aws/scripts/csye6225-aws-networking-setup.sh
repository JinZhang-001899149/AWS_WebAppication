#!/usr/bin/env bash

#Arguments: STACK_NAME

STACK_NAME=$1
#Create VPC and get its Id
vpcId=`aws ec2 create-vpc --cidr-block 10.0.0.0/16 --query 'Vpc.VpcId' --output text`
#Tag vpc
aws ec2 create-tags --resources $vpcId --tags Key=Name,Value=$STACK_NAME-csye6225-vpc
echo "Vpc created-> Vpc Id:  "$vpcId

#Create subnets
subnetId=`aws ec2 create-subnet --vpc-id $vpcId --cidr-block 10.0.0.0/24 --query 'Subnet.SubnetId' --output text`
#Tag subnet
aws ec2 create-tags --resources $subnetId --tags Key=Name,Value=$STACK_NAME-csye6225-subnet

subnetId2=`aws ec2 create-subnet --vpc-id $vpcId --cidr-block 10.0.1.0/24 --query 'Subnet.SubnetId' --output text`
aws ec2 create-tags --resources $subnetId2 --tags Key=Name,Value=$STACK_NAME-csye6225-subnet2

subnetId3=`aws ec2 create-subnet --vpc-id $vpcId --cidr-block 10.0.2.0/24 --query 'Subnet.SubnetId' --output text`
aws ec2 create-tags --resources $subnetId3 --tags Key=Name,Value=$STACK_NAME-csye6225-subnet3
#aws ec2 create-subnet --vpc-id $vipId --cidr-block 10.0.2.0/24

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

#Create Route
aws ec2 create-route --route-table-id $routeTableId --destination-cidr-block 0.0.0.0/0 --gateway-id $gatewayId
echo "Route created: in "$routeTableId" target to "$gatewayId

#Attach route tables to subnets
aws ec2 associate-route-table --subnet-id $subnetId --route-table-id $routeTableId
aws ec2 associate-route-table --subnet-id $subnetId2 --route-table-id $routeTableId
aws ec2 associate-route-table --subnet-id $subnetId3 --route-table-id $routeTableId

#Create security group
#aws ec2 create-security-group --group-name MySecurityGroup description "My security group" --vpc-id $vpcId
#aws ec2 revoke-security-group-ingress --group-name MySecurityGroup --protocol tcp --port 22 --cidr 0.0.0.0/0
#aws ec2 revoke-security-group-ingress --group-name MySecurityGroup --protocol tcp --port 80 --cidr 0.0.0.0/0
groupString=`aws ec2 describe-security-groups --query 'SecurityGroups[].GroupId' --output text`
vpcIdString=`aws ec2 describe-security-groups --query 'SecurityGroups[].VpcId' --output text`

vpcarray=($vpcIdString)
sgarray=($groupString)
count=0
for vpc in "${vpcarray[@]}"
do
	if [ "$vpc" = "$vpcId" ]; then
		groupId=${sgarray[$count]}
	fi
count=$((count+1))
done
echo "Security Group Id: $groupId"
aws ec2 revoke-security-group-ingress --group-id $groupId --protocol all --source-group $groupId
aws ec2 authorize-security-group-ingress --group-id $groupId --protocol tcp --port 22 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-id $groupId --protocol tcp --port 80 --cidr 0.0.0.0/0
echo "Creating the information JSON file"
cat >./"$jsonFileName".json <<EOF
{
	"instanceId": "$instanceId",
	"groupId": "$groupId",
	"routeTableId": "$routeTableId",
	"subNetId1":"$subnetId1",
	"subNetId2":"$subnetId2",
	"subNetId3":"$subnetId3",
	"gatewayId": "$gatewayId",
	"vpcId": "$vpcId"
}
EOF

#Done
echo "Done!"








