package org.nrasoft.androidapp.mytetris.uc.game;

import org.nrasoft.androidapp.R;
import org.nrasoft.androidapp.mytetris.uc.game.components.Component;
import org.nrasoft.androidapp.mytetris.uc.game.components.inner.Row;
import org.nrasoft.androidapp.mytetris.uc.game.pieces.Piece;
import org.nrasoft.androidapp.mytetris.uc.game.GameActivity;

import android.R.color;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.preference.PreferenceManager;

public class GameDisplay extends Component {

	private int prevPhantomY;
	private boolean dropPhantom;
	private Paint paint;
	private int gridRowBorder;
	private int gridColumnBorder;
	private int squareSize;
	private int rowOffset;
	private int rowCount;
	private int columnOffset;
	private int columnCount;
	private boolean landscapeInitialized;
	private int prev_top;
	private int prev_bottom;
	private int prev_left;
	private int prev_right;
	private int textLeft;
	private int textTop;
	private int textRight;
	private int textBottom;
	private int textLines;
	private int textSizeH;
	private int textEmptySpacing;
	private Paint textPaint;
	private Rect textRect;
	private int textHeight;
	private Paint popUptextPaint;
	
	public GameDisplay(GameActivity ga) {
		super(ga);
		invalidatePhantom();
		setPhantomY(0);
		landscapeInitialized = false;
	    paint = new Paint();
		rowCount = host.getResources().getInteger(R.integer.row_count);
		columnCount = host.getResources().getInteger(R.integer.col_count);

		squareSize = 1; // unknown at this point!
		prev_top = 1; // unknown at this point!
		prev_bottom = 1; // unknown at this point!
		prev_left = 1; // unknown at this point!
		
		prev_right = 1; // unknown at this point!
		
		rowOffset = host.getResources().getInteger(R.integer.row_offset);
		columnOffset = host.getResources().getInteger(R.integer.column_offset);

		textPaint = new Paint();
		textRect = new Rect();
		textPaint.setColor(host.getResources().getColor(color.white));
		textPaint.setAlpha(host.getResources().getInteger(R.integer.textalpha));
		textPaint.setAntiAlias(PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_antialiasing", true));
		popUptextPaint = new Paint();
		popUptextPaint.setColor(host.getResources().getColor(color.white));
		popUptextPaint.setAntiAlias(PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_antialiasing", true));
		popUptextPaint.setTextSize(120);
		textSizeH = 1;
		textHeight = 2;
		if(PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_fps", false))
			textLines = 10;
		else
			textLines = 8;
	}
	
	public void doDraw(Canvas c, int fps) {
		if(c==null)
			return;

		if (!landscapeInitialized){
			int fpsenabled = 0;
			if(PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_fps", false))
				fpsenabled = 1;
			
			host.state.getBoard().invalidate();
			//portraitInitialized = false;
			landscapeInitialized = true;
			squareSize = (int)(((c.getHeight()-1) - 2*rowOffset)/ rowCount);
			int size2 = (int)(((c.getHeight()-1) - 2*columnOffset)/(columnCount + 4 + host.getResources().getInteger(R.integer.padding_columns)));
			if(size2 < squareSize) {
				squareSize = size2;
				rowOffset = (int)(((c.getHeight()-1) - squareSize * rowCount)/2);
			} else
				columnOffset = (int)(((c.getWidth()-1) - squareSize *(host.getResources().getInteger(R.integer.padding_columns)+4+ columnCount))/2);
			gridRowBorder = rowOffset + squareSize * rowCount;
			gridColumnBorder = columnOffset + squareSize * columnCount;
			prev_top = rowOffset;
			prev_bottom = rowOffset + 4* squareSize;
			prev_left = gridColumnBorder + host.getResources().getInteger(R.integer.padding_columns)* squareSize;
			prev_right = prev_left + 4* squareSize;
			textLeft = prev_left;
			textTop = prev_bottom + 2* squareSize;
			textRight = (c.getWidth()-1) - columnOffset;
			textBottom = (c.getHeight()-1) - rowOffset - squareSize;
			textSizeH = 1;

			// Adaptive Text Size Setup
			textPaint.setTextSize(textSizeH + 1);
			while(textPaint.measureText("00:00:00") < (textRight - textLeft)) {
				//stuff
				textPaint.getTextBounds((String)"Level:32", 0, 6, textRect);
				textHeight = textRect.height();
				textEmptySpacing = ((textBottom - textTop) - (textLines*(textHeight + 3))) / (3 + fpsenabled);
				if(textEmptySpacing < 10)
					break;
				
				textSizeH++;
				textPaint.setTextSize(textSizeH + 1);
			}
			textPaint.setTextSize(textSizeH);
			textPaint.getTextBounds((String)"Level:32", 0, 6, textRect);
			textHeight = textRect.height() + 3;
			textEmptySpacing = ((textBottom - textTop) - (textLines*(textHeight))) / (3 + fpsenabled);
			
			host.controls.setPreviewRect(new Rect(prev_left,prev_top,prev_right,prev_bottom));
		}

		// Background
		//paint.setColor(host.getResources().getColor(color.background_dark));
		//c.drawRect(0, 0, c.getColCount()-1, c.getRowCount()-1, paint);
		c.drawColor(Color.argb(0, 0, 0, 0), android.graphics.PorterDuff.Mode.CLEAR);
		
		host.state.getBoard().draw(columnOffset, rowOffset, squareSize, c);
		
		drawActive(columnOffset, rowOffset, squareSize, c);

		if(PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_phantom", false))
			drawPhantom(columnOffset, rowOffset, squareSize, c);
		
	    drawGrid(columnOffset, rowOffset, gridColumnBorder, gridRowBorder, c);
		
	    if(host.controls.isBoardTouched())
	    	drawTouchIndicator();
	    
	    drawPreview(prev_left, prev_top, prev_right, prev_bottom, c);

	    drawTextFillBox(c, fps);

		if(PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_popup", true))
			drawPopupText(c);
	}

	private void drawTouchIndicator() {
		// TODO touch indicator on preview box
	}

	private void drawGrid(int x, int y, int xBorder, int yBorder, Canvas c) {
		
		paint.setColor(host.getResources().getColor(color.holo_blue_dark));
        for (int zeilePixel = 0; zeilePixel <= rowCount; zeilePixel ++) {
            c.drawLine(x, y + zeilePixel* squareSize, xBorder, y + zeilePixel* squareSize, paint);
        }
        for (int spaltePixel = 0; spaltePixel <= columnCount; spaltePixel ++) {
            c.drawLine(x + spaltePixel* squareSize, y, x + spaltePixel* squareSize, yBorder, paint);
        }

		//draw Border
		paint.setColor(host.getResources().getColor(color.background_light));
		c.drawLine(x, y, x, yBorder, paint);
		c.drawLine(x, y, xBorder, y, paint);
		c.drawLine(xBorder, yBorder, xBorder, y, paint);
		c.drawLine(xBorder, yBorder, x, yBorder, paint);
	}

	private void drawPreview(int left, int top, int right, int bottom, Canvas c) {
		//Background
		//paint.setColor(host.getResources().getColor(color.background_dark));
		//c.drawRect(left, top, right, bottom, paint);
		
		// Piece
		drawPreview(left, top, squareSize, c);
		
		// Grid Lines
		paint.setColor(host.getResources().getColor(color.holo_blue_dark));
        for (int zeilePixel = 0; zeilePixel <= 4; zeilePixel ++) {
            c.drawLine(left, top + zeilePixel* squareSize, right, top + zeilePixel* squareSize, paint);
        }
        for (int spaltePixel = 0; spaltePixel <= 4; spaltePixel ++) {
            c.drawLine(left + spaltePixel* squareSize, top, left + spaltePixel* squareSize, bottom, paint);
        }
        
        // Border
		paint.setColor(host.getResources().getColor(color.background_light));
		c.drawLine(left, top, right, top, paint);
		c.drawLine(left, top, left, bottom, paint);
		c.drawLine(right, bottom, right, top, paint);
		c.drawLine(right, bottom, left, bottom, paint);
	}

	private void drawTextFillBox(Canvas c, int fps) {	
		
	    // draw Level Text
		c.drawText(host.getResources().getString(R.string.level_title), textLeft, textTop + textHeight, textPaint);
		c.drawText(host.state.getLevelString(), textLeft, textTop + 2*textHeight, textPaint);

	    // draw Score Text
		c.drawText(host.getResources().getString(R.string.score_title), textLeft, textTop + 3*textHeight + textEmptySpacing, textPaint);
		c.drawText(host.state.getScoreString(), textLeft, textTop + 4*textHeight + textEmptySpacing, textPaint);

		// draw Time Text
		c.drawText(host.getResources().getString(R.string.time_title), textLeft, textTop + 5*textHeight + 2*textEmptySpacing, textPaint);
		c.drawText(host.state.getTimeString(), textLeft, textTop + 6*textHeight + 2*textEmptySpacing, textPaint);

	    // draw APM Text
		c.drawText(host.getResources().getString(R.string.apm_title), textLeft, textTop + 7*textHeight + 3*textEmptySpacing, textPaint);
		c.drawText(host.state.getAPMString(), textLeft, textTop + 8*textHeight + 3*textEmptySpacing, textPaint);

	    // draw FPS Text
		if(!PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_fps", false))
			return;
		c.drawText(host.getResources().getString(R.string.fps_title), textLeft, textTop + 9*textHeight + 4*textEmptySpacing, textPaint);
		c.drawText("" + fps, textLeft, textTop + 10*textHeight + 4*textEmptySpacing, textPaint);
	}
	
	private void drawActive(int xOffset, int yOffset, int squareSize,
			Canvas c) {
		host.state.getActivePiece().drawOnBoard(xOffset, yOffset, squareSize, c);
	}

	private void drawPhantom(int xOffset, int yOffset, int squareSize,
			Canvas c) {
		Piece active = host.state.getActivePiece();
		int y = active.getY();
		int x = active.getX();
		active.setPhantom(true);
		
		if(dropPhantom) {
			int backup__currentRowIndex = host.state.getBoard().getCurrentRowIndex();
			Row backup__currentRow = host.state.getBoard().getCurrentRow();
			int cnt = y+1;
			
			while(active.setPositionSimpleCollision(x, cnt, host.state.getBoard())) {
				cnt++;
			}
			
			 host.state.getBoard().setCurrentRowIndex(backup__currentRowIndex);
			 host.state.getBoard().setCurrentRow(backup__currentRow);
		} else
			active.setPositionSimple(x, prevPhantomY);
		
		prevPhantomY = active.getY();
		active.drawOnBoard(xOffset, yOffset, squareSize, c);
		active.setPositionSimple(x, y);
		active.setPhantom(false);
		dropPhantom = false;
	}

	private void drawPreview(int xOffset, int yOffset, int squareSize,
			Canvas c) {
		host.state.getPreviewPiece().drawOnPreview(xOffset, yOffset, squareSize, c);
	}

	private void drawPopupText(Canvas c) {
		
		final int offset = 6;
		final int diagonaloffset = 6;
		
		String text = host.state.getPopupString();
		popUptextPaint.setTextSize(host.state.getPopupSize());
		popUptextPaint.setColor(host.getResources().getColor(color.black));
		popUptextPaint.setAlpha(host.state.getPopupAlpha());

		int left = columnOffset + ((int) columnCount * squareSize /2) - ((int)popUptextPaint.measureText(text)/2); // middle minus half text width
		int top = c.getHeight()/2;
		
		c.drawText(text, offset+left, top, popUptextPaint); // right
		c.drawText(text, diagonaloffset+left, diagonaloffset+top, popUptextPaint); // bottom right
		c.drawText(text, left, offset+top, popUptextPaint); // bottom
		c.drawText(text, -diagonaloffset+left, diagonaloffset+top, popUptextPaint); // bottom left
		c.drawText(text, -offset+left, top, popUptextPaint); // left
		c.drawText(text, -diagonaloffset+left, -diagonaloffset+top, popUptextPaint); // top left
		c.drawText(text, left, -offset+top, popUptextPaint); // top
		c.drawText(text, diagonaloffset+left, -diagonaloffset+top, popUptextPaint); // top right

		popUptextPaint.setColor(host.state.getPopupColor());
		popUptextPaint.setAlpha(host.state.getPopupAlpha());
		c.drawText(text, left, top, popUptextPaint);
		
	}

	public void invalidatePhantom() {
		dropPhantom = true;
	}

	public void setPhantomY(int i) {
		prevPhantomY = i;
	}

}
