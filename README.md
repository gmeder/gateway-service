# Run project locally

## Compile project

	$ mvn clean package

## Run App

	$ java -jar target/gateway-service-thorntail.jar -Slocal
	
# Run project on Openshift

## Set the homework project
 
	$ oc project homework

## Create configmap	

	$ oc create configmap gateway-service --from-file=etc/project-defaults.yml

## Deploy project	

	$ mvn clean fabric8:deploy -Popenshift -DskipTests
	
# Test on Openshift

	$ curl http://gateway-service-homework.[OPENSHIFT_LOCAL_SUBDOMAIN]/gateway/freelancers

	$ curl http://gateway-service-homework.[OPENSHIFT_LOCAL_SUBDOMAIN]/gateway/freelancers/123

	$ curl http://gateway-service-homework.[OPENSHIFT_LOCAL_SUBDOMAIN]/gateway/projects

	$ curl http://gateway-service-homework.[OPENSHIFT_LOCAL_SUBDOMAIN]/gateway/projects/11111
	
	$ curl http://gateway-service-homework.[OPENSHIFT_LOCAL_SUBDOMAIN]/gateway/freelancers/status/open

