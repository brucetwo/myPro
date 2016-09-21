package whu.entity;

import java.io.Serializable;

public class OrderBean implements Serializable {
private int id;
private int goodsId;
private int customId;
private int sellerId;
private String time;
private String num;
private String state;
private String total;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getGoodsId() {
	return goodsId;
}
public void setGoodsId(int goodsId) {
	this.goodsId = goodsId;
}
public int getCustomId() {
	return customId;
}
public void setCustomId(int customId) {
	this.customId = customId;
}
public int getSellerId() {
	return sellerId;
}
public void setSellerId(int sellerId) {
	this.sellerId = sellerId;
}
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
public String getNum() {
	return num;
}
public void setNum(String num) {
	this.num = num;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}
public String getTotal() {
	return total;
}
public void setTotal(String total) {
	this.total = total;
}
public OrderBean() {
	super();
	// TODO Auto-generated constructor stub
}
public OrderBean(int id, int goodsId, int customId, int sellerId, String time,
		String num, String state, String total) {
	super();
	this.id = id;
	this.goodsId = goodsId;
	this.customId = customId;
	this.sellerId = sellerId;
	this.time = time;
	this.num = num;
	this.state = state;
	this.total = total;
}

}
