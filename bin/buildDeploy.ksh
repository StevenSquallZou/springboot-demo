# use buildah to build Dockerfile to image
buildah build -t localhost/springboot-demo:v1.0.0 -f Dockerfile . && buildah rmi -p

# remove the old image tar file, convert the image file to a tar file
rm -f /opt/springboot-demo/image/springboot-demo_v1.0.0.tar
buildah push localhost/springboot-demo:v1.0.0 docker-archive:/opt/springboot-demo/image/springboot-demo_v1.0.0.tar

# import the image tar file to containerd repo
sudo ctr -n=k8s.io images import /opt/springboot-demo/image/springboot-demo_v1.0.0.tar

# deploy
kubectl apply -f /opt/springboot-demo/repo/springboot-demo/k8s
