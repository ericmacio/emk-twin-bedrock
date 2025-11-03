variable "project_name" {
  description = "Name prefix for all resources"
  type        = string
  validation {
    condition     = can(regex("^[a-z0-9-]+$", var.project_name))
    error_message = "Project name must contain only lowercase letters, numbers, and hyphens."
  }
}

variable "s3_config" {
  description = "Configuration of the S3 buckets"
  type = map
}

variable "s3_website_config" {
  description = "Configuration of the S3 buckets for static website"
  type = map
}

variable "api_gwy_name" {
  description = "Name of the API gateway"
  type        = string
  validation {
    condition     = can(regex("^[a-z0-9-]+$", var.api_gwy_name))
    error_message = "API gateway name must contain only lowercase letters, numbers, and hyphens."
  }
}

variable "lambda_config" {
  description = "Configuration of lambda"
  type = map
}

variable "bedrock_model_id" {
  description = "Bedrock model ID"
  type        = string
  default     = "eu-amazon.nova-micro-v1:0"
}
