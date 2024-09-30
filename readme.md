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

**_Maven_**

Download the zip or tar depending on your OS [link](https://maven.apache.org/download.cgi)

If you are on linux/Mac follow this [link](https://www.digitalocean.com/community/tutorials/install-maven-linux-ubuntu)

If you are on windows follow this [link](https://phoenixnap.com/kb/install-maven-windows)


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
> And you will need to [install the maven](https://maven.apache.org/download.cgi) cli

```bash
mv ./template.env .env
mkdir ../TempDatabase
mv ./template.env ../TempDatabase/.env
mv ./database-docker-compose.yml ../TempDatabase/docker-compose.yml
cd ../TempDatabase
docker-compose --env-file up
```

After that setup just run the script in the /scripts file

```bash
go run set_envs.go # This will change
```

**The Second Way**

Use this if you want to test the system as a whole (might break)


```bash
mv ./template.env .env # if you dont have it yet
docker-compose --env-file up
```
**The Third Way**
> Note Check the version that is on dockerhub for the flutter ui and spring api, as they need a release for each new change
> This should only be used to test if the app can scale and the connections are working

First Start Minikube
```bash
Minikube start
```

Then open a tunnel to make it easier to interact with the app
```bash
Minikube Tunnel
```

Open the kubernetes folder write the following command, this is going to apply all the files there
```bash
kubectl apply -f .
```

If there is the need to make a change remove the folder/folders with
```bash
kubectl delete -f .
```
> Note this should be done **BEFORE** updating the yaml files

***(Optional) Minikube dashboard***
```bash
Minikube dashboard
```
