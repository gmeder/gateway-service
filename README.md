# Run project locally

## Compile project

	$ mvn clean package

## Run App

	$ java -jar target/gateway-service-thorntail.jar -Slocal
	
# Run project on Openshift

## Set the homework project
 
	$ oc project assignment-gmeder

## Create configmap	

	$ oc create configmap gateway-service --from-file=etc/project-defaults.yml

## Deploy project	

	$ mvn clean fabric8:deploy -Popenshift -DskipTests
	
# Test on Openshift (Shared Cluster 3.11)

	$ curl http://gateway.assignment-gmeder.apps.na311.openshift.opentlc.com/gateway/freelancers

	$ curl http://gateway.assignment-gmeder.apps.na311.openshift.opentlc.com/gateway/freelancers/123

	$ curl http://gateway.assignment-gmeder.apps.na311.openshift.opentlc.com/gateway/projects

	$ curl http://gateway.assignment-gmeder.apps.na311.openshift.opentlc.com/gateway/projects/11111
	
	$ curl http://gateway.assignment-gmeder.apps.na311.openshift.opentlc.com/gateway/projects/status/open

