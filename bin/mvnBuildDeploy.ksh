#!/bin/ksh

. ${ROOT_DIR}/bin/profile.ksh

# maven build
cd ${ROOT_DIR}
mvn clean install

# image build and deploy
. ${ROOT_DIR}/bin/buildDeploy.ksh
