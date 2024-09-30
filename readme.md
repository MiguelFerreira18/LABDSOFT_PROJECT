# Smart Cities

## Technologies

- Java (Springboot)
- Dart (Flutter)
- Golang
- Mysql
- OPen AI API
- Docker
- Kubernetes

## How to run the project

### There are 3 ways

1. Only run the api and database to test the api
2. Run the Docker-compose file
3. Run the Kubernetes cluster

<details><summary>Tools Needed</summary>

**_golang_**

Follow the steps on this [link](https://go.dev/doc/install)

**_Docker_**

- For [Linux](https://docs.docker.com/desktop/install/linux/)
- For [Mac](https://docs.docker.com/desktop/install/mac-install/)
- For [windows](https://docs.docker.com/desktop/install/windows-install/)

**_Kubernetes_**

- For [Linux](https://kubernetes.io/docs/tasks/tools/install-kubectl-linux/)
- For [Mac](https://kubernetes.io/docs/tasks/tools/install-kubectl-macos/)
- For [windows](https://kubernetes.io/docs/tasks/tools/install-kubectl-windows/)

**_Minikube_**

- [Here](https://minikube.sigs.k8s.io/docs/start/?arch=%2Fwindows%2Fx86-64%2Fstable%2F.exe+download)

**_Flutter_**

- Follow this [link](https://docs.flutter.dev/get-started/install) choose your OS and then android

</details>

**The first way**

> **NOTE**
> You will need to [install golang](https://go.dev/doc/install)

```bash
mkdir ../TempDatabase
mv ./template.env ../TempDatabase/.env
mv ./database-docker-compose.yml ../TempDatabase/docker-compose.yml
cd ../TempDatabase
docker-compose --env-file up
```


