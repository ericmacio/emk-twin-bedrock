output "api_gateway_url" {
  value = module.api_gwy.api_gateway_url
}

output "api_gateway_id" {
  value = module.api_gwy.api_gateway_id
}

output "s3_bucket_memory_id" {
    value = module.s3["memory"].s3_bucket_id
}

output "s3_bucket_frontend_id" {
    value = module.s3_website["frontend"].s3_bucket_id
}

output "lambda_apiV1_id" {
  value = module.lambda["apiV1"].lambda_id
}

output "lambda_apiV1_arn" {
  value = module.lambda["apiV1"].invoke_arn
}

output "website_endpoint" {
    value = module.s3_website["frontend"].website_endpoint
}

output "cloudfront_url" {
  description = "URL of the CloudFront distribution"
  value       = "${module.cloudfront_distribution.cloudfront_url}"
}