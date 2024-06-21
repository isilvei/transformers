variable "region" {
  type = string
  default = "us-west-2"
  description = "Name of AWS region."
}

variable "profile" {
  type = string
  default = "default"
  description = "The AWS profile to use for execution."
}

variable "environment_name" {
  type    = string
  default = "prod"
}

variable "service_name" {
  type    = string
  default = "transformers-api"
}

variable "service_name_short" {
  type    = string
  default = "transformers"
}

variable "ecs_container_instance_type" {
  type    = string
  default = "t2.micro"
}

variable "container_port" {
  type    = string
  default = "8080"
}

variable "host_port" {
  type    = string
  default = "0"
}

variable "container_desired_count" {
  type    = string
  default = "1"
}

variable "container_reserved_task_memory" {
  type    = string
  default = "128"
}

variable "ecs_cluster_name" {
  type    = string
  default = "ecs-cluster"
}

variable "ecs_tg_healthcheck_endpoint" {
  type    = string
  default = "/status"
}

variable "github_username" {
  type    = string
  default = "silveimar"
}

variable "github_repo_name" {
  type    = string
  default = "incode-transformers"
}

variable "github_branch" {
  type    = string
  default = "main"
}

variable "github_token" {
  type    = string
}

variable "route53_hosted_zone" {
  type    = string
  default = "isilver.io"
}

variable "route53_record_set" {
  type    = string
  default = "www"
}

variable "service_hostname" {
  type    = string
  default = "www.isilver.io"
}

variable "platform_type" {
  type       = string
  default = "ecs"
}

variable "ssh_keypair_name" {
  type    = string
  default = ""
}

variable "vpc_name" {
  type    = string
  default = "incode-vpc"
}

