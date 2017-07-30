package sasa.authorization.jersey.controllers;

import javax.inject.Inject;
import javax.naming.AuthenticationException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.JSONObject;

import sasa.authorization.jersey.AuthorizationSQLLite;
import sasa.authorization.jersey.TokenSQLLite;

@Path("/auth")
public class Authorization {

	@Inject
	private AuthorizationSQLLite auth;

	@POST
	@Produces("text/json")
	public String login(String body) {
		JSONObject answer = new JSONObject();
		try {
			JSONObject obj = new JSONObject(body);

			if (!obj.has("user") || !obj.has("password")) {
				throw new AuthenticationException("Непереданы поля имя пользователия или пароль");
			}
			
			TokenSQLLite token = auth.authorizate(obj.getString("user"), obj.getString("password"));

			answer.put("token", token.getToken());
			answer.put("refresh_token", token.getRefreshToken());

		} catch (AuthenticationException e) {
			answer.put("error", "Ошибка авторизации " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			answer.put("error", "Неожиданная ошибка");
		}
		return answer.toString();
	}

	@GET
	@Path("refresh/{refreshToken}")
	@Produces("text/json")
	public String refresh(@PathParam("refreshToken") String refreshToken) {
		JSONObject answer = new JSONObject();
		try {
			TokenSQLLite token = auth.regenerateToken(refreshToken);
			answer.put("token", token.getToken());
			answer.put("refresh_token", token.getRefreshToken());
		} catch (Exception e) {
			answer.put("error", "Неожиданная ошибка");
		}
		return answer.toString();
	}

	@DELETE
	@Path("logout/{token}")
	@Produces("text/json")
	public String logout(@PathParam("token") String token) {
		JSONObject answer = new JSONObject();
		try {
			auth.logout(token);
			answer.put("msg", "Вы успешно вышли");
		} catch (Exception e) {
			answer.put("error", "Неожиданная ошибка");
		}
		return answer.toString();
	}
	
}
