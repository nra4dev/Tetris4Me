package org.nrasoft.androidapp.mytetris.uc.game.pieces;

import org.nrasoft.androidapp.R;
import org.nrasoft.androidapp.mytetris.uc.game.components.inner.Square;

import android.content.Context;

public class TPiece extends Piece3x3 {

	private Square tSquare;

	public TPiece(Context c) {
		super(c);
		tSquare = new Square(Piece.type_T,c);
		num = c.getResources().getInteger(R.integer.TPiece_num);
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
		pattern[1][0] = tSquare;
		pattern[1][1] = tSquare;
		pattern[1][2] = tSquare;
		pattern[2][1] = tSquare;
		patternNum[1][0] = getNum();
		patternNum[1][1] = getNum();
		patternNum[1][2] = getNum();
		patternNum[2][1] = getNum();
	}

}
