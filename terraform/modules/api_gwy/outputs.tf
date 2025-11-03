output "api_gateway_url" {
  description = "URL of the API Gateway"
  value       = aws_apigatewayv2_api.api_gwy.api_endpoint
}

output "api_gateway_id" {
  description = "Id of the API Gateway"
  value       = aws_apigatewayv2_api.api_gwy.id
}

output "api_gateway_arn" {
  description = "Execution arn of the API Gateway"
  value = aws_apigatewayv2_api.api_gwy.execution_arn
}