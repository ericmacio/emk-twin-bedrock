# Create resources
resource "aws_s3_bucket" "s3" {
  bucket = var.name
  tags   = var.config.tags
}

resource "aws_s3_bucket_public_access_block" "s3" {
  bucket                  = aws_s3_bucket.s3.id
  block_public_acls       = var.config.block_public_acls
  block_public_policy     = var.config.block_public_policy
  ignore_public_acls      = var.config.ignore_public_acls
  restrict_public_buckets = var.config.restrict_public_buckets
}

resource "aws_s3_bucket_ownership_controls" "s3" {
  count  = var.config.object_ownership_rule != "none" ? 1 : 0
  bucket = aws_s3_bucket.s3.id
  rule {
    object_ownership = var.config.object_ownership_rule
  }
}
