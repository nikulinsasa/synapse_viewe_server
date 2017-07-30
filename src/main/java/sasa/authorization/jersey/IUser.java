package sasa.authorization.jersey;

public interface IUser {

	public void setName(String name);
	
	public String getName();
	
	public void setPassword(String password);
	
	public boolean isCorrectPassword(String inputPassword);
	
}
