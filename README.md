# HR

This is a sample spring application that uses the Oracle HR Schema.

# How to Run

## Prerequisites

For running and developing:

* [Maven](https://maven.apache.org/install.html)
* [Docker](https://docs.docker.com/get-docker/)
* [Docker Compose](https://docs.docker.com/compose/install/)

For deploying:

* [kubectl](https://kubernetes.io/docs/tasks/tools/)

## Environment

Place your environment variables in the `.env` file. An example, `.env.example` is supplied and can be copied:

```bash
# copy the example
cp .env.example .env
# add your envs to .example.env`
```

To build:

```bash
bash docker-build.sh
```

To run locally on docker:

```bash
# this assumes you have a dp running locally
bash docker-run.sh
```

To tag and push a release to the OCI Container Registry:

```bash
# source your environment variables
source .env
# syntax:
# bash tag-and-push.sh major.minor.patch $REGION $TENANCY_NAMESPACE

# example: 
bash tag-and-push.sh 0.1.1 $REGION $TENANCY_NAMESPACE
```

## Database

Simply build the database from the `docker-compose.yml`.

```bash
docker-compose up -d oracle-database-xe-18400-slim
```

A PDB will be created with the name `ORCLPDB1`. This will run the startup scripts located
in `src/main/resources/db/startup`.

To connect via [SQLcl](https://github.com/trangelier/sqlcl-install):

```bash
# SYS
sql sys/password@localhost:1521/ORCLPDB1 as sysdba
# HR Schema
sql hr/super-secret-password@localhost:1521/ORCLPDB1
```

## Spring

This is a maven project and includes the maven wrapper. Simply use the spring-boot maven command:

```
./mvnw spring-boot:run
```

Each time your run the Liquibase migrations will run if required. This includes seed data.

Or through Docker

```bash
./docker-build.sh
./docker-run.sh
```

Or through docker-compose

```bash
docker-compose up spring-hr
```

# Deployment

## New Deployment

Login to private container registry:

```bash
docker login <region-key>.ocir.io
Username: <container-registry-namespace>/oracleidentitycloudservice/<username>
Password: <auth-token>
```

If this is a new repository, create it with this command:

```bash
oci artifacts container repository create --display-name oci-dev/<app-name> --compartment-id <compartment-id>
```

Provide the login credentials to
kubernetes. [More information.](https://docs.oracle.com/en-us/iaas/Content/ContEng/Tasks/contengpullingimagesfromocir.htm)

```bash
kubectl create secret docker-registry <secret-name> --docker-server=<region-key>.ocir.io --docker-username='<tenancy-namespace>/<oci-username>' --docker-password='<oci-auth-token>' --docker-email='<email-address>'
```

Create a secret for the database url and password:

```bash
kubectl create secret generic hr-db-url-pass \
	--from-literal=url=<url> \
	--from-literal=password=<password>
```

This repository assumes you are using Oracle Autonomous Database (ATP) in OCI. Download your Oracle Wallet for your
database and place its contents in `src/main/resources/db/wallet`. The Docker build will pickup this and put it in the
container.

## Build & Push

Now, build, tag, push to the registry.
See [OCI's documentation](https://docs.oracle.com/en-us/iaas/Content/Registry/Tasks/registrypushingimagesusingthedockercli.htm)
for more help.

```bash
# build the image
bash docker-build.sh
# tag image & push
bash tag-and-push.sh <major.minor.patch>
# Example:
# bash tag-and-push.sh 0.1.1 $REGION $TENANCY_NAMESPACE
```

Once your new version is pushed to the registry, deploy:

```bash
kubectl apply -f deployment/app.yaml
kubectl apply -f deployment/ingress-route.yaml
```