package org.nrasoft.androidapp.mytetris.uc.game.pieces;

import org.nrasoft.androidapp.mytetris.uc.game.components.inner.Square;

import android.content.Context;

public class TPiece extends Piece3x3 {

	private Square tSquare;

	public TPiece(Context c) {
		super(c);
		tSquare = new Square(Piece.type_T,c);
		pattern[1][0] = tSquare;
		pattern[1][1] = tSquare;
		pattern[1][2] = tSquare;
		pattern[2][1] = tSquare;
		patternNum[1][0] = 2;
		patternNum[1][1] = 3;
		patternNum[1][2] = 4;
		patternNum[2][1] = 5;
		reDraw();
	}

	@Override
	public void reset(Context c) {
		super.reset(c);
		pattern[1][0] = tSquare;
		pattern[1][1] = tSquare;
		pattern[1][2] = tSquare;
		pattern[2][1] = tSquare;
		patternNum[1][0] = 2;
		patternNum[1][1] = 3;
		patternNum[1][2] = 4;
		patternNum[2][1] = 5;
		reDraw();
	}

}
