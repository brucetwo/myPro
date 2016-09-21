package whu.entity;

import java.io.Serializable;

public class MessageBean implements Serializable{
private int id;
private int customId;
private int goodsId;
private String time;
private String content;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getCustomId() {
	return customId;
}
public void setCustomId(int customId) {
	this.customId = customId;
}
public int getGoodsId() {
	return goodsId;
}
public void setGoodsId(int goodsId) {
	this.goodsId = goodsId;
}
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public MessageBean(int id, int customId, int goodsId, String time,
		String content) {
	super();
	this.id = id;
	this.customId = customId;
	this.goodsId = goodsId;
	this.time = time;
	this.content = content;
}
public MessageBean() {
	super();
	// TODO Auto-generated constructor stub
}
 
}
