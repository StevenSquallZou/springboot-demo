#!/bin/ksh

. bin/profile.ksh

# maven build
cd ${ROOT_DIR}
mvn clean install

# image build
. ${ROOT_DIR}/bin/imageBuild.ksh

# deploy to K8s
kubectl apply -f ${ROOT_DIR}/k8s
