# Instructions to run the scripts

#"csye6225-aws-cf-create-setup.sh" script:

Create a Virtual Private Cloud (VPC)
Create a cloudformation stack taking STACK_NAME as parameter
Create 3 subnets in VPC.
Create and configure required networking resources
Create Internet Gateway resource called Internet Gateway
Attach the Internet Gateway to the VPC
Create a public Route Table
Create a public route in the Route Table with destination CIDR block 0.0.0.0/0 and InternetGateway as the target

#"csye6225-aws-cf-terminate-stack.sh" script:
Delete stack and all networking resources .

#"csye6225-aws-networking-json":
creates the cloudformation template for the stack
