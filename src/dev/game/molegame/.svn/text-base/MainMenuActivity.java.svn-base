package dev.game.molegame;


import dev.game.molegame.util.Constants;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainMenuActivity extends Activity implements OnClickListener {

	private SharedPreferences mBaseSettings;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		Button startButton = (Button) findViewById(R.id.start_game);
		startButton.setOnClickListener(this);

		Button scoreBoardButton = (Button) findViewById(R.id.score_board);
		scoreBoardButton.setOnClickListener(this);
		
		Button optionButton = (Button) findViewById(R.id.options);
		optionButton.setOnClickListener(this);

		Button exitButton = (Button) findViewById(R.id.exit);
		exitButton.setOnClickListener(this);


		mBaseSettings = getSharedPreferences(
				Constants.LEVEL, 0);
	}


	public void finish() {
		super.finish();
	}


	public void onClick(View v) {
		Intent i = null;
		switch (v.getId()) {
		case R.id.start_game:
			int level = mBaseSettings.getInt(Constants.LEVEL, 0);
			i = new Intent(this, ChooseLevelActivity.class);
			i.putExtra(Constants.LEVEL, level);
			break;
		case R.id.options:
			i = new Intent(this, Prefs.class);
			break;
		case R.id.score_board:
			i = new Intent(this, RankingActivity.class);
			break;
		case R.id.exit:
			finish();
			return;
		}
		if (i != null) {
			startActivity(i);
		}
	}


}