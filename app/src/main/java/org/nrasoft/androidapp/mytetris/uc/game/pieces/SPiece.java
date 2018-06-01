package org.nrasoft.androidapp.mytetris.uc.game.pieces;

import org.nrasoft.androidapp.mytetris.uc.game.components.inner.Square;

import android.content.Context;

public class SPiece extends Piece3x3 {

	private Square sSquare;
	
	public SPiece(Context c) {
		super(c);
		sSquare = new Square(Piece.type_S,c);
		pattern[1][1] = sSquare;
		pattern[1][2] = sSquare;
		pattern[2][0] = sSquare;
		pattern[2][1] = sSquare;
		patternNum[1][1] = 1;
		patternNum[1][2] = 1;
		patternNum[2][0] = 2;
		patternNum[2][1] = 3;
		reDraw();
	}

	@Override
	public void reset(Context c) {
		super.reset(c);
		pattern[1][1] = sSquare;
		pattern[1][2] = sSquare;
		pattern[2][0] = sSquare;
		pattern[2][1] = sSquare;
		patternNum[1][1] = 1;
		patternNum[1][2] = 1;
		patternNum[2][0] = 2;
		patternNum[2][1] = 3;
		reDraw();
	}

}
