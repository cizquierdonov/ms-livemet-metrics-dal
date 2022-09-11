[![N|Solid](https://design.jboss.org/quarkus/logo/final/PNG/quarkus_logo_horizontal_rgb_200px_default.png)](https://quarkus.io/)
# ms-livemet-metrics-dal

Backend REST microservices that represents the data access layer to manage metric types and posts entities in a PostgreSQL database and implements all business logic. These software are implemented with the next tools & technologies:

### For localhost compilation & packaging

OpenJDK (Java Development Kit) version 11.0.15 for Windows x64

### For containerized compilation & packaging

- GraalVM JDK (Java Development Kit)
- Docker image: quay.io/quarkus/ubi-quarkus-native-image:22.0-java11

### For dependencies management, packaging & deploy

Apache Maven version 3.8.1

### Development Framework

Quarkus version 2.9.2. For more information, please check [https://quarkus.io](https://quarkus.io)

### Database Engine

PostgreSQL version 14

## Public access to REST service in Google Cloud

Live Metrics DAL microservice is deployed and published in the following URL: [https://ms-livemet-metrics-dal-6rwr6t3zia-uc.a.run.app](https://ms-livemet-metrics-dal-6rwr6t3zia-uc.a.run.app)

The next services were be used to deploy application in Google Cloud:

- Google Cloud Run - PaaS to deploy containerized applications in a serverless infraestructure
- Google Cloud SQL - PaaS to manage relational database engine
- Google Cloud IAM - SaaS to manage identity & access in Google

## Pre-requisites to localhost

If you want to install in localhost, you need the following tools/configurations:

1) Install or Download portable version of OpenJDK 11

2) Configure `JAVA_HOME` environment variable, setting OpenJDK 11 root folder

3) Add new entry in `PATH` environment variable, setting OpenJDK 11 `bin` folder (recommended use JAVA_HOME variable to add this new entry)

- Example in Windows: %JAVA_HOME%\bin
- Example in Unix: $JAVA_HOME/bin

## Step by step to install and run backend application localhost

Run the following commands to download, install and run the application:

1) `git clone https://gitlab.com/cizquierdonov/live-metrics/ms-livemet-metrics-dal.git`

2) `cd ms-livemet-metrics-dal/`

3) `./mvnw compile quarkus:dev`

REST operations are available to execute in [http://localhost:8080](http://localhost:8080)

## REST Operations & OpenAPI Specification 
[![N|Solid](https://img.stackshare.io/service/3417/thumb_retina_pIea9Ji0.png)](https://quarkus.io/)

You can understand and call API operations, request & response specifications using any Swagger or OpenAPI client.
It is recommended to use the https://editor.swagger.io/ website and copy and paste the content of the `./openapi.yaml` file (inside GitLab repository) on the page.

You can try calling the operations from there selecting https://ms-livemet-metrics-dal-6rwr6t3zia-uc.a.run.app/livemet/metrics-dal/v1 server.

For more information about how to use and execute REST operations, please download and import in Postman the collection file `./postman/Live Metrics.postman_collection.json` inside GitLab repository.

## Learn More

For more information about architecture and integrations, please visit [https://app.diagrams.net/#G1HGe_iqgwu4FQwDNBW-1FL0MTv3BXDk3R](https://app.diagrams.net/#G1HGe_iqgwu4FQwDNBW-1FL0MTv3BXDk3R) to check database E-R diagram
and Architecture diagram.
