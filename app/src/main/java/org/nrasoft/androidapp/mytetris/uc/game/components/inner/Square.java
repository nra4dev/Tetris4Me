package org.nrasoft.androidapp.mytetris.uc.game.components.inner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import org.nrasoft.androidapp.R;

public class Square {

	public static final int type_empty = 0;
	public static final int type_blue = 1;
	public static final int type_orange = 2;
	public static final int type_yellow = 3;
	public static final int type_red = 4;
	public static final int type_green = 5;
	public static final int type_magenta = 6;
	public static final int type_cyan = 7;

	private int num;
	private int type;
	private Paint textPaint;
	private Paint circlePaint;
	private Paint rectPaint;
	private Bitmap bm;
	private Bitmap phantomBM;
	private Canvas canv;
	private Canvas phantomCanv;
	//private Context context;
	private int squaresize;
	private int phantomAlpha;

	public Square(int type, Context c) {
		this.type = type;
		rectPaint = new Paint();
		circlePaint = new Paint();
		circlePaint.setColor(Color.BLACK);
		circlePaint.setTextAlign(Paint.Align.CENTER);
		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextAlign(Paint.Align.CENTER);
		phantomAlpha = c.getResources().getInteger(R.integer.phantom_alpha);
		squaresize = 0;
		switch(type){
			case type_blue:
				rectPaint.setColor(c.getResources().getColor(R.color.square_blue));
				break;
			case type_orange:
				rectPaint.setColor(c.getResources().getColor(R.color.square_orange));
				break;
			case type_yellow:
				rectPaint.setColor(c.getResources().getColor(R.color.square_yellow));
				break;
			case type_red:
				rectPaint.setColor(c.getResources().getColor(R.color.square_red));
				break;
			case type_green:
				rectPaint.setColor(c.getResources().getColor(R.color.square_green));
				break;
			case type_magenta:
				rectPaint.setColor(c.getResources().getColor(R.color.square_magenta));
				break;
			case type_cyan:
				rectPaint.setColor(c.getResources().getColor(R.color.square_cyan));
				break;
			case type_empty:
				return;
			default: // error: white
				rectPaint.setColor(c.getResources().getColor(R.color.square_error));
				break;
		}
	}

	public void reDraw(int ss) {
		if(type == type_empty)
			return;

		squaresize = ss;
		textPaint.setTextSize(ss/2);
		bm = Bitmap.createBitmap(ss, ss, Bitmap.Config.ARGB_8888);
		phantomBM = Bitmap.createBitmap(ss, ss, Bitmap.Config.ARGB_8888);
		canv = new Canvas(bm);
		phantomCanv = new Canvas(phantomBM);

		rectPaint.setAlpha(255);
		canv.drawRect(0, 0, squaresize, squaresize, rectPaint);
		canv.drawCircle(squaresize/2, squaresize/2, squaresize/2, circlePaint);
		canv.drawText("5", squaresize/2, squaresize/2, textPaint);


		rectPaint.setAlpha(phantomAlpha);
		phantomCanv.drawRect(0, 0, squaresize, squaresize, rectPaint);

	}

	public Square clone(Context c) {
		return new Square(type, c);
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public boolean isEmpty() {
		if(type == type_empty)
			return true;
		else
			return false;
	}

	public void draw(int x, int y, int squareSize, Canvas c, boolean isPhantom) { // top left corner of square
		if(type == type_empty)
			return;

		if(squareSize != squaresize)
			reDraw(squareSize);

		if(isPhantom) {
			c.drawBitmap(phantomBM, x, y, null);
		} else {
			c.drawBitmap(bm, x, y, null);
		}
	}

	private float calculateFontSize(Rect textBounds, Rect textContainer, String text, Paint textPaint) {
		int stage = 1;
		float textSize = 0;

		while(stage < 3) {
			if (stage == 1) textSize += 10;
			else
			if (stage == 2) textSize -= 1;

			textPaint.setTextSize(textSize);
			textPaint.getTextBounds(text, 0, text.length(), textBounds);

			textBounds.offsetTo(textContainer.left, textContainer.top);

			boolean fits = textContainer.contains(textBounds);
			if (stage == 1 && !fits) stage++;
			else
			if (stage == 2 &&  fits) stage++;
		}

		return textSize;
	}

	private void drawRectText(String text, Paint textPaint, Canvas canvas, Rect r) {

		textPaint.setTextSize(20);
		textPaint.setTextAlign(Paint.Align.CENTER);
		int width = r.width();
		int numOfChars = textPaint.breakText(text,true,width,null);
		int start = (text.length()-numOfChars)/2;
		canvas.drawText(text,start,start+numOfChars,r.exactCenterX(),r.exactCenterY(),textPaint);
	}
}
