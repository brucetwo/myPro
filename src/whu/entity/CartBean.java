package whu.entity;

import java.io.Serializable;

public class CartBean implements Serializable{
private int id;
private int goodsId;
private int num;
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
public int getNum() {
	return num;
}
public void setNum(int num) {
	this.num = num;
}
public CartBean() {
	super();
	// TODO Auto-generated constructor stub
}
public CartBean(int id, int goodsId, int num) {
	super();
	this.id = id;
	this.goodsId = goodsId;
	this.num = num;
}

}
