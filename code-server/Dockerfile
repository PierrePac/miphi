FROM lscr.io/linuxserver/code-server:latest as code-server-test-candidats

# Setting HTTP and HTTPS proxy environment variables
ENV http_proxy http://jhurry.a:Mipih318051!@proxy.mipih.local:3128
ENV https_proxy http://jhurry.a:Mipih318051!@proxy.mipih.local:3128
ENV no_proxy dv02.mipih.net

# WORKDIR
WORKDIR /config/workspace

# WORKDIR
ENV WORKD="config/workspace"

# Installer les dépendances nécessaires (si elles ne sont pas déjà installées)
RUN apt-get update && \
    apt-get install -y git \
    openssh-client \
    ca-certificates \
    dnsutils \
    wget \
    openjdk-8-jdk \
    curl &&\
    rm -rf /var/lib/apt/lists/*

ENV MAVEN_VER="3.8.6"

# Définissez la variable d'environnement JAVA_HOME
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

# Définissez la variable d'environnement MAVEN_HOME
ENV MAVEN_HOME /usr/share/maven

# Ajoutez les binaires de Java et Maven dans le PATH
ENV PATH $PATH:$JAVA_HOME/bin:$MAVEN_HOME/bin

# Copiez votre code source dans le conteneur
COPY . /app

# Définissez le répertoire de travail
WORKDIR /app

# Télécharger les certificats du Mipih
RUN wget --no-check-certificate -q -Y off https://meteo.mipih.net/cdp/mipih-racine.pem -O /usr/local/share/ca-certificates/mipih-racine.crt
RUN wget --no-check-certificate -q -Y off https://meteo.mipih.net/cdp/mipih-cas5.pem   -O /usr/local/share/ca-certificates/mipih-cas5.crt
RUN wget --no-check-certificate -q -Y off https://meteo.mipih.net/cdp/mipih-cas6.pem   -O /usr/local/share/ca-certificates/mipih-cas6.crt

# Importer les certificats du mipih avec ceux du système
RUN update-ca-certificates

COPY config /config

# Copier la clé SSH privée dans l'image
COPY config/.ssh/id_rsa /root/.ssh/id_rsa

# COPY config/https-crt /config/https-crt

RUN mkdir -p ${MAVEN_HOME}

COPY config/maven/3.8.6 ${MAVEN_HOME}

# Make ./mvn executable
RUN chmod +x "${MAVEN_HOME}/bin/mvn"

# Add the paths to the existing PATH variable
ENV PATH="${MAVEN_HOME}/bin:${PATH}"

ENV MAVEN ${MAVEN_HOME}/bin/mvn



# Configurer SSH pour ignorer la vérification de l'hôte
# RUN echo "Host gitlab.mipih.net\n\tStrictHostKeyChecking no\n" >> /root/.ssh/config

# RUN ssh -v git@gitlab.mipih.net


# Cloner le dépôt
# RUN ssh-agent bash -c 'ssh-add /root/.ssh/id_rsa; git clone git@gitlab.mipih.net:32022/prodige/hors-carto/entretien.git'


# RUN ssh-keyscan -H gitlab.mipih.net >> ~/.ssh/known_hosts

# RUN git clone ssh://git@gitlab.mipih.net:32022/prodige/hors-carto/entretien.git --branch complet