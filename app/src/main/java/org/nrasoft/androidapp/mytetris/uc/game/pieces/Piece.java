package org.nrasoft.androidapp.mytetris.uc.game.pieces;

import org.nrasoft.androidapp.R;
import org.nrasoft.androidapp.mytetris.uc.game.components.inner.Square;
import org.nrasoft.androidapp.mytetris.uc.game.components.Board;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public abstract class Piece {

	public static final int type_J = 1; // blue
	public static final int type_L = 2; // orange
	public static final int type_O = 3; // yellow
	public static final int type_Z = 4; // red
	public static final int type_S = 5; // green
	public static final int type_T = 6; // magenta
	public static final int type_I = 7; // cyan

	protected boolean active;
	protected int x; // pattern position
	protected int y; // pattern position
	protected int dim;    // maximum dimensions for square matrix, so all rotations fit inside!
	protected int squareSize;
	protected int patternNum[][];
	protected int rotatedNum[][];
	protected int num;
	protected Square pattern[][]; 	// square matrix
	protected Square rotated[][]; 	// square matrix
	private Square emptySquare;
	private Canvas cv;
	private Bitmap bm;
	private Canvas cvPhantom;
	private Bitmap bmPhantom;
	private boolean isPhantom;
	
	/**
	 * Always call super(); first.
	 */
	protected Piece(Context c, int dimension) {
		this.dim = dimension;
		squareSize = 1;
		x = c.getResources().getInteger(R.integer.piece_start_x);
		y = 0;
		active = false;
		isPhantom = false;
		emptySquare =  new Square(Square.type_empty,c);
		
		pattern = new Square[dim][dim]; // empty piece
		rotated = new Square[dim][dim];
		patternNum = new int[dim][dim];
		rotatedNum = new int[dim][dim];
		for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
				pattern[i][j] = emptySquare;
				rotated[i][j] = emptySquare;
			}
		}
	}
	
	public void reset(Context c) {
		x = c.getResources().getInteger(R.integer.piece_start_x);
		y = 0;
		active = false;
		for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
				pattern[i][j] = emptySquare;
				patternNum[i][j] = 0;
			}
		}
	}
	
	public void setActive(boolean b) {
		active = b;
		reDraw();
	}
	
	public boolean isActive() {
		return active;
	}

	public int[][] getPatternNum() {
		return patternNum;
	}

	public void place(Board board) {
		active = false;
		for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
				if(pattern[i][j] != null)
					board.set(x+j,y+i,pattern[i][j], patternNum[i][j]);
			}
		}
	}

	public int getNum() {
		return num;
	}

	/**
	 * 
	 * @return true if movement was successfull.
	 */
	public boolean setPosition(int x_new, int y_new, boolean noInterrupt, Board board) {
		Log.v("NRA", "Piece.setPosition(x_new, y_new, noInterrupt) -> " + x_new + ","  + y_new + "," + noInterrupt + " entered");
		boolean collision = false;
		int leftOffset = 0;
		int rightOffset = 0;
		int bottomOffset = 0;
		for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
				if(pattern[i][j] != null) {
					leftOffset = - (x_new+j);
					rightOffset = (x_new+j) - (board.getColCount() - 1);
					bottomOffset = (y_new+i) - (board.getRowCount() - 1);
					if(!pattern[i][j].isEmpty() && (leftOffset > 0)) // left border violation
						return false;
					if(!pattern[i][j].isEmpty() && (rightOffset > 0)) // right border violation
						return false;
					if(!pattern[i][j].isEmpty() && (bottomOffset > 0)) // bottom border violation
						return false;
					if(board.get(x_new+j,y_new+i) != null) {
						collision = (!pattern[i][j].isEmpty() && !board.get(x_new+j,y_new+i).isEmpty()); // collision
						if(collision) {
							if(noInterrupt)
								return false;
							// Try to avoid collision by interrupting all running clear animations.
							board.interruptClearAnimation();
							collision = !board.get(x_new+j,y_new+i).isEmpty(); // Still not empty?
							if(collision)
								return false; // All hope is lost.
						}
					}
				}
			}
		}
		x = x_new;
		y = y_new;
		board.getModel().setActivePieceX(x);
		board.getModel().setActivePieceY(y);
		board.getModel().updateGridActiveValueMatrix(this);
		Log.d("NRA", "Piece.setPosition(x_new, y_new, noInterrupt) -> " + x_new + ","  + y_new + "," + noInterrupt + " finished");
		return true;
	}
	
	/**
	 * @return true if rotation was successfull.
	 */
	public abstract boolean turnLeft(Board board);

	/**
	 * @return true if rotation was successfull.
	 */
	public abstract boolean turnRight(Board board);

	/**
	 * 
	 * @return true if movement to the left was successfull.
	 */
	public boolean moveLeft(Board board) {
		if(!active)
			return true;
		return setPosition(x - 1, y, false, board);
	}

	/**
	 * 
	 * @return true if movement to the right was successfull.
	 */
	public boolean moveRight(Board board) {
		if(!active)
			return true;
		return setPosition(x + 1, y, false, board);
	}
	
	/**
	 * 
	 * @return true if drop was successfull. Otherwise the ground or other pieces was hit.
	 */
	public boolean drop(Board board) {
		if(!active)
			return true;
		return setPosition(x, y + 1, false, board);
	}

	public int hardDrop(boolean noInterrupt, Board board) {
		int i=0;
		while(setPosition(x, y + 1, noInterrupt, board)){
			if(i >= board.getRowCount())
				throw new RuntimeException("Hard Drop Error: dropped too far.");
			i++;
		}
		return i;
	}
	
	protected void reDraw() {
		Log.v("NRA", "Piece.reDraw()");
		bm = Bitmap.createBitmap(squareSize*dim, squareSize*dim, Bitmap.Config.ARGB_8888);
		cv = new Canvas(bm);
		for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
				if(pattern[i][j] == null) {} else
					if(!pattern[i][j].isEmpty()) {
						pattern[i][j].draw(j*squareSize, i*squareSize, squareSize, cv, false, patternNum[i][j]);
					}
			}
		}

		bmPhantom = Bitmap.createBitmap(squareSize*dim, squareSize*dim, Bitmap.Config.ARGB_8888);
		cvPhantom = new Canvas(bmPhantom);
		for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
				if(pattern[i][j] == null) {} else
					if(!pattern[i][j].isEmpty())
						pattern[i][j].draw(j*squareSize, i*squareSize, squareSize, cvPhantom, true, patternNum[i][j]);
			}
		}
	}
	
	/** draw on actual position
	 * 
	 * @param xOffset board x offset
	 * @param yOffset board y offset
	 * @param ss square size
	 * @param c canvas
	 */
	public void drawOnBoard(int xOffset, int yOffset, int ss, Canvas c) {
		Log.v("NRA", "Piece.drawOnBoard(xOffset,yOffset,ss) -> (" + xOffset + ","  + yOffset + "," + ss + ")");
		Log.v("NRA", toString());
		if(!active)
			return;
		if(ss != squareSize) {
			squareSize = ss;
			//reDraw();
		}
		reDraw();
		if(isPhantom)
			c.drawBitmap(bmPhantom, x*squareSize + xOffset, y*squareSize + yOffset, null);
		else
			c.drawBitmap(bm, x*squareSize + xOffset, y*squareSize + yOffset, null);
	}
	
	// draw on preview position
	public void drawOnPreview(int xpos, int ypos, int ss, Canvas c) {
		if(ss != squareSize) {
			squareSize = ss;
			reDraw();
		}
		c.drawBitmap(bm, xpos, ypos, null);
	}

	public int getDim() {
		return dim;
	}

	public void setPhantom(boolean b) {
		isPhantom = b;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setPositionSimple(int x_new, int y_new) {
		x = x_new;
		y = y_new;
	}

	public boolean setPositionSimpleCollision(int x_new, int y_new, Board board) {
		for(int i = 0; i < dim; i++) {
			for(int j = 0; j < dim; j++) {
				if(pattern[i][j] != null) {
					if(board.get(x_new+j,y_new+i) == null) {
						if(!pattern[i][j].isEmpty())
							return false;
					} else {
						if(!pattern[i][j].isEmpty() && !board.get(x_new+j,y_new+i).isEmpty())
							return false;
					}
					
				}
			}
		}
		x = x_new;
		y = y_new;
		return true;
	}

	public String toString() {
		String str = "Piece\n";
		str += "active=" + active + "\n";
		str += "x=" + x + "\n";
		str += "y=" + y + "\n";
		str += "dim=" + dim + "\n";
		str += "patternNum\n";
		str +=toString(patternNum);
		str += "\n";
		str += "rotatedNum\n";
		str +=toString(rotatedNum);
		str += "\n";
		return str;
	}
	public String toString(int[][] matrix) {
		String str = "";
		try {
			for (int i = 0; i < matrix.length; i++) {
				str += "\t";
				for (int j = 0; j < matrix[i].length; j++) {
					str += matrix[i][j] + " ";
				}
				str += "\n";
			}
		}
		catch (Exception e) {
			Log.d("NRA", "Piece.matrix(int[][] matrix) failed");
		}
		return str;
	}
}


