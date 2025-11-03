variable "website_endpoint" {
    description = "Endpoint of the website origin"
    type = string
}

variable "s3_bucket_id" {
    description = "S3 bucket id of the origin"
    type = string
}

variable "tags" {
  type    = map(string)
  default = {}
}
