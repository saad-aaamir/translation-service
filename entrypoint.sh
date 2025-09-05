#!/bin/sh

# Fetch secrets from AWS Secrets Manager and store them in /tmp/secrets.env
aws secretsmanager get-secret-value --region "$AWS_REGION" --secret-id "active-sloth-prod-shared-secrets" --query SecretString --output text | \
jq -r 'to_entries|map("\(.key)=\(.value|tostring)")|.[]' > /tmp/secrets.env || {
    echo "Failed to fetch yap-pk-qa-shared-secrets"
    exit 1
}

# Copy secrets to the application's .env file
cp /tmp/secrets.env /app/.env

# Load environment variables from the .env file
if [ -f /app/.env ]; then
    export $(grep -v '^#' /app/.env | xargs)
else
    echo "/app/.env file not found."
    exit 1
fi

# Execute the command specified by CMD in the Dockerfile
exec "$@"
