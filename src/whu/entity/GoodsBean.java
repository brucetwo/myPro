package whu.entity;

import java.io.Serializable;

public class GoodsBean implements Serializable{
private int id;
private String name;
private String price;
private String type;
private String note;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getPrice() {
	return price;
	
}
public void setPrice(String price) {
	this.price = price;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getNote() {
	return note;
}
public void setNote(String note) {
	this.note = note;
}

public GoodsBean() {
	super();
	// TODO Auto-generated constructor stub
}
public GoodsBean(int id, String name, String price, String type, String note) {
	super();
	this.id = id;
	this.name = name;
	this.price = price;
	this.type = type;
	this.note = note;
}

}
