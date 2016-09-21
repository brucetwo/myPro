package whu.entity;

import java.io.Serializable;

public class UserBean implements Serializable {
	private int id;
	private int lv;
	private int identity;
	private String name;
	private String password;
	private String phone;
	private String address;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLv() {
		return lv;
	}
	public void setLv(int lv) {
		this.lv = lv;
	}
	public int getIdentity() {
		return identity;
	}
	public void setIdentity(int identity) {
		this.identity = identity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public UserBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserBean(int id, int lv, int identity, String name, String password,
			String phone, String address) {
		super();
		this.id = id;
		this.lv = lv;
		this.identity = identity;
		this.name = name;
		this.password = password;
		this.phone = phone;
		this.address = address;
	}
	public UserBean(int id, int identity, String name, String password,
			String phone, String address) {
      this(id, 0,identity,name,password,phone,address);
	}
	
}
