# IAM role for Lambda execution
data "aws_iam_policy_document" "assume_role" {
  statement {
    effect = "Allow"
    principals {
      type        = "Service"
      identifiers = ["lambda.amazonaws.com"]
    }
    actions = ["sts:AssumeRole"]
  }
}

resource "aws_iam_role" "lambda_role" {
  name = "${var.name}-role"
  assume_role_policy = data.aws_iam_policy_document.assume_role.json
}

resource "aws_iam_role_policy_attachment" "policies" {
  count = length(var.config.policies)
  policy_arn = var.config.policies[count.index]
  role       = aws_iam_role.lambda_role.name
}

resource "aws_lambda_function" "lambda_function" {
  filename         = "${path.module}/../../../${var.config.filename}"
  function_name    = var.name
  role             = aws_iam_role.lambda_role.arn
  handler          = var.config.handler
  source_code_hash = filebase64sha256("${path.module}/../../../${var.config.filename}")
  runtime          = var.config.runtime
  architectures    = var.config.architectures
  timeout          = var.config.timeout
  tags             = var.config.tags
  environment {
    variables = var.env_variables
  }
}

module "api_gwy_integration" {
  source = "../api_gwy_integration"
  api_gwy_id = var.api_gwy_id
  api_gwy_arn = var.api_gwy_arn
  lambda_arn = aws_lambda_function.lambda_function.invoke_arn
  lambda_function_name = aws_lambda_function.lambda_function.function_name
  routes = var.config.routes
}