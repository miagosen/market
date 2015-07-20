package com.lx.market.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Antikvo.Miao on 2014/9/15.
 */
@Table(name = "StartPageInfo")
public class StartPageInfo implements Serializable {
  private static final long serialVersionUID = -477481470349671320L;

  @Id
  private int id;
  @Column(column = "imageUrl")
  private String imageUrl;
  @Column(column = "text")
  private String text;

  public int getId () {
	return id;
  }

  public void setId (int id) {
	this.id = id;
  }

  public String getImageUrl () {
	return imageUrl;
  }

  public void setImageUrl (String imageUrl) {
	this.imageUrl = imageUrl;
  }

  public String getText () {
	return text;
  }

  public void setText (String text) {
	this.text = text;
  }

  @Override
  public String toString () {
	return "StartPageInfo{" +
	  "id=" + id +
	  ", imageUrl='" + imageUrl + '\'' +
	  ", text='" + text + '\'' +
	  '}';
  }
}
