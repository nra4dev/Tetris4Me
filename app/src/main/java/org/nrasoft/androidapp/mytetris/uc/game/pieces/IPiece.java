package org.nrasoft.androidapp.mytetris.uc.game.pieces;

import org.nrasoft.androidapp.mytetris.uc.game.components.inner.Square;

import android.content.Context;

public class IPiece extends Piece4x4 {
	
	private Square iSquare;

	public IPiece(Context c) {
		super(c);
		iSquare = new Square(Piece.type_I,c);
		pattern[2][0] = iSquare;
		pattern[2][1] = iSquare;
		pattern[2][2] = iSquare;
		pattern[2][3] = iSquare;
		patternNum[2][0] = 1;
		patternNum[2][1] = 2;
		patternNum[2][2] = 3;
		patternNum[2][3] = 4;
		reDraw();
	}
	
	@Override
	public void reset(Context c) {
		super.reset(c);
		pattern[2][0] = iSquare;
		pattern[2][1] = iSquare;
		pattern[2][2] = iSquare;
		pattern[2][3] = iSquare;
		patternNum[2][0] = 1;
		patternNum[2][1] = 2;
		patternNum[2][2] = 3;
		patternNum[2][3] = 4;
		reDraw();
	}

}
