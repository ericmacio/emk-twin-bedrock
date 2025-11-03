terraform {
  backend "s3" {
    bucket = "tf-emk-twin-ai-backend"
    key = "terraform.tfstate"
    workspace_key_prefix = "workspaces"
    region = "eu-west-3"
  }
}