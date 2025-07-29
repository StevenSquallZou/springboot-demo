#!/bin/ksh

. bin/profile.ksh

# maven build
cd ${ROOT_DIR}
mvn clean install

# image build
. ${ROOT_DIR}/bin/imageBuild.ksh

# deploy chart to K8s
helm install ${RELEASE_NAME} ${CHART_DIR}
