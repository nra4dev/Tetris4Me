package org.nrasoft.androidapp.mytetris.uc.game.pieces;

import org.nrasoft.androidapp.R;
import org.nrasoft.androidapp.mytetris.uc.game.components.inner.Square;

import android.content.Context;

public class LPiece extends Piece3x3 {

	private Square lSquare;
	
	public LPiece(Context c) {
		super(c);
		lSquare = new Square(Piece.type_L,c);
		num = c.getResources().getInteger(R.integer.LPiece_num);
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
		pattern[1][0] = lSquare;
		pattern[1][1] = lSquare;
		pattern[1][2] = lSquare;
		pattern[2][0] = lSquare;
		patternNum[1][0] = getNum();
		patternNum[1][1] = getNum();
		patternNum[1][2] = getNum();
		patternNum[2][0] = getNum();
	}

}
