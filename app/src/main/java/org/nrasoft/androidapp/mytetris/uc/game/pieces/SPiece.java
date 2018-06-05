package org.nrasoft.androidapp.mytetris.uc.game.pieces;

import org.nrasoft.androidapp.R;
import org.nrasoft.androidapp.mytetris.uc.game.components.inner.Square;

import android.content.Context;

public class SPiece extends Piece3x3 {

	private Square sSquare;
	
	public SPiece(Context c) {
		super(c);
		sSquare = new Square(Piece.type_S,c);
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
		pattern[1][1] = sSquare;
		pattern[1][2] = sSquare;
		pattern[2][0] = sSquare;
		pattern[2][1] = sSquare;
		patternNum[1][1] = getNum();
		patternNum[1][2] = getNum();
		patternNum[2][0] = getNum();
		patternNum[2][1] = getNum();
	}

}
