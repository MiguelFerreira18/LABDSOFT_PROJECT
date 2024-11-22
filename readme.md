# Smart Cities

## Technologies

- Java (Springboot)
- VueJS (IonicFramework)
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

<details><summary><b>Tools Needed</b></summary>

**_Maven_**

- Download the zip or tar depending on your OS [link](https://maven.apache.org/download.cgi)
- If you are on linux/Mac follow this [link](https://www.digitalocean.com/community/tutorials/install-maven-linux-ubuntu)
- If you are on windows follow this [link](https://phoenixnap.com/kb/install-maven-windows)

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

**_Ionic_**

- Follow this [link](https://ionicframework.com/docs/intro/cli) choose your OS and then android

</details>

<br>

## **The first way**

> **NOTE:** You will need to [install golang](https://go.dev/doc/install)

> **NOTE:** You will need to [install the maven](https://maven.apache.org/download.cgi) cli

> **NOTE:** You will need to have **docker** installed

Execute the script to create a temporary folder on your system just before your current project. Don't go into the scripts folder, execute the go command in the home directory of the project

```bash
go run ./scripts/create_database_folder.go
```

<details>What is happening

```bash
## Do this if you haven't created the .env file
cp ./template.env .env
## Create a new folder where you will run the docker with the database
mkdir ../TempDatabase
## Copy the .env file to where you are going to run the docker file
cp ./template.env ../TempDatabase/.env
# Copy the database docker file into the folder
cp ./database-docker-compose.yml ../TempDatabase/docker-compose.yml
cd ../TempDatabase
# Execute the docker compose file
docker-compose --env-file up
```

</details>

### Run backend

After that setup just run the script in the /scripts file

```bash
go run ./scripts/run_java_api.go
```

### Run Frontend

```bash
cd public
ionic serve
```

## **The Second Way**

Use this if you want to test the system as a whole (might break)

```bash
cp ./template.env .env # if you dont have it yet
docker-compose --env-file up
```

## **The Third Way**

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

**_(Optional) Minikube dashboard_**

```bash
Minikube dashboard
```

After that you can access every service.

<details><summary>Extra for kubernetes</summary>
If you don't want to use minikube dashboard, you can just use the kubectl cli

**To check the nodes**

```bash
kubectl get nodes -o wide
```

or if you want only one

```bash
kubectl get nodes <NODE_NAME>
```

<br>

**To check the services**

```bash
kubectl get services -o wide
```

or if you want only one

```bash
kubectl get services <SERVICE_NAME>
```

</details>

## Trouble Running

- If you are having problems of any kind, talk to the QA dev :)
- If You Don't want to rerun docker everytime you do a change in the API, run the database as normal with docker, and after this do the following:
  1. Go to application.properties in the resources folder, and change all the database fields manually the envs are in the .env file
  2. Change the active profile in the application properties from prod to dev `spring.profiles.active=dev`
  3. Change the application-dev.properties single line to this : `spring.datasource.url=jdbc:mysql://127.0.0.1:3306/data_labdsoft`, if it doesnt work copy and paste it in the application.properties and that should override the dev configs
  4. You should be able to run the API without docker
- Always run the database before starting the API
- If you have done your implementation and you are running the docker-compose and it doesn't work check which profile got activated
- Always make sure you have the .env file in the root directory of the project
- If By any chance you decide to use the mapstrcut dependency in the api and are getting errors, clean use the maven clean command `mvn clean`, if this doesn't solve your issue, stop using that god forgotten library
- If you have none of the problems listed here go to the first point :)

# Check Health API

Once the application is running, you can access the health check at:

```bash
http://localhost:9091/actuator/health
```

# Next step: Creating the pipeline

- Test both FrontEnd(if we have tests) and Spring API
- Send the FrontEndand api to docker hub
- Create a release of there is a stable version

just testing out stuff
