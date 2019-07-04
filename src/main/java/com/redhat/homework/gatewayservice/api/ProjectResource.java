package com.redhat.homework.gatewayservice.api;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import com.redhat.homework.projectservice.client.api.ProjectApi;
import com.redhat.homework.projectservice.client.invoker.ApiClient;
import com.redhat.homework.projectservice.client.invoker.ApiException;
import com.redhat.homework.projectservice.client.model.Project;

@ApplicationScoped
@Path("/gateway")
public class ProjectResource {

	@Inject
	@ConfigurationValue("projects.service.url")
	private String projectsUrl;

	private ProjectApi api;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/projects")
	public List<Project> projects() throws ApiException {
		buildClient();
		return api.getProjects();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/projects/{projectId}")
	public Response projectsById(@PathParam("projectId") Integer id) {
		buildClient();
		
		try {
			Project project = api.getProjectById(id);
			return Response.ok(project).build();
		} catch (ApiException e) {
			if (e.getCode() > 499) {
				throw new RuntimeException("Internal error", e);
			} else {
				return Response.status(e.getCode()).build();
			}

		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/projects/status/{status}")
	public Response projectsByStatus(@PathParam("status") String status) {
		buildClient();
		try {
			List<Project> projects = api.getProjectsByStatus(status);
			return Response.ok(projects).build();
		} catch (ApiException e) {
			if (e.getCode() > 499) {
				throw new RuntimeException("Internal error", e);
			} else {
				return Response.status(e.getCode()).build();
			}

		}
	}

	private void buildClient() {
		if (projectsUrl == null) {
			throw new IllegalArgumentException("projects.service.url not set");
		}
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(projectsUrl);
		api = new ProjectApi(apiClient);
	}

}
