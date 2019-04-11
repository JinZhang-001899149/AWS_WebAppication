
#!/usr/bin/env bash

stackname=$1

vpc=$2
VpcName=$vpc"-csye6225-vpc"
ami=$3
subnet1=$vpc"-cye6225-subnet1"
subnet2=$vpc"-cye6225-subnet2"
subnet3=$vpc"-cye6225-subnet3"
domain=$4
s3bucket="code-deploy.csye6225-spring2019-"$domain".me"
Domain="csye6225-spring2019-"$domain".me"
NoWAFDomain="nowaf.csye6225-spring2019-"$domain".me"



instanceName="$stackname-csye6225-instance"


vpcid=`aws ec2 describe-vpcs --filter Name=tag:Name,Values="${VpcName}" --query 'Vpcs[*].{id:VpcId}' --output text`
echo $vpcid

PublicSubnet1=`aws ec2 describe-subnets --filters Name=vpc-id,Values="${vpcid}" "Name=cidrBlock,Values=10.0.0.0/24" --query 'Subnets[*].{id:SubnetId}' --output text`
echo $PublicSubnet1

PublicSubnet2=`aws ec2 describe-subnets --filters Name=vpc-id,Values="${vpcid}" "Name=cidrBlock,Values=10.0.1.0/24" --query 'Subnets[*].{id:SubnetId}' --output text`
echo $PublicSubnet2

PublicSubnet3=`aws ec2 describe-subnets --filters Name=vpc-id,Values="${vpcid}" "Name=cidrBlock,Values=10.0.2.0/24" --query 'Subnets[*].{id:SubnetId}' --output text`
echo $PublicSubnet3


User=`aws iam get-user --user-name circleci --query User.UserId --output text`
echo $User

UserName="circleci"
echo $UserName





CERTIFICATE_ARN=`aws acm list-certificates --query 'CertificateSummaryList[0].CertificateArn' --output text`
echo $CERTIFICATE_ARN


aws cloudformation create-stack --stack-name ${stackname} --template-body file://./csye6225-cf-auto-scaling-application.json --capabilities CAPABILITY_NAMED_IAM --parameters ParameterKey=vpcId,ParameterValue=$vpcid ParameterKey=ImageId,ParameterValue=$ami ParameterKey=publicsubnet1,ParameterValue=$PublicSubnet1 ParameterKey=publicsubnet2,ParameterValue=$PublicSubnet2 ParameterKey=publicsubnet3,ParameterValue=$PublicSubnet3 ParameterKey=circleci,ParameterValue=$UserName ParameterKey=s3bucket,ParameterValue=$s3bucket ParameterKey=Domain,ParameterValue=$Domain ParameterKey=CertificateArn1,ParameterValue=$CERTIFICATE_ARN ParameterKey=NoWAFDomain,ParameterValue=$NoWAFDomain

echo "Creating! Please wait until done"



aws cloudformation wait stack-create-complete --stack-name ${stackname}

echo "Done"
