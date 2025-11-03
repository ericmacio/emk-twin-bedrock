project_name = "emk-twin"
api_gwy_name = "api-gwy"
s3_config = {
  "memory" = {
    name_suffix             = "memory"
    block_public_acls       = true
    block_public_policy     = true
    ignore_public_acls      = true
    restrict_public_buckets = true
    object_ownership_rule   = "BucketOwnerEnforced"
    static_website          = false
  }
}
s3_website_config = {
  "frontend" = {
    name_suffix             = "frontend"
    block_public_acls       = false
    block_public_policy     = false
    ignore_public_acls      = false
    restrict_public_buckets = false
    static_website          = true
  }
}
lambda_config = {
  "apiV1" = {
    name_suffix   = "apiV1"
    filename      = "backend/lambda-deployment.zip"
    timeout       = 60
    handler       = "lambda_handler.handler"
    runtime       = "python3.12"
    architectures = ["x86_64"]
    policies = [
      "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole",
      "arn:aws:iam::aws:policy/AmazonBedrockFullAccess",
      "arn:aws:iam::aws:policy/AmazonS3FullAccess"
    ]
    routes = ["GET /", "POST /chat", "GET /health"],
  }
}
bedrock_model_id = "eu.amazon.nova-micro-v1:0"
