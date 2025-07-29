#!/bin/ksh

. ./profile.ksh

# maven build
cd ${ROOT_DIR}
mvn clean install

# image build and deploy
. ./buildDeploy.ksh
