# API Gateway lambda integration
resource "aws_apigatewayv2_integration" "lambda" {
  api_id           = var.api_gwy_id
  integration_type = "AWS_PROXY"
  integration_uri  = var.lambda_arn
}

# API Gateway Routes
resource "aws_apigatewayv2_route" "route" {
  api_id    = var.api_gwy_id
  count = length(var.routes)
  route_key = var.routes[count.index]
  target    = "integrations/${aws_apigatewayv2_integration.lambda.id}"
}

# Lambda permission for API Gateway
resource "aws_lambda_permission" "api_gw" {
  statement_id  = "AllowExecutionFromAPIGateway"
  action        = "lambda:InvokeFunction"
  function_name = var.lambda_function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${var.api_gwy_arn}/*/*"
}