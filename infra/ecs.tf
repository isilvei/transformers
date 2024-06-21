resource "aws_ecs_cluster" "prod" {
  name = "${var.environment_name}-${var.service_name}-${var.ecs_cluster_name}"
}

## ECS SERVICE

resource "aws_cloudwatch_log_group" "ecs" {
  name = "${var.environment_name}-${var.ecs_cluster_name}-logs"
}


resource "aws_ecs_task_definition" "service" {
  family                   = "${var.environment_name}-${var.service_name}"
  container_definitions    = jsonencode([
    {
      name : "${var.environment_name}-${var.service_name}-td",
      image : aws_ecr_repository.repo.repository_url,
      memoryReservation : var.container_reserved_task_memory,
      portMappings: [
        {
          containerPort: var.container_port
          hostPort: var.host_port
        }
      ],
      environment: [
        {
          name: "AWS_DEFAULT_REGION",
          value: var.region
        }
      ],
      networkMode: "bridge",
      essential: true,
      logConfiguration: {
        logDriver: "awslogs",
        options: {
          awslogs-group: aws_cloudwatch_log_group.ecs.name,
          awslogs-region: var.region,
          awslogs-stream-prefix: var.service_name
        }
      }
    }
  ])
  requires_compatibilities = ["EC2"]
  network_mode             = "bridge"
  execution_role_arn       = aws_iam_role.ecs_execution_role.arn
  task_role_arn            = aws_iam_role.ecs_task_role.arn

  placement_constraints {
    type = "memberOf"
    expression = "attribute:environment == ${var.environment_name}"
  }
}

data "aws_ecs_task_definition" "service_current" {
  task_definition = aws_ecs_task_definition.service.family
}

resource "aws_ecs_service" "service" {
  name            = "${var.environment_name}-${var.service_name}"
  task_definition = "${aws_ecs_task_definition.service.family}:${max(aws_ecs_task_definition.service.revision, data.aws_ecs_task_definition.service_current.revision)}"

  cluster         = aws_ecs_cluster.prod.id
  launch_type     = "EC2"
  desired_count   = var.container_desired_count

  ordered_placement_strategy {
    type  = "spread"
    field = "attribute:ecs.availability-zone"
  }

  load_balancer {
    target_group_arn = aws_alb_target_group.service_tg.arn
    container_name   = "${var.environment_name}-${var.service_name}"
    container_port   = var.container_port
  }
}


