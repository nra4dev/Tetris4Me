package org.nrasoft.androidapp.mytetris.uc.game.components;

import org.nrasoft.androidapp.R;
import org.nrasoft.androidapp.mytetris.uc.game.GameModel;
import org.nrasoft.androidapp.mytetris.uc.game.components.inner.Row;
import org.nrasoft.androidapp.mytetris.uc.game.components.inner.Square;
import org.nrasoft.androidapp.mytetris.uc.game.GameActivity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class Board extends Component {

	private int rowCount;
	private int colCount;
	private Row topRow; // index 0
	private Row currentRow;
	private int currentIndex;
	private GameModel model;
	private Row tempRow;
	private boolean valid;
	private Bitmap blockMap;
	private Canvas blockVas;

	public Board(GameActivity ga) {
		super(ga);
		colCount = host.getResources().getInteger(R.integer.col_count);
		rowCount = host.getResources().getInteger(R.integer.row_count);
		valid = false;
		model = new GameModel();
		/* Init Board */
		topRow = new Row(colCount,host);
		currentIndex = 0;
		tempRow = topRow;
		currentRow = topRow;
		for(int i = 1; i < rowCount; i++) {
			currentRow = new Row(colCount,host);
			currentIndex = i;
			currentRow.setAbove(tempRow);
			tempRow.setBelow(currentRow);
			tempRow = currentRow;
		}
		topRow.setAbove(currentRow);
		currentRow.setBelow(topRow);
	}

	public void draw(int x, int y, int squareSize, Canvas c){ // top left corner of state board
		Log.v("NRA", "Board.draw(x, y, squareSize) -> " + x + ","  + y + "," + squareSize);
		Log.v("NRA", "Board.valid=" + valid);

		if(topRow == null)
			throw new RuntimeException("Board was not initialized!");

		if(valid) {
			c.drawBitmap(blockMap, x, y, null);
			return;
		}
		
		/* This Block is responsible to prevent the
		 * java.lang.OutOfMemoryError: bitmap size exceeds VM budget
		 * Crash.
		 */
		try {
			blockMap = Bitmap.createBitmap(colCount *squareSize, rowCount *squareSize, Bitmap.Config.ARGB_8888);
		} catch(Exception e) {
			valid = false;
			tempRow = topRow;
			for(int i = 0; i < rowCount; i++) {
				if(tempRow != null) {
					c.drawBitmap(tempRow.drawBitmap(squareSize, i, model), x, y+i*squareSize, null);
					tempRow = tempRow.below();
				}
			}
			return;
		}

		blockVas = new Canvas(blockMap);
		valid = true;
		tempRow = topRow;
		for(int i = 0; i < rowCount; i++) {
			if(tempRow != null) {
				tempRow.draw(0,0+i*squareSize,squareSize,i, model,blockVas);
				tempRow = tempRow.below();
			}
		}
		c.drawBitmap(blockMap, x, y, null);
	}

	public int getColCount() {
		return colCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public GameModel getModel() {
		return model;
	}

	public Square get(int x, int y) {
		if(x < 0)
			return null;
		if(x > (colCount - 1))
			return null;
		if(y < 0)
			return null;
		if(y > (rowCount - 1))
			return null;
		if(currentIndex == y){
			return currentRow.get(x);
		} else if(currentIndex < y) {
			if(currentRow.below() == null)
				return null;
			else {
				currentRow = currentRow.below();
				currentIndex++;
				return get(x, y);
			}
		} else {
			if(currentRow.above() == null)
				return null;
			else {
				currentRow = currentRow.above();
				currentIndex--;
				return get(x, y);
			}
		}
	}

	public void set(int x, int y, Square square, int value) {
		Log.v("NRA", "Board.set(x,y,square, value) -> " + x + ","  + y + "," + square + ", " + value);
		if(x < 0)
			return;
		if(x > (colCount - 1))
			return;
		if(y < 0)
			return;
		if(y > (rowCount - 1))
			return;
		if(square == null)
			return;
		if(square.isEmpty())
			return;

		valid = false;
		if (value > 0) {
			try {
				model.setGridValue(y, x, value);
			} catch (Exception e) {
				Log.d("NRA", "setGridValue failed" + e.getMessage());
			}
		}
		if(currentIndex == y)
			currentRow.set(square,x);
		else if(currentIndex < y) {
			currentRow = currentRow.below();
			currentIndex++;
			set(x, y, square, value);
		} else {
			currentRow = currentRow.above();
			currentIndex--;
			set(x, y, square, value);
		}
	}

	public void cycle(long time) {
		// begin at bottom line
		if(topRow == null)
			throw new RuntimeException("BlockBoard was not initialized!");
		
		tempRow = topRow.above();
		for(int i = 0; i < rowCount; i++) {
			tempRow.cycle(time, this);
			tempRow = tempRow.above();
			if(tempRow == null)
				return;
		}
	}


	public int clearLines(int dim) {
		valid = false;
		Row clearPointer = currentRow;
		int clearCounter = 0;
		for(int i = 0; i < dim; i++) {
			if(clearPointer.isFull()) {
				clearCounter++;
				clearPointer.clear(this, host.state.getAutoDropInterval());
			}
			clearPointer = clearPointer.above();
		}
		currentRow = topRow;
		currentIndex = 0;
		return clearCounter;
	}
	public int clearColumns() {
		Log.d("NRA", "Board.clearColumns() entered");
		int clearCounter = 0;
		try {
			int columnMagicSum = host.getResources().getInteger(R.integer.columnMagicSum);
			for (int i = 0; i < model.getGridColCount(); i++) {
				if (model.getGridValueTotalColumn()[i] == columnMagicSum) {
					Log.i("NRA", "getGridValueTotalColumn()=" + columnMagicSum);
					clearCounter++;
					valid = false;
					tempRow = topRow;
					for (int j = 0; j < rowCount; j++) {
						if (tempRow != null) {
							tempRow.clear(i);
							tempRow = tempRow.below();
						}
					}
					currentRow = topRow;
					currentIndex = 0;
				}
			}
		} catch (Exception e) {
			Log.e("NRA", "Board.clearColumns() failed");
		}
		Log.d("NRA", "Board.clearColumns() finished");
		return clearCounter;
	}
	public Row getTopRow() {
		return topRow;
	}

	public void finishClear(Row row) {
		valid = false;
		topRow = row;
		currentIndex++;
		host.display.invalidatePhantom();
	}

	public void interruptClearAnimation() {
		// begin at bottom line
		if(topRow == null)
			throw new RuntimeException("Board was not initialized!");
		
		Row interator = topRow.above();
		for(int i = 0; i < rowCount; i++) {
			if(interator.interrupt(this)) {
				interator = topRow.above();
				i = 0;
				valid = false;
			} else
				interator = interator.above();
			if(interator == null)
				return;
		}
		host.display.invalidatePhantom();
	}

	public void invalidate() {
		valid = false;
	}

	public void popupScore(long addScore) {
		//TODO
	}

	public int getCurrentRowIndex() {
		return currentIndex;
	}

	public Row getCurrentRow() {
		return currentRow;
	}

	public void setCurrentRowIndex(int index) {
		currentIndex = index;
	}

	public void setCurrentRow(Row row) {
		currentRow = row;
	}

}
