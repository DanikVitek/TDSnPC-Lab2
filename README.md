# How to create an image
```shell
cd <TDSnPC path>/CommentService
docker build -t <username>/comment-service .
cd ../IdentityService
docker build -t <username>/identity-service .
cd ../PluginService
docker build -t <username>/plugin-service .
cd ../
docker push <username>/comment-service
docker push <username>/identity-service
docker push <username>/plugin-service
```

# How to deploy to minikube and run
```shell
cd <TDSnPC path>
minikube start --driver=hyperv
kubectl apply -f mariadb-workloads.yaml
kubectl apply -f CommentService/workloads.yaml
kubectl apply -f IdentityService/workloads.yaml
kubectl apply -f PluginService/workloads.yaml
minikube service comment-service --url
minikube service identity-service --url
minikube service plugin-service --url
```