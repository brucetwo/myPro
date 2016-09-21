package whu.entity;

import java.io.Serializable;

public class User1Bean implements Serializable {
	private int userId;
	private String userName;
	private String userPassword;
	private int userPermission;
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public int getUserPermission() {
		return userPermission;
	}
	public void setUserPermission(int userPermission) {
		this.userPermission = userPermission;
	}
	
	
	public User1Bean()
	{
		
	}
	
	public User1Bean(String username,String userpwd,int userpermission)
	{
		this.userName=username;
		this.userPassword=userpwd;
		this.userPermission=userpermission;
	}
	
	public User1Bean(int uid,String username,String userpwd,int userpermission)
	{
		this.userId=uid;
		this.userName=username;
		this.userPassword=userpwd;
		this.userPermission=userpermission;
	}
}
