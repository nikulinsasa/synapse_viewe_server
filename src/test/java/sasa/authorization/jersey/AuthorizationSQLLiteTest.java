package sasa.authorization.jersey;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class AuthorizationSQLLiteTest {

	private ConnectionSource source;

	private AuthorizationSQLLite auth;

	public AuthorizationSQLLiteTest() throws SQLException {
		source = new JdbcConnectionSource("jdbc:sqlite:main.sqlite");
	}

	@Before
	public void setUp() throws AuthorizationException {
		auth = new AuthorizationSQLLite(source);
		try {
			auth.init();
		} catch (AuthorizationException e) {
		}
	}

	@Test
	public void test() throws AuthorizationException {
		TokenSQLLite token = auth.authorizate("test", "test");
		assertEquals(true, token != null);
		assertTrue(auth.isAuthorizated(token.getToken()));
		auth.TIMELIFE = 0;
		assertFalse(auth.isAuthorizated(token.getToken()));
		TokenSQLLite token2 = auth.regenerateToken(token.getRefreshToken());
		auth.TIMELIFE = 900000;
		assertEquals(token.getId(), token2.getId());
		assertNotEquals(token.getToken(), token2.getToken());
		assertFalse(auth.isAuthorizated(token.getToken()));
		assertTrue(auth.isAuthorizated(token2.getToken()));
		
	}

}
