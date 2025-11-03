variable "name" {
  description = "Name of the API gateway"
  type        = string
  default     = "default-api-gwy"
}

variable "tags" {
  type    = map(string)
  default = {}
}

variable "api_throttle_burst_limit" {
  description = "API Gateway throttle burst limit"
  type        = number
  default     = 10
}

variable "api_throttle_rate_limit" {
  description = "API Gateway throttle rate limit"
  type        = number
  default     = 5
}