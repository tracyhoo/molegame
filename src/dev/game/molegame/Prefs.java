package dev.game.molegame;

import java.text.DecimalFormat;


import dev.game.molegame.util.HandleUtils;
import dev.game.molegame.util.Constants;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Prefs extends Activity implements OnClickListener {

	private SharedPreferences mBaseSettings;
	private static final String TAG = "Prefs";

//	private SharedPreferences mRankingSettings;

//	private EditText mUserNameEditText;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.options);

		Log.d(TAG,"1");
		mBaseSettings = getSharedPreferences(
				Constants.PREFERENCE_MOLEGAME_BASE_INFO, 0);
/*		mRankingSettings = getSharedPreferences(
				Constants.PREFERENCE_MIXEDCOLOR_RANKING_INFO, 0);*/
		Log.d(TAG,"2");
		CheckBox vibrateCheckbox = (CheckBox) findViewById(R.id.options_vibrate_checkbox);
		vibrateCheckbox.setChecked(mBaseSettings.getBoolean(
				Constants.PREFERENCE_KEY_VIBRATE, true));
		Log.d(TAG,"3");
		vibrateCheckbox
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mBaseSettings.edit().putBoolean(
									Constants.PREFERENCE_KEY_VIBRATE, true)
									.commit();
						} else {
							mBaseSettings
									.edit()
									.putBoolean(
											Constants.PREFERENCE_KEY_VIBRATE,
											false).commit();
						}
					}
				});
		Log.d(TAG,"4");
		CheckBox soundsCheckbox = (CheckBox) findViewById(R.id.options_sounds_checkbox);
		soundsCheckbox.setChecked(mBaseSettings.getBoolean(
				Constants.PREFERENCE_KEY_SOUNDS, true));
		soundsCheckbox
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mBaseSettings.edit().putBoolean(
									Constants.PREFERENCE_KEY_SOUNDS, true)
									.commit();
						} else {
							mBaseSettings.edit().putBoolean(
									Constants.PREFERENCE_KEY_SOUNDS, false)
									.commit();
						}
					}
				});

		CheckBox showTipsCheckbox = (CheckBox) findViewById(R.id.options_showtips_checkbox);
		showTipsCheckbox.setChecked(mBaseSettings.getBoolean(
				Constants.PREFERENCE_KEY_SHOWTIPS, true));
		showTipsCheckbox
				.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mBaseSettings
									.edit()
									.putBoolean(
											Constants.PREFERENCE_KEY_SHOWTIPS,
											true).commit();
						} else {
							mBaseSettings.edit().putBoolean(
									Constants.PREFERENCE_KEY_SHOWTIPS,
									false).commit();
						}
					}
				});
		Log.d(TAG,"5");
/*		mUserNameEditText = (EditText) findViewById(R.id.options_username_edittext);*/
/*		mUserNameEditText.setText(mRankingSettings.getString(
				Constants.PREFERENCE_KEY_RANKING_NAME, ""));*/

		DecimalFormat formatter = HandleUtils.getDecimalFormatter();
/*		TextView bestRecordTextView = (TextView) findViewById(R.id.options_best_record_textview);
		bestRecordTextView
				.setText(bestRecordTextView.getText()
						+ "   "
						+ formatter
								.format(mRankingSettings
										.getFloat(
												Constants.PREFERENCE_KEY_RANKING_RECORD,
												30)) + "s");*/
		Log.d(TAG,"6");
		Button okayButton = (Button) findViewById(R.id.options_okay_button);
	//	okayButton.setOnClickListener(this);
		Log.d(TAG,"7");
		Button cancelButton = (Button) findViewById(R.id.options_cancel_button);
	//	cancelButton.setOnClickListener(this);
		Log.d(TAG,"8");
	}


	public void onClick(View v) {
/*		String userName = null;
		if (mUserNameEditText.getText() != null) {
			userName = mUserNameEditText.getText().toString()
					.replace("\n", " ").trim();
		}
		if (userName == null || "".equals(userName)) {
			showToast(R.string.options_toast_username_null);
			return;
		} else if (userName.length() > 20) {
			showToast(R.string.options_toast_username_too_long);
			return;
		}*/
/*		mRankingSettings.edit().putString(
				Constants.PREFERENCE_KEY_RANKING_NAME, userName).commit();
		switch (v.getId()) {
		case R.id.options_okay_button:
			finish();
			break;
		case R.id.options_upload_score_button:
			mRankingSettings.edit().putBoolean(
					Constants.PREFERENCE_KEY_RANKING_FLAG,
					!mRankingSettings.getBoolean(
							Constants.PREFERENCE_KEY_RANKING_FLAG, false))
					.commit();
			break;
		}*/
	}

	private void showToast(int strId) {
		Toast toast = Toast.makeText(this, strId, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 220);
		toast.show();
	}
}
