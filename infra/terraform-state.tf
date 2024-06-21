terraform {
  backend "s3" {
    bucket = "incode-tfstate"
    key = "terraform.tfstate"
    dynamodb_table = "incode-tfstate-lock-dynamo"
    region = "us-west-2"
    profile = "incode_admin"
    workspace_key_prefix = "incode-transformers"
  }
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 5.54.1"
    }
    random = {
      version = "~> 3.6"
    }
    null = {
      version = "~> 3.2"
    }
    github = {
      source  = "integrations/github"
      version = ">= 6.2.2"
    }
  }

  required_version = ">= 1.8.0"
}

provider "aws" {
  region  = var.region
  profile = var.profile
}

provider "github" {
  token        = local.github_token
}

provider "random" {}
provider "null" {}