# Service documentation

## Service design

The project is just a gateway for project and freelancer service. It has an API endpoint and resources that uses swagger codegen client based on the sagger files for freelancer and project

- FreelancerResouce: JAX RS Rest API that calls freelancer api client
- ProjectResouce: JAX RS Rest API that calls project api client

## Service definition

The service uses swagger codegen client so no need to maintain a model layer in the gateway.

The sevice client jar are installed locally and added to the pom file

		<dependency>
			<groupId>com.redhat</groupId>
			<artifactId>freelancer-api-client </artifactId>
			<version>1.0.0</version>
		</dependency>

		<dependency>
			<groupId>com.redhat</groupId>
			<artifactId>project-api-client </artifactId>
			<version>1.0.0</version>
		</dependency>


To refresh service client implementation from yaml, install swagger codegen (https://swagger.io/tools/swagger-codegen/) and run the following commands:

### Generate new source code (freelancer)

    $ swagger-codegen generate \
        -i https://raw.githubusercontent.com/gmeder/freelancer-service/master/src/main/resources/swagger.yaml \
        --api-package com.redhat.homework.freelancerservice.client.api \
        --model-package com.redhat.homework.freelancerservice.client.model \
        --invoker-package com.redhat.homework.freelancerservice.client.invoker \
        --group-id com.redhat \
        --artifact-id freelancer-api-client \
        --artifact-version 1.0.0 \
        -l java \
        -o freelancer-api-client

### Install the client in your local maven M2 repo

    $ cd freelancer-api-client
    $ mvn clean install

### Generate new source code (project)

    $ swagger-codegen generate \
        -i https://raw.githubusercontent.com/gmeder/project-service/master/src/main/resources/swagger.yaml \
        --api-package com.redhat.homework.projectservice.client.api \
        --model-package com.redhat.homework.projectservice.client.model \
        --invoker-package com.redhat.homework.projectservice.client.invoker \
        --group-id com.redhat \
        --artifact-id project-api-client \
        --artifact-version 1.0.0 \
        -l java \
        -o project-api-client


### Install the client in your local maven M2 repo

    $ cd project-api-client
    $ mvn clean install


## Run service

How to run and test the service is described in README (Openshift and local)

## Service info

- Service is running on port 8080
- Service connect to mongodb *localhost* on port *27017* with creds *mongo/mongo* and database *projectdb*
- The project has a route defined for Openshift (gateway-service-homework.[OPENSHIFT_DOMAIN])
