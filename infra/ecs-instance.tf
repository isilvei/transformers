data "aws_ami" "latest_ecs" {
  most_recent = true
  owners = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn-ami-*-amazon-ecs-optimized"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  filter {
    name   = "root-device-type"
    values = ["ebs"]
  }
}

resource "aws_security_group" "ecs_instance" {
  name        = "${var.environment_name}-${var.ecs_cluster_name}-ec2-sg"
  description = "${var.environment_name}-${var.ecs_cluster_name}-ec2-sg"
  vpc_id      = module.vpc.vpc_id
  tags = {
    Name      = "${var.environment_name}-${var.ecs_cluster_name}-ec2-sg"
  }
}

resource "aws_security_group_rule" "ssh" {
  security_group_id = aws_security_group.ecs_instance.id
  description       = "TCP/22 for VPC Range"
  type              = "ingress"
  protocol          = "tcp"
  from_port         = 22
  to_port           = 22
  cidr_blocks       = ["10.0.0.0/16"]
}

resource "aws_security_group_rule" "container_port" {
  security_group_id        = aws_security_group.ecs_instance.id
  description              = "Allows ALB to Connect to Dynamic ECS Port"
  type                     = "ingress"
  protocol                 = "tcp"
  from_port                = 0
  to_port                  = 65535
  source_security_group_id = aws_security_group.alb.id
}

resource "aws_security_group_rule" "ec2_egress" {
  security_group_id = aws_security_group.ecs_instance.id
  type              = "egress"
  protocol          = "-1"
  from_port         = 0
  to_port           = 0
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_iam_instance_profile" "ecs_instance" {
  name               = "${var.platform_type}-${var.environment_name}-instance-profile"
  role               = aws_iam_role.ecs_container_instance_role.name
}

locals {
  instance-userdata = templatefile("${path.module}/templates/bootstrap.sh.tpl", {
    ecs_cluster_name = aws_ecs_cluster.prod.name
    environment_name = var.environment_name
  })
}


resource "aws_instance" "ec2" {
  ami                         = data.aws_ami.latest_ecs.id
  instance_type               = var.ecs_container_instance_type
  subnet_id                   = element(random_shuffle.private_subnets.result, 1)
  key_name                    = var.ssh_keypair_name
  vpc_security_group_ids      = [aws_security_group.ecs_instance.id]
  associate_public_ip_address = false
  iam_instance_profile        = aws_iam_instance_profile.ecs_container_instance_profile.name

  lifecycle {
    ignore_changes            = [subnet_id,ami]
  }
  user_data_base64 =          base64encode(local.instance-userdata)

  tags = {
    Name                      = "${var.platform_type}-${var.environment_name}-ec2-instance"
    InstanceLifecycle         = "ondemand"
    ECSClusterName            = var.ecs_cluster_name
    Environment               = var.environment_name
    ManagedBy                 = "terraform"
  }

  depends_on = [ aws_ecs_cluster.prod ]

}