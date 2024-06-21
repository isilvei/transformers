resource "aws_ecr_repository" "repo" {
  name = "${var.environment_name}-${var.service_name}"
}