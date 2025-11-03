output "s3_bucket_id" {
    value = aws_s3_bucket.s3.id
}

output "website_endpoint" {
    value = aws_s3_bucket_website_configuration.s3.website_endpoint
}