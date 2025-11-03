# Use account id to ensure name is unique
data "aws_caller_identity" "current" {}

terraform {
  required_version = ">= 1.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
  }
}

provider "aws" {
  # Uses AWS CLI configuration (aws configure)
  region = "eu-west-3"
}

locals {
  name_prefix = "${var.project_name}-${terraform.workspace}"
  common_tags = {
    Project     = var.project_name
    Environment = terraform.workspace
    ManagedBy   = "terraform"
  }
}

module "s3" {
  source = "./modules/s3"
  for_each = var.s3_config
  name = "${var.project_name}-${terraform.workspace}-${each.value.name_suffix}-${data.aws_caller_identity.current.account_id}"
  config = each.value
}

module "s3_website" {
  source = "./modules/s3_website"
  for_each = var.s3_website_config
  name = "${var.project_name}-${terraform.workspace}-${each.value.name_suffix}-${data.aws_caller_identity.current.account_id}"
  config = each.value
}

module "api_gwy" {
  source = "./modules/api_gwy"
  name   = "${local.name_prefix}-${var.api_gwy_name}"
  tags   = local.common_tags
}

module "lambda" {
  source   = "./modules/lambda"
  for_each = var.lambda_config
  name = "${var.project_name}-${terraform.workspace}-${each.value.name_suffix}"
  config = each.value
  api_gwy_id = module.api_gwy.api_gateway_id
  api_gwy_arn = module.api_gwy.api_gateway_arn
  env_variables = each.value.name_suffix == "apiV1" ? {
    S3_BUCKET = module.s3["memory"].s3_bucket_id
    USE_S3           = "true"
    BEDROCK_MODEL_ID = var.bedrock_model_id
    CORS_ORIGINS     = "${module.cloudfront_distribution.cloudfront_url}"
    DEFAULT_AWS_REGION = "eu-west-3"
  } : {}

  # Ensure Lambda waits for the distribution to exist
  depends_on = [module.cloudfront_distribution]
}

module "cloudfront_distribution" {
  source = "./modules/cloudfront"
  website_endpoint = module.s3_website["frontend"].website_endpoint
  s3_bucket_id = module.s3_website["frontend"].s3_bucket_id
  tags = local.common_tags
}