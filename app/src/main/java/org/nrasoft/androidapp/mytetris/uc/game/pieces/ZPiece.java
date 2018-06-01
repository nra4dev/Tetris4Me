package org.nrasoft.androidapp.mytetris.uc.game.pieces;

import org.nrasoft.androidapp.mytetris.uc.game.components.inner.Square;

import android.content.Context;

public class ZPiece extends Piece3x3 {

	private Square zSquare;

	public ZPiece(Context c) {
		super(c);
		zSquare = new Square(Piece.type_Z,c);
		pattern[1][0] = zSquare;
		pattern[1][1] = zSquare;
		pattern[2][1] = zSquare;
		pattern[2][2] = zSquare;
		patternNum[1][0] = 1;
		patternNum[1][1] = 1;
		patternNum[2][1] = 1;
		patternNum[2][2] = 1;

		reDraw();
	}

	@Override
	public void reset(Context c) {
		super.reset(c);
		pattern[1][0] = zSquare;
		pattern[1][1] = zSquare;
		pattern[2][1] = zSquare;
		pattern[2][2] = zSquare;

		patternNum[1][0] = 1;
		patternNum[1][1] = 1;
		patternNum[2][1] = 1;
		patternNum[2][2] = 1;

		reDraw();
	}

}
