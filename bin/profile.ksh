#!/bin/ksh

APP_NAME=springboot-demo
APP_VERSION=v1.0.0
APP_FULL_NAME=${APP_NAME}:${APP_VERSION}

ROOT_DIR=/opt/${APP_NAME}
IMAGE_DIR=${ROOT_DIR}/image
IMAGE_NAME=${APP_NAME}-${APP_VERSION}.tar

# create IMAGE_DIR if it doesn't exist
if [ ! -d "${IMAGE_DIR}" ]; then
    mkdir -m 775 -p "${IMAGE_DIR}"
fi
