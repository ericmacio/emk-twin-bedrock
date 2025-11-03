variable "name" {
  description = "Name of the S3 bucket"
  type        = string
}

variable "config" {
  description = "Configuration parameters for the s3 creation"
  type = object({
    name_suffix = string
    block_public_acls = optional(bool, true)
    block_public_policy = optional(bool, true)
    ignore_public_acls = optional(bool, true)
    restrict_public_buckets = optional(bool, true)
    object_ownership_rule = optional(string, "none")
    tags = optional(map(string), {})
  })
}