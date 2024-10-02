# Use the OpenJDK base image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy folders into the container
COPY libs /app/libs
COPY src /app/src
COPY test-output /app/test-output
COPY sources.txt /app/sources.txt
COPY README.txt  /app/README.txt
COPY testng.xml  /app/testng.xml

# Add Google Chrome repo and install Chrome
RUN apt-get update && \
    apt-get install -y wget unzip curl gnupg && \
    curl -sS https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable

# Install compatible ChromeDriver (version 129.*)
RUN wget -O /tmp/chromedriver.zip https://storage.googleapis.com/chrome-for-testing-public/129.0.6668.89/linux64/chrome-linux64.zip && \
    unzip /tmp/chromedriver.zip -d /usr/local/bin/ && \
    rm /tmp/chromedriver.zip
	
# Compile the Java files
RUN javac -cp "libs/*:src/main" -d bin @sources.txt

# Command to run the tests
CMD ["java", "-cp", "libs/*:bin", "test.AppTest"]