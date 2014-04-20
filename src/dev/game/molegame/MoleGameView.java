package dev.game.molegame;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


import dev.game.molegame.data.DataHelper;
import dev.game.molegame.data.HoleData;
import dev.game.molegame.data.MoleData;
import dev.game.molegame.data.RectArea;
import dev.game.molegame.data.SparkData;
import dev.game.molegame.material.UIModel;
import dev.game.molegame.util.Util;
import dev.game.molegame.util.Constants;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MoleGameView extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String TAG = "MoleGameView";
    private static final String HANDLE_MESSAGE_FINAL_RECORD = "1";

    private static final int HIT_A_MOLE = 1;
    private static final int HIT_A_BOMB = 2;
    private static final int TIME_UP = 3;

    private Context mContext;
    private Handler mHandler;


    private GameThread mUIThread;

    private Drawable mTimeTotalImage;
    private Drawable mTimeExpendImage;
    private Bitmap mBgImage;
    private Drawable mHoleImage;
    private Drawable[] mMoleImage;
    private Drawable mSparkImage;
    private boolean drawSpark;
    private int mLevel;


    private RectArea mPaintArea;

    private boolean mVibratorFlag;

    private boolean mSoundsFlag;

    private Vibrator mVibrator;

    private SoundPool soundPool;

    private HashMap<Integer, Integer> soundPoolMap;

    private Map<Integer, Drawable> moleMap;

    private DataHelper dh;

    private int mScore;


    private Paint mGameMsgRightPaint;
    private Paint mGameMsgLeftPaint;


    public MoleGameView(Context context, int level) {
        super(context);

        mContext = context;
        mLevel = level;

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        dh = new DataHelper(context);

        mHandler = new Handler() {

            public void handleMessage(Message m) {

                //decide add/deduct score based on the actual event happened
                switch(m.what){
                    case HIT_A_MOLE:
                        vibrate();
                        //make some sound
                        playSoundEffect(UIModel.EFFECT_FLAG_MISS);
                        //play animation and then remove the mole
                        return;
                    case HIT_A_BOMB:
                        vibrate();
                        //todo do sth for hit a bomb
                        break;
                    case TIME_UP:
/*					SharedPreferences rankingSettings = mContext
					.getSharedPreferences(
							MixedConstant.PREFERENCE_MIXEDCOLOR_RANKING_INFO,
							0);*/

                        mScore = m.getData().getInt(HANDLE_MESSAGE_FINAL_RECORD);
                        saveLevel(mScore);


                        LayoutInflater factory = LayoutInflater.from(mContext);
                        View dialogView = factory.inflate(R.layout.score_post_panel, null);
                        dialogView.setFocusableInTouchMode(true);
                        dialogView.requestFocus();

                        final EditText usernameEditText = (EditText) dialogView.findViewById(R.id.namefield);
/*			usernameEditText.setText(rankingSettings.getString(
					MixedConstant.PREFERENCE_KEY_RANKING_NAME, ""));*/

                        DecimalFormat formatter = Util.getDecimalFormatter();

                        TextView curRecordView = (TextView) dialogView.findViewById(R.id.current_record);
                        curRecordView.setText(mContext.getResources().getString(
                                R.string.gameover_dialog_textview_current_record) + "   " + formatter.format(mScore));

                        TextView bestRecordView = (TextView) dialogView.findViewById(R.id.best_record);
                        bestRecordView.setText(mContext.getResources().getString(
                                R.string.gameover_dialog_textview_best_record)
                                + "   "
                                + dh.getBest());


                        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(dialogView).create();
                        if (dh.isNewHighScore(mScore)) {
                            dialog.setIcon(R.drawable.tip_new_record);
                            dialog.setTitle(R.string.gameover_dialog_text_newrecord);
                        } else {
                            if (mScore <10) {
                                dialog.setIcon(R.drawable.tip_pool_guy);
                                dialog.setTitle(R.string.gameover_dialog_text_poolguy);
                            } else if (mScore <20) {
                                dialog.setIcon(R.drawable.tip_not_bad);
                                dialog.setTitle(R.string.gameover_dialog_text_notbad);
                            } else {
                                dialog.setIcon(R.drawable.tip_awesome);
                                dialog.setTitle(R.string.gameover_dialog_text_awesome);
                            }
                        }
                        dialog.show();
                        dialogView.findViewById(R.id.retry).setOnClickListener(
                                new OnClickListener() {

                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        restartGame();
                                    }
                                });

                        dialogView.findViewById(R.id.post_scores).setOnClickListener(
                                new OnClickListener() {

                                    public void onClick(View v) {
                                        String userName = null;
                                        if (usernameEditText.getText() != null) {
                                            userName = usernameEditText.getText().toString().replace("\n", " ").trim();
                                        }
                                        if (userName.length() > 0 && userName.length() < 20) {

                                            dh.update(dh.new ScorePair(userName, mScore));
                                            dialog.dismiss();
                                            ((MoleGameActivity) mContext).finish();
                                        } else {

                                            showToast(R.string.options_toast_username_too_long);
                                        }
                                    }
                                });

                        break;

                    default:

                }

            }
        };

        initResource();
        mUIThread = new GameThread(holder, context, mHandler);
        setFocusable(true);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    /**
     * play vibration
     */
    public void vibrate(){
        if (mVibrator == null) {
            mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        }
        mVibrator.vibrate(40);
    }

    public void saveLevel(int score){
        SharedPreferences settings = mContext.getSharedPreferences(Constants.LEVEL, 0);
        int level = settings.getInt(Constants.LEVEL, 0);

        Log.d("level for now is:",Integer.toString(level));

        //check if user can make to the next level
        if(mLevel == level && mLevel < Constants.LEVELS && score >= Constants.QUALIFICATION[level]){

            Log.d("save level ",Integer.toString(level+1));
            //todo shall display some INFO if user upgrade to next level
            settings.edit().putInt(Constants.LEVEL, mLevel+1).commit();
        }
    }



    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        mPaintArea = new RectArea(0, 0, width, height);
        mBgImage = mUIThread.initUIModel(mPaintArea);
        mUIThread.setRunning(true);
        mUIThread.start();
    }


    public void surfaceCreated(SurfaceHolder holder) {
    }


    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        mUIThread.setRunning(false);
        while (retry) {
            try {
                mUIThread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.d("", "Surface destroy failure:", e);
            }
        }
    }


    public boolean onTouchEvent(MotionEvent event) {
        //when player clicked any place in the game, check if this press actually hit a mole
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mUIThread.checkSelection(
                    (int) event.getX(),
                    (int) event.getY());
        }
        return true;
    }

    public void restartGame() {
        mUIThread = new GameThread(this.getHolder(), this.getContext(), mHandler);
        mUIThread.initUIModel(mPaintArea);
        mUIThread.setRunning(true);
        mUIThread.start();
    }


    private void initResource() {
        mBgImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.game_bg1);
        mHoleImage = mContext.getResources().getDrawable(R.drawable.root);

        //set the number of moles to show depending on current level
        mMoleImage = new Drawable[Constants.MOLE_AMOUNT_BY_LEVEL[mLevel]];


        mTimeTotalImage = mContext.getResources().getDrawable(R.drawable.time_total);
        mTimeExpendImage = mContext.getResources().getDrawable(R.drawable.time_expend);

        //todo need refactor
        mGameMsgRightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGameMsgRightPaint.setColor(Color.BLUE);
        mGameMsgRightPaint.setStyle(Style.FILL);
        mGameMsgRightPaint.setTextSize(17f);
        mGameMsgRightPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mGameMsgRightPaint.setTextAlign(Paint.Align.RIGHT);

        mGameMsgLeftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGameMsgLeftPaint.setColor(Color.BLUE);
        mGameMsgLeftPaint.setStyle(Style.FILL);
        mGameMsgLeftPaint.setTextSize(17f);
        mGameMsgLeftPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mGameMsgLeftPaint.setTextAlign(Paint.Align.LEFT);


        mSparkImage = mContext.getResources().getDrawable(R.drawable.light);

        Resources res = mContext.getResources();

        moleMap = new HashMap<Integer,Drawable>();
        moleMap.put(0, mContext.getResources().getDrawable(R.drawable.blue_mole));
        moleMap.put(1, mContext.getResources().getDrawable(R.drawable.dark_blue_mole));
        moleMap.put(2, mContext.getResources().getDrawable(R.drawable.green_mole));
        moleMap.put(3, mContext.getResources().getDrawable(R.drawable.killer_mole));
        moleMap.put(4, mContext.getResources().getDrawable(R.drawable.purple_mole));
        moleMap.put(5, mContext.getResources().getDrawable(R.drawable.red_mole));
        moleMap.put(6, mContext.getResources().getDrawable(R.drawable.bomb));



        SharedPreferences baseSettings = mContext.getSharedPreferences(
                Constants.PREFERENCE_MOLEGAME_BASE_INFO, 0);
        mSoundsFlag = baseSettings.getBoolean(Constants.PREFERENCE_KEY_SOUNDS, true);
        mVibratorFlag = baseSettings.getBoolean(Constants.PREFERENCE_KEY_VIBRATE, true);

        soundPool = new SoundPool(10, AudioManager.STREAM_RING, 5);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(UIModel.EFFECT_FLAG_MISS, soundPool.load(getContext(), R.raw.miss, 1));
        soundPoolMap.put(UIModel.EFFECT_FLAG_PASS, soundPool.load(getContext(), R.raw.pass, 1));
        soundPoolMap.put(UIModel.EFFECT_FLAG_TIMEOUT, soundPool.load( getContext(), R.raw.timeout, 1));
        soundPoolMap.put(UIModel.EFFECT_FLAG_HIT, soundPool.load(getContext(), R.raw.hit, 1));
    }

    private void showToast(int strId) {
        Toast toast = Toast.makeText(mContext, strId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 220);
        toast.show();
    }

    // thread for updating UI
    class GameThread extends Thread {

        private SurfaceHolder mSurfaceHolder;

        private Context mContext;

        private Handler mHandler;

        private boolean mRun = true;

        private UIModel mUIModel;

        public GameThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
            mSurfaceHolder = surfaceHolder;
            mContext = context;
            mHandler = handler;
        }

        @Override
        public void run() {

            while (mRun) {

                //update UI
                Canvas c = null;
                try {
                    mUIModel.updateUIModel();

                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        doDraw(c);
                    }

                    //		handleEffect(mUIModel.getEffectFlag());
                    Thread.sleep(100);

                } catch (Exception e) {
                    Log.d("", "Error at 'run' method", e);
                } finally {
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }

                //if game over,send message notify UI thread
                if (mUIModel.getStatus() == UIModel.GAME_STATUS_GAMEOVER) {
                    Message message = new Message();
                    message.what = TIME_UP;

                    Bundle bundle = new Bundle();
                    bundle.putInt(MoleGameView.HANDLE_MESSAGE_FINAL_RECORD, mUIModel.getFinalRecord());
                    message.setData(bundle);
                    mHandler.sendMessage(message);
                    mRun = false;
                }
            }
        }

        //render holes and moles onto canvas
        private void doDraw(Canvas canvas){

            canvas.drawBitmap(mBgImage, 0, 0, null);

            UIModel uiModel = mUIModel;

            //render time
            mTimeExpendImage.setBounds(mPaintArea.mMaxX / 2 - 80, 15,
                    (int) (mPaintArea.mMaxX / 2 - 80 + 160 * uiModel.getTimePercent()), 25);
            mTimeExpendImage.draw(canvas);

            //render holes
/*			HoleData[] holes = uiModel.getHoles();
			for (HoleData currentHole : holes) {
	//			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
				
				mHoleImage.setBounds(currentHole.mMinX, 
						currentHole.mMinY,
						currentHole.mMaxX,
						currentHole.mMaxY);
				mHoleImage.draw(canvas);
			}		*/

            //render moles
            MoleData[] moles = uiModel.getMoles();
            int i =0;
            for(MoleData currentMole : moles){

                mMoleImage[i] = moleMap.get(currentMole.imageID);
                mMoleImage[i].setBounds(currentMole.mMinX,
                        currentMole.mMinY,
                        currentMole.mMaxX,
                        currentMole.mMaxY);
                mMoleImage[i].draw(canvas);
                i++;
            }

            if(drawSpark){
                SparkData sd = uiModel.getSpark();
                mSparkImage.setBounds(sd.mMinX, sd.mMinY, sd.mMaxX, sd.mMaxY);
                mSparkImage.draw(canvas);
                drawSpark = false;
            }
/*			
			SparkData[] sparks = uiModel.getSparks();
			for(SparkData currentSpark : sparks){
				if(currentSpark.visible){
					mSparkImage.setBounds(currentSpark.mMinX,
							currentSpark.mMinY, 
							currentSpark.mMaxX, 
							currentSpark.mMaxY);
					mSparkImage.draw(canvas);
				}
			}*/

            FontMetrics fmsr = mGameMsgLeftPaint.getFontMetrics();
            canvas.drawText(uiModel.getHitCount(), 5, (float) 15 - (fmsr.ascent + fmsr.descent), mGameMsgLeftPaint);
/*			Log.d("y:  ---------",Float.toString((float) 15
					- (fmsr.ascent + fmsr.descent)));*/

            fmsr = mGameMsgRightPaint.getFontMetrics();
            canvas.drawText(uiModel.getTimeText(), mPaintArea.mMaxX - 5, (float) 15 - (fmsr.ascent + fmsr.descent), mGameMsgRightPaint);
/*			Log.d("x:-------------",Float.toString(mPaintArea.mMaxX - 5));*/

        }



        public Bitmap initUIModel(RectArea paintArea) {
            if (mUIModel != null) {
                mRun = false;
            }

            Bitmap bmp = Bitmap.createBitmap(paintArea.mMaxX, paintArea.mMaxY, Config.ARGB_8888);

            mUIModel = new UIModel(paintArea, mLevel);
            mBgImage = Bitmap.createScaledBitmap(mBgImage, paintArea.mMaxX, paintArea.mMaxY, true);

            drawSpark = false;

            Canvas canvas;
            try {
                canvas = new Canvas(bmp);

                canvas.drawBitmap(mBgImage, 0, 0, null);

                UIModel uiModel = mUIModel;

                mTimeTotalImage.setBounds(mPaintArea.mMaxX / 2 - 80, 15,
                        mPaintArea.mMaxX / 2 + 80, 25);
                mTimeTotalImage.draw(canvas);
                //render holes
                HoleData[] holes = uiModel.getHoles();
                for (HoleData currentHole : holes) {

                    mHoleImage.setBounds(currentHole.mMinX,
                            currentHole.mMinY,
                            currentHole.mMaxX,
                            currentHole.mMaxY);
                    mHoleImage.draw(canvas);
                }

            }catch (Exception e) {
                Log.d("", "Error at 'run' method", e);
            }

            return bmp;
        }

        /**
         * check x and y axis to decide on which event happened
         * @param x
         * @param y
         */
        public void checkSelection(int x, int y) {
            int val = mUIModel.checkSelection(x, y);

            Message message = new Message();

            if(val == 1){
                //show spark
                drawSpark = true;

                message.what = HIT_A_MOLE;
                playSoundEffect(UIModel.EFFECT_FLAG_HIT);

            }else if(val == 2){
                //todo this part shall change to hit a bomb
                drawSpark = true;

                message.what = HIT_A_BOMB;
                //todo play different sound
                playSoundEffect(UIModel.EFFECT_FLAG_HIT);
            }else{
                return;
            }

            mHandler.sendMessage(message);
        }

        /*
         * handle sound and vibration effect
         */
        private void handleEffect(int effectFlag) {
            if (effectFlag == UIModel.EFFECT_FLAG_NO_EFFECT){
                return;
            }


/*			if (mSoundsFlag) {
				playSoundEffect(effectFlag);
			}*/

            if (mVibratorFlag) {
                if (effectFlag == UIModel.EFFECT_FLAG_PASS) {
                    if (mVibrator == null) {
                        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                    }
                    mVibrator.vibrate(50);
                }
            }
        }


        private void playSoundEffect(int soundId) {
            try {
                AudioManager mgr = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_RING);
                float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_RING);
                float volume = streamVolumeCurrent / streamVolumeMax;
                soundPool.play(soundPoolMap.get(soundId), 100, volume, 1, 0, 1f);
            } catch (Exception e) {
                Log.d("PlaySounds", e.toString());
            }
        }

        public void setRunning(boolean run) {
            mRun = run;
        }
    }// Thread

}
