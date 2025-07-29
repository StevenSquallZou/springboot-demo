#!/bin/ksh

. ./profile.ksh

# maven build
cd ${REPO_DIR}
mvn clean install

# image build and deploy
. ./buildDeploy.ksh
