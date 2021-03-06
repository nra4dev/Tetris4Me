package org.nrasoft.androidapp.mytetris.uc.etc;

import org.nrasoft.androidapp.R;
import org.nrasoft.androidapp.mytetris.uc.game.GameActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

public class DefeatDialog extends Dialog {

	private CharSequence scoreString;
	private CharSequence timeString;
	private CharSequence apmString;
	private long score;
	
	public DefeatDialog(Context context) {
		super(context);
		scoreString = "unknown";
		timeString = "unknown";
		apmString = "unknown";
	}
	
	public void setData(long scoreArg, String time, int apm) {
		scoreString = String.valueOf(scoreArg);
		timeString = time;
		apmString = String.valueOf(apm);
		score = scoreArg;
	}



	//@Override
	public void onCreate(Bundle savedInstance) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(R.string.defeatDialogTitle);
		builder.setMessage(
				getContext().getResources().getString(R.string.scoreLabel) +
				"\n    " + scoreString + "\n\n" +
						getContext().getResources().getString(R.string.timeLabel) +
				"\n    " + timeString + "\n\n" +
						getContext().getResources().getString(R.string.apmLabel) +
				"\n    " + apmString + "\n\n" +
						getContext().getResources().getString(R.string.hint)
				);
		builder.setNeutralButton(R.string.defeatDialogReturn, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((GameActivity)getContext()).putScore(score);
			}
		});
		//return builder.create();
	}
}
