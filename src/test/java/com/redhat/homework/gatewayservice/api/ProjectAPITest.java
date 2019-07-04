package com.redhat.homework.gatewayservice.api;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
public class ProjectAPITest {
	
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
    public void testGetProjects() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("/gateway").path("/projects");

        Response response = target.request(MediaType.APPLICATION_JSON).get();
        JsonArray value = Json.parse(response.readEntity(String.class)).asArray();
        assertThat(response.getStatus(), equalTo(Integer.valueOf(200)));
        assertThat(value.size(),equalTo(3));
        
        Map<String,JsonObject> results = new HashMap<>();
        
        value.forEach(project -> {
        	results.put(project.asObject().get("projectId").asString(), project.asObject());
        });
        
        assertTrue(results.containsKey("11111"));
        assertTrue(results.containsKey("22222"));
        assertTrue(results.containsKey("33333"));
        
    }
    
    @Test
    @RunAsClient
    public void testGetProjectById() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("/gateway").path("/projects/11111");

        Response response = target.request(MediaType.APPLICATION_JSON).get();
        JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
        assertThat(response.getStatus(), equalTo(Integer.valueOf(200)));
        assertThat(value.get("projectId").asString(),equalTo("11111"));
        assertThat(value.get("ownerFirstName").asString(),equalTo("John"));
    }
    
    @Test
    @RunAsClient
    public void testGetProjectByExisitingStatus() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("/gateway").path("/projects/status/open");

        Response response = target.request(MediaType.APPLICATION_JSON).get();
        JsonArray value = Json.parse(response.readEntity(String.class)).asArray();
        assertThat(value.size(),equalTo(1));
        assertThat(response.getStatus(), equalTo(Integer.valueOf(200)));
        assertThat(value.get(0).asObject().get("projectId").asString(),equalTo("11111"));
        assertThat(value.get(0).asObject().get("ownerFirstName").asString(),equalTo("John"));
    }
   
    @Test
    @RunAsClient
    public void testGetProjectByNonExistingStatus() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("/gateway").path("/projects/status/s1");

        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(Integer.valueOf(404)));


    }

}
