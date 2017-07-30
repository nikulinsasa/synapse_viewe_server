package sasa.authorization.jersey;

import java.security.NoSuchAlgorithmException;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class AuthorizationSQLLite {

	private ConnectionSource source;

	public int TIMELIFE = 900000*10000;

	public AuthorizationSQLLite(ConnectionSource source) {
		this.source = source;
	}

	public void init() throws AuthorizationException {
		try {
			TableUtils.createTableIfNotExists(source, UserSQLLite.class);
			TableUtils.createTableIfNotExists(source, TokenSQLLite.class);

			Dao<TokenSQLLite, String> dao = DaoManager.createDao(source, TokenSQLLite.class);
			dao.deleteBuilder().delete();
			
			UserSQLLite user = new UserSQLLite();
			user.setName("test");
			user.setPassword("test");
			createUser(user);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new AuthorizationException();
		}

	}

	public void createUser(UserSQLLite user) throws AuthorizationException {
		Dao<UserSQLLite, String> dao;
		try {
			dao = DaoManager.createDao(source, UserSQLLite.class);
			dao.createIfNotExists(user);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AuthorizationException("Пользователь уже существует");
		}

	}

	public TokenSQLLite authorizate(String userName, String password) throws AuthorizationException {

		try {
			Dao<UserSQLLite, String> dao = DaoManager.createDao(source, UserSQLLite.class);

			QueryBuilder<UserSQLLite, String> queryBuilder = dao.queryBuilder();
			queryBuilder.where().eq("name", userName);
			queryBuilder.where().eq("password", UserSQLLite.createDigest(password));

			UserSQLLite user = queryBuilder.queryForFirst();
			if (user == null) {
				throw new AuthorizationException("Проверьте правильность введенного пользователя и пароля");
			}

			TokenSQLLite token = new TokenSQLLite();
			token.setDate(new Timestamp(System.currentTimeMillis()));
			token.setUser(user);
			token.generateTokenes();
			Dao<TokenSQLLite, String> daoToken = DaoManager.createDao(source, TokenSQLLite.class);
			daoToken.createIfNotExists(token);

			return token;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new AuthorizationException();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AuthorizationException();
		}
	}

	public boolean isAuthorizated(String token) throws AuthorizationException {
		Dao<TokenSQLLite, String> dao;
		try {
			dao = DaoManager.createDao(source, TokenSQLLite.class);
			QueryBuilder<TokenSQLLite, String> queryBuilder = dao.queryBuilder();
			SelectArg arg = new SelectArg();
			arg.setValue(token);
			queryBuilder.where().like("token", arg).and().ge("date", System.currentTimeMillis() - TIMELIFE);

			return queryBuilder.queryForFirst() != null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AuthorizationException("Проблемы с подключением к бд");
		}

	}

	public TokenSQLLite regenerateToken(String refreshToken) throws AuthorizationException {
		try {
			Dao<TokenSQLLite, String> dao = DaoManager.createDao(source, TokenSQLLite.class);
			QueryBuilder<TokenSQLLite, String> queryBuilder = dao.queryBuilder();
			queryBuilder.where().eq("refreshToken", refreshToken);

			TokenSQLLite token = queryBuilder.queryForFirst();
			token.generateTokenes();
			dao.update(token);

			return token;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AuthorizationException("Проблемы с подключением к бд");
		}
	}

	public void logout(String token) throws AuthorizationException {
		try {
			Dao<TokenSQLLite, String> dao = DaoManager.createDao(source, TokenSQLLite.class);
			TokenSQLLite t = dao.queryBuilder().where().eq("token", token).queryForFirst();
			dao.delete(t);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AuthorizationException("Проблема с подключением к бд");
		}
		
	}
	
}
