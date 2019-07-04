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

import com.redhat.homework.freelancerservice.client.api.FreelancerApi;
import com.redhat.homework.freelancerservice.client.invoker.ApiClient;
import com.redhat.homework.freelancerservice.client.invoker.ApiException;
import com.redhat.homework.freelancerservice.client.model.Freelancer;


@ApplicationScoped
@Path("/gateway")
public class FreelancerResource {

	@Inject
	@ConfigurationValue("freelancer.service.url")
	private String freelancerUrl;

	private FreelancerApi api;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/freelancers")
	public List<Freelancer> freelancers() throws ApiException {
		buildClient();
		return api.getFreelancers();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/freelancers/{id}")
	public Response freelancersById(@PathParam("id") Integer id) {
		buildClient();
		
		try {
			Freelancer project = api.getFreelancersById(id);
			return Response.ok(project).build();
		} catch (ApiException e) {
			if (e.getCode() > 499) {
				throw new RuntimeException("Internal error", e);
			} else {
				return Response.status(e.getCode()).build();
			}

		}
	}

	private void buildClient() {
		if (freelancerUrl == null) {
			throw new IllegalArgumentException("freelancer.service.url not set");
		}
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(freelancerUrl);
	    api = new FreelancerApi(apiClient);
	}

}
