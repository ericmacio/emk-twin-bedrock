output "lambda_id" {
  description = "Lambda id"
  value       = aws_lambda_function.lambda_function.id
}

output "invoke_arn" {
  description = "Invoke lambda arn"
  value       = aws_lambda_function.lambda_function.invoke_arn
}