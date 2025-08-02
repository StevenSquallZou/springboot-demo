#!/bin/ksh

. bin/profile.ksh

# deploy chart to K8s
helm uninstall ${RELEASE_NAME}
helm install ${RELEASE_NAME} ${CHART_DIR}
