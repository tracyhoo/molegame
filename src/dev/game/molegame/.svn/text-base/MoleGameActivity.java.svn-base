package dev.game.molegame;


import dev.game.molegame.util.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class MoleGameActivity extends Activity {
	MoleGameView gameView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Bundle extras = getIntent().getExtras();
		int level = extras.getInt(Constants.CURRENT_LEVEL);
		
		gameView = new MoleGameView(this, level);
		setContentView(gameView);
/*		setContentView(R.layout.mole_game);
		*/
		
	}
}
