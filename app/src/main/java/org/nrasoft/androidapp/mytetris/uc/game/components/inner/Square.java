package org.nrasoft.androidapp.mytetris.uc.game.components.inner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

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

	private int type;

	private Paint textPaint;
	private Paint circlePaint;
	private Paint rectPaint;
	private Bitmap bm;
	private Bitmap phantomBM;
	private Canvas canv;
	private Canvas phantomCanv;
	private int squaresize;
	private int phantomAlpha;

	public Square(int type, Context c) {
		this.type = type;
		rectPaint = new Paint();
		circlePaint = new Paint();
		circlePaint.setColor(Color.BLACK);
		circlePaint.setTextAlign(Paint.Align.CENTER);
		textPaint = new Paint();
		textPaint.setColor(Color.BLACK);
		textPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setFakeBoldText(true);
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

	public void reDraw(int ss, int num) {
		if(type == type_empty)
			return;
		Log.v("NRA", "Square.reDraw(ss,num) -> (" + ss + ","+ num + ")");
		squaresize = ss;
		textPaint.setTextSize(ss/2);
		bm = Bitmap.createBitmap(ss, ss, Bitmap.Config.ARGB_8888);
		phantomBM = Bitmap.createBitmap(ss, ss, Bitmap.Config.ARGB_8888);
		canv = new Canvas(bm);
		phantomCanv = new Canvas(phantomBM);

		rectPaint.setAlpha(255);
		canv.drawRect(0, 0, squaresize, squaresize, rectPaint);
		//canv.drawCircle(squaresize/2, squaresize/2, squaresize/2, circlePaint);
		if (num >0) {
			canv.drawText("" + num, squaresize / 2, squaresize / 2, textPaint);
		}
		rectPaint.setAlpha(phantomAlpha);
		phantomCanv.drawRect(0, 0, squaresize, squaresize, rectPaint);

	}

	public Square clone(Context c) {
		return new Square(type, c);
	}

	public boolean isEmpty() {
		if(type == type_empty)
			return true;
		else
			return false;
	}

	public void draw(int x, int y, int squareSize, Canvas c, boolean isPhantom, int num) { // top left corner of square
		Log.v("NRA", "Square.draw(x,y,isPhantom, num) -> (" + x + ","  + y + "," + isPhantom + "," + num+ ")");
		if(type == type_empty)
			return;
		if(squareSize != squaresize) {
			///reDraw(squareSize, num);
		}
		reDraw(squareSize, num);
		if(isPhantom) {
			c.drawBitmap(phantomBM, x, y, null);
		} else {
			c.drawBitmap(bm, x, y, null);
		}
	}



}
