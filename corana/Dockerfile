FROM ubuntu:18.04

#############################################################################
# Setup base image 
#############################################################################
RUN \
  apt-get update -y && \
  apt-get install software-properties-common -y && \
  apt-get install apt-utils && \
  apt-get install -y openjdk-11-jdk  && \
  apt-get update -y && \
  apt-get install -y git \
                build-essential \
                python \
                wget \
                antlr3 \
                && \
  rm -rf /var/lib/apt/lists/* 

#############################################################################
# Environment 
#############################################################################

# Set java env
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64

# Home dir
RUN mkdir /home/project
ENV PROJ_DIR /home/project
WORKDIR ${PROJ_DIR}

# Install objdump
RUN apt-get update -y
RUN apt-get install binutils-arm-none-eabi

# Install Z3 
RUN apt-get update -y && apt-get install -y z3

# Clone github
RUN git clone https://github.com/vananhnt/corana.git
ENV CORANA ${PROJ_DIR}/corana

WORKDIR ${CORANA}/libs/capstone
RUN ./make.sh 
RUN ./make.sh install

# Install mongodb
WORKDIR ${PROJ_DIR}
RUN wget -qO - https://www.mongodb.org/static/pgp/server-4.4.asc | apt-key add -
RUN echo "deb http://repo.mongodb.org/apt/debian stretch/mongodb-org/4.4 main" | tee /etc/apt/sources.list.d/mongodb-org-4.4.list
RUN apt-get install -y mongodb

RUN service mongodb start

# CORANA/API
WORKDIR ${CORANA}


