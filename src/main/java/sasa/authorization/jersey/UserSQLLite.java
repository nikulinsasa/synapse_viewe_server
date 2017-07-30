package sasa.authorization.jersey;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="synapse_users")
public class UserSQLLite implements IUser {

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(uniqueIndex = true)
	private String name;
	
	@DatabaseField
	private String password;
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setPassword(String password) {
		try {
			this.password = createDigest(password);
		} catch (NoSuchAlgorithmException e) {
			this.password = "";
			e.printStackTrace();
		}
		
	}

	public static String createDigest(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(password.getBytes());
		return new String(md.digest());
	}
	
	@Override
	public boolean isCorrectPassword(String inputPassword) {
		try {
			return password.equals(createDigest(inputPassword));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
