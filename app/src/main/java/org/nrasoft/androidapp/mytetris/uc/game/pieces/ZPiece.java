package org.nrasoft.androidapp.mytetris.uc.game.pieces;

import org.nrasoft.androidapp.R;
import org.nrasoft.androidapp.mytetris.uc.game.components.inner.Square;

import android.content.Context;

public class ZPiece extends Piece3x3 {

	private Square zSquare;

	public ZPiece(Context c) {
		super(c);
		zSquare = new Square(Piece.type_Z,c);
		num = c.getResources().getInteger(R.integer.OPiece_num);
		initPattern(c);
		reDraw();
	}

	@Override
	public void reset(Context c) {
		super.reset(c);
		initPattern(c);
		reDraw();
	}

	public void initPattern(Context c) {
		pattern[1][0] = zSquare;
		pattern[1][1] = zSquare;
		pattern[2][1] = zSquare;
		pattern[2][2] = zSquare;

		patternNum[1][0] = getNum();
		patternNum[1][1] = getNum();
		patternNum[2][1] = getNum();
		patternNum[2][2] = getNum();
	}

}
