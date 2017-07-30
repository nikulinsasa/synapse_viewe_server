package sasa.authorization.jersey;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

@Security
public class SecurityFilter implements ContainerRequestFilter {

	@Inject
	private AuthorizationSQLLite auth;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String token = requestContext.getHeaderString("Authorization");
		System.out.println(token);
		try {
			if(token==null || !auth.isAuthorizated(token)){
				requestContext.abortWith(Response
	                    .status(Response.Status.UNAUTHORIZED)
	                    .entity("User cannot access the resource.")
	                    .build());
			}
		} catch (AuthorizationException e) {
			e.printStackTrace();
			requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Fatal error")
                    .build());
		}
	}

}
