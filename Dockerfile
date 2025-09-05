# Use your private ECR-hosted Eclipse Temurin JDK 21 image
FROM 541971055918.dkr.ecr.eu-west-1.amazonaws.com/java-eclipse-temurin-21:21-jdk

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY target/application-0.0.1-SNAPSHOT.jar /app/app.jar

# Copy the entrypoint script
COPY ./entrypoint.sh /app/entrypoint.sh

# Make the entrypoint script executable
RUN chmod +x /app/entrypoint.sh

# Install dependencies using apt
RUN apt-get update && \
    apt-get install -y python3 python3-pip python3-venv jq && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Create virtual environment and install AWS CLI
RUN python3 -m venv /opt/venv && \
    /opt/venv/bin/pip install awscli && \
    ln -s /opt/venv/bin/aws /usr/bin/aws

# Expose application port
EXPOSE 8080

# Set entrypoint script
ENTRYPOINT ["/app/entrypoint.sh"]

# Run the application
CMD ["java", "-jar", "/app/app.jar"]
