package com.lx.market.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.lx.market.model.CircleLabel;

/**
 * Created by Antikvo.Miao on 2014/8/27.
 */
public class CircleView extends View {
  private Paint circlePaint;//圆画笔
  private Paint textPaint;//字画笔
  private static final int DEFAULT_CIRCLE_PAINT_COLOR = 0xffE066FF;
  private static final int DEFAULT_TEXT_PAINT_COLOR = 0xffffffff;
  private static final int DEFAULT_TEXT_SIZE = 18;
  private static final int DEFAULT_ALPHA = 100;//默认透明度 0~255
  private CircleLabel circleLabel;

  public CircleView (Context context) {
	super(context);
	initPaint();
  }

  public CircleView (Context context, AttributeSet attrs) {
	super(context, attrs);
	initPaint();
  }

  public CircleView (Context context, AttributeSet attrs, int defStyleAttr) {
	super(context, attrs, defStyleAttr);
	initPaint();
  }

  private void initPaint () {
	circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	circlePaint.setColor(DEFAULT_CIRCLE_PAINT_COLOR);
	textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	textPaint.setColor(DEFAULT_TEXT_PAINT_COLOR);
	textPaint.setTextSize(DEFAULT_TEXT_SIZE);
  }

  public CircleLabel getCircleLabel () {
	return circleLabel;
  }

  public void setCircleLabel (CircleLabel circleLabel) {
	this.circleLabel = circleLabel;
	invalidate();
  }

  protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	if (circleLabel != null) {
	  setMeasuredDimension(circleLabel.getRadius() * 2, circleLabel.getRadius() * 2);
	}
  }

  @Override
  protected void onDraw (Canvas canvas) {
	if (circleLabel == null)
	  return;
	//计算半径
	float r1 = circleLabel.getRadius();//外面大圆
	float r2 = circleLabel.getRadius() * 4 / 5;//里面小圆
	//设置圆的颜色
	if (circleLabel.getCircleColor() == 0) {
	  circlePaint.setColor(DEFAULT_CIRCLE_PAINT_COLOR);
	} else {
	  circlePaint.setColor(circleLabel.getCircleColor());
	}
	float centerX, centerY;
	// 计算圆心
	centerX = this.getMeasuredWidth() / 2;
	centerY = this.getMeasuredHeight() / 2;
	circlePaint.setAlpha(DEFAULT_ALPHA);
	//画外面半透明大圆
	canvas.drawCircle(centerX, centerY, r1, circlePaint);
	//重置画笔透明度
	circlePaint.setAlpha(255);
	//画中间不透明圆
	canvas.drawCircle(centerX, centerY, r2, circlePaint);
	textPaint.setTextAlign(Paint.Align.CENTER);
	Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
	//计算文字的底线位置
	float baseline = centerY - r1 + (centerY + r1 - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
	if (circleLabel.getTextColor() != 0) {
	  textPaint.setColor(circleLabel.getTextColor());
	} else {
	  textPaint.setColor(DEFAULT_TEXT_PAINT_COLOR);
	}
	if (circleLabel.getTextSize() != 0) {
	  textPaint.setTextSize(circleLabel.getTextSize());
	} else {
	  textPaint.setTextSize(DEFAULT_TEXT_SIZE);
	}
	if (!TextUtils.isEmpty(circleLabel.getText())) {
	  canvas.drawText(circleLabel.getText(), centerX, baseline, textPaint);
	}
	super.onDraw(canvas);
  }

}
