#!/bin/ksh

. bin/profile.ksh

# maven build
cd ${ROOT_DIR}
mvn clean install

# image build
. ${ROOT_DIR}/bin/imageBuild.ksh

# deploy chart to K8s
. ${ROOT_DIR}/bin/reinstallChart.ksh
