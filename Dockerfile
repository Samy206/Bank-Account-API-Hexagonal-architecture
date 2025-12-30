FROM maven:3.9.6-eclipse-temurin-21

# App location
WORKDIR /ExaltApp
COPY . /ExaltApp

# App build
RUN cd /ExaltApp
RUN mvn clean install -Dmaven.test.skip

# Expose the API
EXPOSE 8080

# Launches the application
ENTRYPOINT ["java","-jar","target/bank-Exalt-Version.jar"]