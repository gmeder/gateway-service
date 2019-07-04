package com.redhat.homework.gatewayservice.api;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.redhat.homework.gatewayservice.RestApplication;

@RunWith(Arquillian.class)
public class FreelancerAPITest {
	
    private static String port = "8080";
    
    private Client client;
    
    @Deployment
    public static Archive<?> createDeployment() {
    
       
    	PomEquippedResolveStage resolver = Maven.resolver()
                .loadPomFromFile("pom.xml");
    	
    	File[] freelancerDeps = resolver
    			.resolve("com.redhat:freelancer-api-client:1.0.0")
    			.withTransitivity()
    			.asFile();
    	
    	File[] projectDeps = resolver
    			.resolve("com.redhat:project-api-client:1.0.0")
    			.withTransitivity()
    			.asFile();
    	
    	
        WebArchive war =  ShrinkWrap.create(WebArchive.class, "gateway-service.war")
                .addPackages(true, RestApplication.class.getPackage())
                .addAsResource("project-local.yml", "project-defaults.yml")
                .addAsLibraries(freelancerDeps)
                .addAsLibraries(projectDeps);

        return war;
    }

    @Before
    public void before() throws Exception {
        client = ClientBuilder.newClient();
    }

    @After
    public void after() throws Exception {
        client.close();
    }
    
    @Test
    @RunAsClient
    public void testGetFreelancers() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("/gateway").path("/freelancers");

        Response response = target.request(MediaType.APPLICATION_JSON).get();
        JsonArray value = Json.parse(response.readEntity(String.class)).asArray();
        assertThat(response.getStatus(), equalTo(Integer.valueOf(200)));
        assertThat(value.size(),equalTo(1));
        assertThat(value.get(0).asObject().get("freelancerId").asInt(),equalTo(123));
        
    }
    
    @Test
    @RunAsClient
    public void testGetFreelancerById() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("/gateway").path("/freelancers/123");

        Response response = target.request(MediaType.APPLICATION_JSON).get();
        JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
        assertThat(response.getStatus(), equalTo(Integer.valueOf(200)));
        assertThat(value.get("freelancerId").asInt(),equalTo(123));
        assertThat(value.get("firstName").asString(),equalTo("john"));
        assertThat(value.get("lastName").asString(),equalTo("doe"));
        
    }
    
    @Test
    @RunAsClient
    public void testGetNonExistingFreelancerById() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("/gateway").path("/freelancers/000");

        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(Integer.valueOf(404)));

        
    }


}
