package org.nrasoft.androidapp.mytetris.uc.game.pieces;

import org.nrasoft.androidapp.R;
import org.nrasoft.androidapp.mytetris.uc.game.components.inner.Square;

import android.content.Context;

public class JPiece extends Piece3x3 {

	private Square jSquare;
	
	public JPiece(Context c) {
		super(c);
		jSquare = new Square(Piece.type_J,c);
		num = c.getResources().getInteger(R.integer.JPiece_num);
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
		pattern[1][0] = jSquare;
		pattern[1][1] = jSquare;
		pattern[1][2] = jSquare;
		pattern[2][2] = jSquare;
		patternNum[1][0] = getNum();
		patternNum[1][1] = getNum();
		patternNum[1][2] = getNum();
		patternNum[2][2] = getNum();;
	}


}
