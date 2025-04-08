provider "aws" {
  region = "us-east-1"
}

resource "aws_iam_role" "lambda_exec_role" {
  name = "lambda_exec_role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "lambda.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "lambda_s3_access" {
  role       = aws_iam_role.lambda_exec_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3FullAccess"
}

resource "aws_lambda_function" "process_video_lambda" {
  function_name = "process-video-lambda"
  runtime       = "java11"
  role          = aws_iam_role.lambda_exec_role.arn
  handler       = "com.example.videoprocessing.lambda.LambdaHandler::handleRequest"
  filename      = "build/libs/video-processing-app.jar"

  environment {
    variables = {
      BUCKET_NAME = "video-processing-bucket"
    }
  }

  tags = {
    Name        = "ProcessVideoLambda"
    Environment = "Production"
  }
}

resource "aws_s3_bucket" "video_bucket" {
  bucket = "video-processing-bucket"
  acl    = "private"
  versioning {
    enabled = true
  }
  tags = {
    Name        = "VideoProcessingBucket"
    Environment = "Production"
  }
}