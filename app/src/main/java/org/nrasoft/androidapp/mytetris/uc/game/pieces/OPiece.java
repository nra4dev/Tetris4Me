package org.nrasoft.androidapp.mytetris.uc.game.pieces;

import org.nrasoft.androidapp.mytetris.uc.game.components.inner.Square;

import android.content.Context;

public class OPiece extends Piece4x4 {

	private Square oSquare;
	
	public OPiece(Context c) {
		super(c);
		oSquare = new Square(Piece.type_O,c);
		pattern[1][1] = oSquare;
		pattern[1][2] = oSquare;
		pattern[2][1] = oSquare;
		pattern[2][2] = oSquare;
		patternNum[1][1] = 2;
		patternNum[1][2] = 2;
		patternNum[2][1] = 1;
		patternNum[2][2] = 3;
		reDraw();
	}

	@Override
	public void reset(Context c) {
		super.reset(c);
		pattern[1][1] = oSquare;
		pattern[1][2] = oSquare;
		pattern[2][1] = oSquare;
		pattern[2][2] = oSquare;
		patternNum[1][1] = 2;
		patternNum[1][2] = 2;
		patternNum[2][1] = 1;
		patternNum[2][2] = 3;
		reDraw();
	}

}
