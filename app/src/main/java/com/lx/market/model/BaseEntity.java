package com.lx.market.model;

import com.lidroid.xutils.db.annotation.Id;

import java.io.Serializable;

/**
 * Created by Antikvo.Miao on 2014/9/16.
 */
public abstract class BaseEntity implements Serializable {
  private static final long serialVersionUID = 5084954852561583013L;
  @Id(column = "id")
  protected int id;

  public int getId () {
	return id;
  }

  public void setId (int id) {
	this.id = id;
  }

  @Override
  public String toString () {
	return "BaseEntity{" +
	  "id=" + id +
	  '}';
  }
}
