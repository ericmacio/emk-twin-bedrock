variable "name" {
  description = "Name of the lambda"
  type        = string
}

variable "config" {
  description = "Configuration parameters for the lambda creation"
  type = object({
    name_suffix = string
    filename = string
    timeout = optional(number, 3)
    handler = optional(string, "lambda.lambda_handler")
    runtime = string
    architectures = list(string)
    policies = optional(list(string), [])
    tags = optional(map(string), {})
    routes = list(string)
  })
}

variable "env_variables" {
  description = "Environment variables of the lambda"
  type = map(string)
  default = {}
}

variable "api_gwy_id" {
  description = "API gateway id"
  type        = string
}

variable "api_gwy_arn" {
  description = "API gateway execution arn"
  type = string
}
