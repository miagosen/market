package com.lx.market.model;

/**
 * Created by Antikvo.Miao on 2014/8/30.
 */
public class CircleLabel {

  private int id;
  //圆中间显示的文字内容
  private String text;
  //圆的颜色
  private int circleColor;
  //文字颜色
  private int textColor;
  //文字大小;
  private int textSize;
  //半径大小
  private int radius;

  public int getRadius () {
	return radius;
  }

  public void setRadius (int radius) {
	this.radius = radius;
  }

  public CircleLabel (String text, int radius) {
	this.radius = radius;
	this.text = text;
  }

  public int getTextColor () {
	return textColor;
  }

  public void setTextColor (int textColor) {
	this.textColor = textColor;
  }

  public String getText () {
	return text;
  }

  public void setText (String text) {
	this.text = text;
  }

  public int getCircleColor () {
	return circleColor;
  }

  public void setCircleColor (int circleColor) {
	this.circleColor = circleColor;
  }

  public int getId () {
	return id;
  }

  public void setId (int id) {
	this.id = id;
  }

  public int getTextSize () {
	return textSize;
  }

  public void setTextSize (int textSize) {
	this.textSize = textSize;
  }
}
