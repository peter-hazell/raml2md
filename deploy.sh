#!/usr/bin/env bash

PRIVATE_KEY=~/petehazell.pem
SERVICE_NAME=petehazell-raml2md
SERVICE_VERSION=1.0-SNAPSHOT
PROD_CONFIG=conf/prod.conf
EC2=ubuntu@ec2-3-10-104-53.eu-west-2.compute.amazonaws.com

sbt clean compile dist

chmod 664 ${PROD_CONFIG}

scp -i ${PRIVATE_KEY} target/universal/${SERVICE_NAME}-${SERVICE_VERSION}.zip ${EC2}:/home/ubuntu/${SERVICE_NAME}.zip

scp -i ${PRIVATE_KEY} ${PROD_CONFIG} ${EC2}:/home/ubuntu/${SERVICE_NAME}.conf

ssh -i ${PRIVATE_KEY} ${EC2} << EOF
sudo kill -9 \$(cat ${SERVICE_NAME}-${SERVICE_VERSION}/RUNNING_PID)
sudo rm ${SERVICE_NAME}-${SERVICE_VERSION}/RUNNING_PID
sudo unzip -o ${SERVICE_NAME}.zip
sudo rm ${SERVICE_NAME}.zip
sudo ./${SERVICE_NAME}-${SERVICE_VERSION}/bin/${SERVICE_NAME} -Dconfig.file=${SERVICE_NAME}.conf &
EOF