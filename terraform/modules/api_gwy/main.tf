resource "aws_apigatewayv2_api" "api_gwy" {
  name          = var.name
  protocol_type = "HTTP"
  tags          = var.tags
  cors_configuration {
    allow_credentials = false
    allow_headers     = ["*"]
    allow_methods     = ["GET", "POST", "OPTIONS"]
    allow_origins     = ["*"]
    max_age           = 300
  }
}

resource "aws_apigatewayv2_stage" "default" {
  api_id      = aws_apigatewayv2_api.api_gwy.id
  name        = "$default"
  auto_deploy = true
  tags        = var.tags
  default_route_settings {
    throttling_burst_limit = var.api_throttle_burst_limit
    throttling_rate_limit  = var.api_throttle_rate_limit
  }
}