# Instructions to run the script

#"csye6225-aws-networking-setup.sh" script:

Create a Virtual Private Cloud(VPC) resource.
Create 3 subnetcs in the VPC under same region and different availability zones.
Create Internet Gateway resource and attach it to the VPC.
Create a public Route Table and attach all subnets to the Route Table.
Create a public route in the public Route Table with destination CIDR BLOCK 0.0.0.0/0 and the internet Gateway as the target.
Modify the default security group for VPC, remove existing rules and add new rules that only allow TCP traffic on port 22 and 80 from anywhere.

#"csye6225-aws-networking-teardown.sh" script:
Delete networking resources using AWS CLI.
