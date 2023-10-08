# Use an image that includes the Docker client (Docker CLI)
FROM docker:24.0.6 as docker-cli

ENV http_proxy ""
ENV https_proxy ""
ENV HTTP_PROXY ""
ENV HTTPS_PROXY ""
ENV no_proxy "*"
ENV NO_PROXY "*"

# Utilisation de l'image de base Maven avec OpenJDK 17 pour la phase de build
FROM maven:3.8.3-openjdk-17 AS build

# Setting HTTP and HTTPS proxy environment variables
ENV http_proxy http://bouzaggou.a:Rep.135tazor@proxy.mipih.local:3128
ENV https_proxy http://bouzaggou.a:Rep.135tazor@proxy.mipih.local:3128
ENV no_proxy dv02.mipih.net

# Répertoire de travail dans le conteneur
WORKDIR /app

# Copie du fichier POM et des sources
COPY pom.xml .
COPY src ./src

ARG MAVEN_OPTS

# Run build
RUN mvn clean package -DskipTests

# Utilisation de l'image Tomcat avec OpenJDK 17 pour la phase de déploiement
FROM tomcat:10-jdk17-openjdk-slim

# Suppression des applications par défaut de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copie du fichier WAR dans le conteneur
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Copy the Docker CLI from the docker-cli image to the final image
COPY --from=docker-cli /usr/local/bin/docker /usr/local/bin/docker

# Installation de docker-compose plugin
RUN /usr/bin/apt-get update && /usr/bin/apt-get install -y curl
RUN DOCKER_CONFIG=${DOCKER_CONFIG:-$HOME/.docker}
RUN mkdir -p $DOCKER_CONFIG/cli-plugins
RUN curl -SL https://github.com/docker/compose/releases/download/v2.20.3/docker-compose-linux-x86_64 -o $DOCKER_CONFIG/cli-plugins/docker-compose
RUN chmod +x $DOCKER_CONFIG/cli-plugins/docker-compose                                                                                              

ENV http_proxy ""
ENV https_proxy ""
ENV HTTP_PROXY ""
ENV HTTPS_PROXY ""
ENV no_proxy "*"
ENV NO_PROXY "*"

# Exposition du port 8080
EXPOSE 8080

# Démarrage de Tomcat
CMD ["catalina.sh", "run"]
