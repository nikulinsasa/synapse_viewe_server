package sasa.authorization.jersey;

import java.sql.Timestamp;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "synapse_tokens")
public class TokenSQLLite {

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private Timestamp date;

	@DatabaseField
	private String token;

	@DatabaseField
	private String refreshToken;

	@DatabaseField(foreign = true)
	private UserSQLLite user;

	public void generateTokenes() {
		Long time = new Long(date.getTime());
		setDate(new Timestamp(time));

		token = Long.toHexString(time + Math.round(Math.random() * 1000) * 10000000);
		refreshToken = Long.toHexString(time + Math.round(Math.random() * 1000 + 6000) * 10000000);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public UserSQLLite getUser() {
		return user;
	}

	public void setUser(UserSQLLite user) {
		this.user = user;
	}

}
