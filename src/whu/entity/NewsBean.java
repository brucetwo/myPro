package whu.entity;

import java.io.Serializable;

public class NewsBean implements Serializable{
private int id;
private String title;
private String author;
private String time;
private String content;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getAuthor() {
	return author;
}
public void setAuthor(String author) {
	this.author = author;
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
public NewsBean() {
	super();
	// TODO Auto-generated constructor stub
}
public NewsBean(int id, String title, String author, String time, String content) {
	super();
	this.id = id;
	this.title = title;
	this.author = author;
	this.time = time;
	this.content = content;
}

}
