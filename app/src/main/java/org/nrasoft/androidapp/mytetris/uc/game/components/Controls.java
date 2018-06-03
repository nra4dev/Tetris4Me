package org.nrasoft.androidapp.mytetris.uc.game.components;




import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

import org.nrasoft.androidapp.R;
import org.nrasoft.androidapp.mytetris.uc.game.GameActivity;
import org.nrasoft.androidapp.mytetris.uc.game.pieces.Piece;

public class Controls extends Component {

	// Constants

	// Stuff
	private Board board;
	//private boolean initialized;

	private Vibrator v;
	private int vibrationOffset;
	private long shortVibeTime;
	private int[] lineThresholds;

	// Player Controls
	private boolean playerSoftDrop;
	private boolean clearPlayerSoftDrop;
	private boolean playerHardDrop;
	private boolean leftMove;
	private boolean rightMove;
	private boolean continuousSoftDrop;
	private boolean continuousLeftMove;
	private boolean continuousRightMove;
	private boolean clearLeftMove;
	private boolean clearRightMove;
	private boolean leftRotation;
	private boolean rightRotation;
	private boolean buttonVibrationEnabled;
	private boolean eventVibrationEnabled;
	private int initialHIntervalFactor;
	private int initialVIntervalFactor;
	private Rect previewBox;
	private boolean boardTouched;



	public Controls(GameActivity ga) {
		super(ga);


		lineThresholds = host.getResources().getIntArray(R.array.line_thresholds);
		shortVibeTime = 0;

		v = (Vibrator) host.getSystemService(Context.VIBRATOR_SERVICE);

		buttonVibrationEnabled = PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_vibration_button", false);
		eventVibrationEnabled = PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_vibration_events", false);
		try {
			vibrationOffset = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(host).getString("pref_vibDurOffset", "0"));
		} catch(NumberFormatException e) {
			vibrationOffset = 0;
		}
		if(PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_accelerationH", true))
			initialHIntervalFactor = 2;
		else
			initialHIntervalFactor = 1;
		if(PreferenceManager.getDefaultSharedPreferences(host).getBoolean("pref_accelerationV", true))
			initialVIntervalFactor = 2;
		else
			initialVIntervalFactor = 1;
		playerSoftDrop = false;
		leftMove = false;
		rightMove = false;
		leftRotation = false;
		rightRotation = false;
		clearLeftMove = false;
		clearRightMove = false;
		clearPlayerSoftDrop = false;
		continuousSoftDrop = false;
		continuousLeftMove = false;
		continuousRightMove = false;
		previewBox = null;
		boardTouched = false;
	}

	public void vibrateWall() {
		if (v == null)
			return;
		if (!eventVibrationEnabled)
			return;
		if(((AudioManager)host.getSystemService(Context.AUDIO_SERVICE)).getRingerMode() == AudioManager.RINGER_MODE_SILENT)
			return;
		v.vibrate(host.state.getMoveInterval() + vibrationOffset);
	}

	public void cancelVibration() {
		v.cancel();
	}

	public void vibrateBottom() {
		Log.d("NRA", "Controls.vibrateBottom()");
		if (v == null)
			return;
		if (!eventVibrationEnabled)
			return;
		if(((AudioManager)host.getSystemService(Context.AUDIO_SERVICE)).getRingerMode() == AudioManager.RINGER_MODE_SILENT)
			return;
		v.cancel();
		v.vibrate(new long[] {0, 5 + vibrationOffset, 30 + vibrationOffset, 20 + vibrationOffset}, -1);
	}

	public void vibrateShort() {
		if (v == null)
			return;
		if (!buttonVibrationEnabled)
			return;
		if(((AudioManager)host.getSystemService(Context.AUDIO_SERVICE)).getRingerMode() == AudioManager.RINGER_MODE_SILENT)
			return;
		if((host.state.getTime() - shortVibeTime) > (host.getResources().getInteger(R.integer.shortVibeInterval) + vibrationOffset)) {
			shortVibeTime = host.state.getTime();
			v.vibrate(vibrationOffset);
		}
	}

	public void rotateLeftPressed() {
		leftRotation = true;
		host.state.action();
		vibrateShort();
		host.sound.buttonSound();
		//Thread.yield();
	}

	public void rotateLeftReleased() {
		//Thread.yield();
	}

	public void rotateRightPressed() {
		rightRotation = true;
		host.state.action();
		vibrateShort();
		host.sound.buttonSound();
		//Thread.yield();
	}

	public void rotateRightReleased() {
		//Thread.yield();
	}

	public void downButtonReleased() {
		clearPlayerSoftDrop = true;
		vibrateShort();
		//Thread.yield();
	}

	public void downButtonPressed() {
		host.state.action();
		playerSoftDrop = true;
		clearPlayerSoftDrop = false;
		vibrateShort();
		host.state.setNextPlayerDropTime(host.state.getTime());
		host.sound.buttonSound();
	}

	public void dropButtonReleased() {}

	public void dropButtonPressed() {
		if(!host.state.getActivePiece().isActive())
			return;
		host.state.action();
		playerHardDrop = true;
		if(buttonVibrationEnabled & !eventVibrationEnabled)
			vibrateShort();
	}

	public void leftButtonReleased() {
		clearLeftMove = true;
		cancelVibration();
	}

	public void leftButtonPressed() {
		host.state.action();
		clearLeftMove = false;
		leftMove = true;
		rightMove = false;
		host.state.setNextPlayerMoveTime(host.state.getTime());
		host.sound.buttonSound();
	}

	public void rightButtonReleased() {
		clearRightMove = true;
		cancelVibration();
	}

	public void rightButtonPressed() {
		host.state.action();
		clearRightMove = false;
		rightMove = true;
		leftMove = false;
		host.state.setNextPlayerMoveTime(host.state.getTime());
		host.sound.buttonSound();
	}

	public void cycle(long tempTime) {
		long gameTime = host.state.getTime();
		Piece active = host.state.getActivePiece();
		Board board = host.state.getBoard();
		int maxLevel = host.state.getMaxLevel();
		boolean actionResult;

		// Left Rotation
		if(leftRotation) {
			leftRotation = false;
			Log.d("NRA", "before turnLeft");
			Log.d("NRA", active.toString());
			actionResult = active.turnLeft(board);
			Log.d("NRA", "after turnLeft");
			Log.d("NRA", active.toString());
			if (actionResult) {
				board.getModel().updateGridActiveValueMatrix(active);
			}
			host.display.invalidatePhantom();
		}


		// Right Rotation
		if(rightRotation) {
			rightRotation = false;
			Log.d("NRA", "before turnRight");
			Log.d("NRA", active.toString());
			actionResult = active.turnRight(board);
			Log.d("NRA", "after turnRight");
			Log.d("NRA", active.toString());
			if (actionResult) {
				board.getModel().updateGridActiveValueMatrix(active);
			}

			host.display.invalidatePhantom();
		}


		// Reset Move Time
		if((!leftMove && !rightMove) && (!continuousLeftMove && !continuousRightMove))
			host.state.setNextPlayerMoveTime(gameTime);


		// Left Move
		if(leftMove) {
			continuousLeftMove = true;
			leftMove = false;
			if(active.moveLeft(board)) { // successful move
				vibrateShort();
				host.display.invalidatePhantom();
				host.state.setNextPlayerMoveTime(host.state.getNextPlayerMoveTime() + initialHIntervalFactor*host.state.getMoveInterval());
			} else { // failed move
				vibrateWall();
				host.state.setNextPlayerMoveTime(gameTime);
			}


		} else if(continuousLeftMove) {
			if(gameTime >= host.state.getNextPlayerMoveTime()) {
				if(active.moveLeft(board)) { // successful move
					vibrateShort();
					host.display.invalidatePhantom();
					host.state.setNextPlayerMoveTime(host.state.getNextPlayerMoveTime() + host.state.getMoveInterval());
				} else { // failed move
					vibrateWall();
					host.state.setNextPlayerMoveTime(gameTime);
				}
			}

			if(clearLeftMove) {
				continuousLeftMove = false;
				clearLeftMove = false;
			}
		}


		// Right Move
		if(rightMove) {
			continuousRightMove = true;
			rightMove = false;
			if(active.moveRight(board)) { // successful move
				vibrateShort();
				host.display.invalidatePhantom();
				host.state.setNextPlayerMoveTime(host.state.getNextPlayerMoveTime() + initialHIntervalFactor*host.state.getMoveInterval());
			} else { // failed move
				vibrateWall();
				host.state.setNextPlayerMoveTime(gameTime); // first interval is doubled!
			}


		} else if(continuousRightMove) {
			if(gameTime >= host.state.getNextPlayerMoveTime()) {
				if(active.moveRight(board)) { // successful move
					vibrateShort();
					host.display.invalidatePhantom();
					host.state.setNextPlayerMoveTime(host.state.getNextPlayerMoveTime() + host.state.getMoveInterval());
				} else { // failed move
					vibrateWall();
					host.state.setNextPlayerMoveTime(gameTime);
				}
			}

			if(clearRightMove) {
				continuousRightMove = false;
				clearRightMove = false;
			}
		}




		// Hard Drop
		if(playerHardDrop) {
			board.interruptClearAnimation();
			int hardDropDistance = active.hardDrop(false, board);
			vibrateBottom();
			host.state.clearLines(true, hardDropDistance);
			host.state.pieceTransition(eventVibrationEnabled);
			board.invalidate();
			playerHardDrop = false;


			if((host.state.getLevel() < maxLevel) && (host.state.getClearedLines() > lineThresholds[Math.min(host.state.getLevel(),maxLevel - 1)]))
				host.state.nextLevel();
			host.state.setNextDropTime(gameTime + host.state.getAutoDropInterval());
			host.state.setNextPlayerDropTime(gameTime);

			// Initial Soft Drop
		} else if(playerSoftDrop) {
			playerSoftDrop = false;
			continuousSoftDrop = true;
			if(!active.drop(board)) {
				// piece finished
				Log.d("NRA", "Initial Soft Drop / piece finished");
				host.state.board.getModel().updateGridValueMatrix();

				vibrateBottom();
				host.state.clearLines(false, 0);
				host.state.pieceTransition(eventVibrationEnabled);
				board.invalidate();
			} else {
				host.state.incSoftDropCounter();
			}
			if((host.state.getLevel() < maxLevel) && (host.state.getClearedLines() > lineThresholds[Math.min(host.state.getLevel(),maxLevel - 1)]))
				host.state.nextLevel();
			host.state.setNextDropTime(host.state.getNextPlayerDropTime() + host.state.getAutoDropInterval());
			host.state.setNextPlayerDropTime(host.state.getNextPlayerDropTime() + initialVIntervalFactor*host.state.getSoftDropInterval());


			// Continuous Soft Drop
		} else if(continuousSoftDrop) {
			if(gameTime >= host.state.getNextPlayerDropTime()) {
				if(!active.drop(board)) {
					// piece finished
					Log.d("NRA", "Continuous Soft Drop / piece finished");
					host.state.board.getModel().updateGridValueMatrix();
					vibrateBottom();
					host.state.clearLines(false, 0);
					host.state.pieceTransition(eventVibrationEnabled);
					board.invalidate();
				} else {
					host.state.incSoftDropCounter();
				}
				if((host.state.getLevel() < maxLevel) && (host.state.getClearedLines() > lineThresholds[Math.min(host.state.getLevel(),maxLevel - 1)]))
					host.state.nextLevel();
				host.state.setNextDropTime(host.state.getNextPlayerDropTime() + host.state.getAutoDropInterval());
				host.state.setNextPlayerDropTime(host.state.getNextPlayerDropTime() + host.state.getSoftDropInterval());


				// Autodrop if faster than playerDrop
			} else if(gameTime >= host.state.getNextDropTime()) {
				if(!active.drop(board)) {
					// piece finished
					Log.d("NRA", "Autodrop if faster than playerDrop / piece finished");
					host.state.board.getModel().updateGridValueMatrix();;
					vibrateBottom();
					host.state.clearLines(false, 0);
					host.state.pieceTransition(eventVibrationEnabled);
					board.invalidate();
				}
				if((host.state.getLevel() < maxLevel) && (host.state.getClearedLines() > lineThresholds[Math.min(host.state.getLevel(),maxLevel - 1)]))
					host.state.nextLevel();
				host.state.setNextDropTime(host.state.getNextDropTime() + host.state.getAutoDropInterval());
				host.state.setNextPlayerDropTime(host.state.getNextDropTime() + host.state.getSoftDropInterval());
			}

			/* Cancel continuous SoftDrop */
			if(clearPlayerSoftDrop) {
				continuousSoftDrop = false;
				clearPlayerSoftDrop = false;
			}


			// Autodrop if no playerDrop
		} else if(gameTime >= host.state.getNextDropTime()) {
			if(!active.drop(board)) {
				// piece finished
				Log.d("NRA", "Autodrop if no playerDrop / piece finished");
				Log.v("NRA", host.state.board.getModel().toString());
				vibrateBottom();
				host.state.clearLines(false, 0);
				host.state.pieceTransition(eventVibrationEnabled);
				board.invalidate();
			}
			if((host.state.getLevel() < maxLevel) && (host.state.getClearedLines() > lineThresholds[Math.min(host.state.getLevel(),maxLevel - 1)]))
				host.state.nextLevel();
			host.state.setNextDropTime(host.state.getNextDropTime() + host.state.getAutoDropInterval());
			host.state.setNextPlayerDropTime(host.state.getNextDropTime());


		} else
			host.state.setNextPlayerDropTime(gameTime);
	}

	public void setBoard(Board instance2) {
		this.board = instance2;
	}

	public Board getBoard() {
		return this.board;
	}

	/**
	 * unused!
	 */
	@Override
	public void reconnect(GameActivity cont) {
		super.reconnect(cont);
		v = (Vibrator) cont.getSystemService(Context.VIBRATOR_SERVICE);

		buttonVibrationEnabled = PreferenceManager.getDefaultSharedPreferences(cont).getBoolean("pref_vibration_button", false);
		eventVibrationEnabled = PreferenceManager.getDefaultSharedPreferences(cont).getBoolean("pref_vibration_events", false);
		try {
			vibrationOffset = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(cont).getString("pref_vibDurOffset", "0"));
		} catch(NumberFormatException e) {
			vibrationOffset = 0;
		}
	}

	/**
	 * unused!
	 */
	@Override
	public void disconnect() {
		super.disconnect();
		v = null;
	}

	public void boardPressed(float x, float y) {
		if(previewBox == null)
			return;

		boardTouched = true;

		if(previewBox.contains((int)x, (int)y))
			host.state.hold();
	}

	public void boardReleased() {
		boardTouched = false;
	}

	public void setPreviewRect(Rect rect) {
		previewBox = rect;
	}

	public boolean isBoardTouched() {
		return boardTouched;
	}

}
