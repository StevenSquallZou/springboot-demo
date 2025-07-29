#!/bin/ksh

. bin/profile.ksh

cd ${ROOT_DIR}

# use buildah to build Dockerfile to image
buildah build -t localhost/${APP_NAME}:${APP_VERSION} -f Dockerfile . && buildah rmi -p

# remove the old image tar file, convert the image file to a tar file
rm -f ${IMAGE_DIR}/${IMAGE_NAME}
buildah push localhost/${APP_FULL_NAME} docker-archive:${IMAGE_DIR}/${IMAGE_NAME}

# import the image tar file to containerd repo
sudo ctr -n=k8s.io images import ${IMAGE_DIR}/${IMAGE_NAME}
