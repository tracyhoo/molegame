package dev.game.molegame.material;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dev.game.molegame.data.HoleData;
import dev.game.molegame.data.MoleData;
import dev.game.molegame.data.RectArea;
import dev.game.molegame.data.SparkData;
import android.util.Log;

public class UIModel {


	public static final int FIELD_VIRGIN = 111;
	public static final int FIELD_MARK = 999;

	public static final int MOLE_IMG_NUM = 7;
	
	
	private int hitCount = 0;
	
	//todo shall this be move into constant?
	public static final int MAX_TIME = 30000;

	public static final int GAME_ATTRIBUTE_TOTAL_LEVEL = 6;

	public int HOLE_AMOUNT_X = 3;
	public int HOLE_AMOUNT_Y = 3;
	public int HOLE_AMOUNT = HOLE_AMOUNT_X * HOLE_AMOUNT_Y;
	public static int MOLE_AMOUNT = 3;

	public static final int GAME_STATUS_PAUSE = 0;
	public static final int GAME_STATUS_RUNNING = 1;
	public static final int GAME_STATUS_GAMEOVER = 2;

	public static final int EFFECT_FLAG_NO_EFFECT = 0;
	public static final int EFFECT_FLAG_PASS = 1;
	public static final int EFFECT_FLAG_TIMEOUT = 2;
	public static final int EFFECT_FLAG_MISS = 3;
	public static final int EFFECT_FLAG_HIT = 4;

	

	
	private Random rand = new Random();

	private int mGameStatus;
	private int mLevel;

	private RectArea mCanvasArea;

	
	private MoleData[] mMoles;
	private HoleData[] mHoles;
	private SparkData mSpark;
	
	private int canvas_width;
	private int canvas_height;	
	private int score_bar_height;
	private int contorl_bar_height;
	private int vertical_gap;
	private int horizental_gap;
	private int hole_width;
	private int hole_height;
	private int mole_width;
	private int mole_height;
	private int spark_height;

	private int mEffectFlag;

	private long mTimeLogger;

	private long mTotalTime;


	public synchronized void updateUIModel() {
		buildStage();
	}
	
	public synchronized void buildStage(){
		long lastTimeLogger = mTimeLogger;
		mTimeLogger = System.currentTimeMillis();
		long timeInterval = mTimeLogger - lastTimeLogger;
		
		if(mTotalTime + timeInterval >= MAX_TIME){
			mGameStatus = GAME_STATUS_GAMEOVER;
		}else{
			mTotalTime += timeInterval;
			
			int holeID;
			
			for(int i=0; i<mMoles.length;i++){
				if(!mMoles[i].isPlaced()) {
                    place(mMoles[i],-1);
                } else{
					holeID = mMoles[i].refresh(timeInterval);
					
					if(holeID != -1){
						mHoles[holeID].unsetOccupied();
						place(mMoles[i],holeID);
					}
				}
			}
		}
	}

	
	public SparkData getSpark(){
		return mSpark;
	}
	
	public void initStage() {
		mTotalTime = 0;
		mTimeLogger = System.currentTimeMillis();

		buildPaintArea();
	}
	
	public void buildPaintArea(){
        //in each repaint, put the exact amount of moles to canvas
		for(int i=0;i<mMoles.length;i++){
			
//			Log.d("-------------i is: ",Integer.toString(i));
			if(!mMoles[i].isPlaced()){
				place(mMoles[i], -1);
			}
		}
	}

	private void place(MoleData m, int n){
		int i = -1;

        //randomly pick an unoccupied hole to place the mole
		i = rand.nextInt(HOLE_AMOUNT);
		
		if(i >= HOLE_AMOUNT){
            i = i % HOLE_AMOUNT;
        }
		
		while(mHoles[i].isOccupied() || i==n){
			i++;
			
			if(i >= HOLE_AMOUNT){
                i = i % HOLE_AMOUNT;
            }
		}

		RectArea retVal = mHoles[i].setOccupied();
		m.imageID = changeImage(m.imageID);

		int offsetX = (int)(hole_width * 0.1);
		
		m.place(i,
				retVal.mMinX + offsetX,
				retVal.mMaxY - mole_height,
				retVal.mMaxX - offsetX,
				retVal.mMaxY);
		
		
/*		int offsetX = (int)(retVal.getWidth() * 0.1);
		int offsetY = (int)(retVal.getHeight() * 0.1);
		m.place(i, 
				retVal.mMinX + offsetX,
				retVal.mMinY + offsetY,
				retVal.mMaxX - offsetX,
				retVal.mMaxY - offsetY);*/
		
	}
	
	public int changeImage(int id){
		int retVal = -1;
		
		do{
			retVal = rand.nextInt(MOLE_IMG_NUM);
		}while(retVal == -1 || retVal == id);
		
		return retVal;
	}

	/*
	 * check what has been hit. 0 for nothing, 1 for a mole, 2 for a bomb
	 */
	public int checkSelection(int x, int y){
		MoleData checkedMole = null;
        int retVal = 0;
		
		for(MoleData curMoleData : mMoles){
			if(curMoleData.mMinX < x && curMoleData.mMaxX > x
					&& curMoleData.mMinY < y && curMoleData.mMaxY > y){
				checkedMole = curMoleData;
			}
		}
		
		if(checkedMole != null){
            if(checkedMole.isBomb()){
                hitCount = hitCount - 2;
                retVal = 2;
            } else{
                hitCount++;
                retVal = 1;
            }
            //free up the space
			int holeID = checkedMole.free();
			if(holeID != -1){
				mHoles[holeID].unsetOccupied();
			}

            //place spark to indicate A hit
			mSpark = new SparkData(mHoles[holeID].mMinX,
					mHoles[holeID].mMinY,
					mHoles[holeID].mMaxX,
					mHoles[holeID].mMinY+spark_height);

            //repaint canvas
			buildStage();
			return retVal;
		}
		return retVal;
	}
	
	public HoleData[] getHoles(){
		return mHoles;
	}
	
	public MoleData[] getMoles(){
		return mMoles;
	}

	public String getHitCount() {
		return Integer.toString(hitCount) + " hits";
	}

	public String getTimeText() {
		String time = String.valueOf((MAX_TIME - mTotalTime) / 1000);
		if (time.length() < 2) {
			time = "0" + time;
		}
		return time + "s";
	}
	
	public float getTimePercent(){
		return 1 - (float) mTotalTime / MAX_TIME;
	}
	
	public int getFinalRecord(){
		return hitCount;
	}	

	public int getStatus() {
		return mGameStatus;
	}

	public int getEffectFlag() {
		try {
			return mEffectFlag;
		} finally {
			mEffectFlag = EFFECT_FLAG_NO_EFFECT;
		}
	}
	
	private void initLevelParameters(){
		switch(mLevel){
		case 0:
			HOLE_AMOUNT_X = 3;
			HOLE_AMOUNT_Y = 2;
			HOLE_AMOUNT = 6;
			MOLE_AMOUNT = 1;
			break;
		case 1:
			HOLE_AMOUNT_X = 3;
			HOLE_AMOUNT_Y = 2;
			HOLE_AMOUNT = 6;
			MOLE_AMOUNT = 2;
			break;
		case 2:
			HOLE_AMOUNT_X = 3;
			HOLE_AMOUNT_Y = 3;
			HOLE_AMOUNT = 9;
			MOLE_AMOUNT = 2;
			break;
		case 3:
			HOLE_AMOUNT_X = 3;
			HOLE_AMOUNT_Y = 3;
			HOLE_AMOUNT = 9;
			MOLE_AMOUNT = 3;
			break;
		case 4:
			HOLE_AMOUNT_X = 3;
			HOLE_AMOUNT_Y = 3;
			HOLE_AMOUNT = 9;
			MOLE_AMOUNT = 3;
			break;
		case 5:
			HOLE_AMOUNT_X = 3;
			HOLE_AMOUNT_Y = 3;
			HOLE_AMOUNT = 9;
			MOLE_AMOUNT = 3;
			break;
		case 6:
			HOLE_AMOUNT_X = 3;
			HOLE_AMOUNT_Y = 4;
			HOLE_AMOUNT = 12;
			MOLE_AMOUNT = 3;
			break;
		case 7:
			HOLE_AMOUNT_X = 3;
			HOLE_AMOUNT_Y = 4;
			HOLE_AMOUNT = 12;
			MOLE_AMOUNT = 4;
			break;
		case 8:
			HOLE_AMOUNT_X = 3;
			HOLE_AMOUNT_Y = 4;
			HOLE_AMOUNT = 12;
			MOLE_AMOUNT = 4;
			break;
		}
	}
	
	public UIModel(RectArea canvasArea, int level) {
		
		mCanvasArea = canvasArea;
		mLevel = level;
		
		initLevelParameters();
		
		mMoles = new MoleData[MOLE_AMOUNT];
		mHoles = new HoleData[HOLE_AMOUNT];
		
		canvas_width = canvasArea.mMaxX - canvasArea.mMinX;
		canvas_height = canvasArea.mMaxY - canvasArea.mMinY;
		
		score_bar_height = (int)(canvas_height * 0.15);
		contorl_bar_height = (int)(canvas_height * 0.15);
		vertical_gap = (int)(canvas_height * 0.025);
		horizental_gap = (int)(canvas_width * 0.07);
		
		hole_width = (int)(canvas_width * 0.24);
		hole_height = (int)(canvas_height * 0.14);
//		hole_height = (int)(canvas_height * 0.2);
//		Log.d("canvas_width:     ",Integer.toString(canvas_width));
//		Log.d("canvas_height:    ",Integer.toString(canvas_height));

//		spark_height = (int)(hole_height * 0.3);
		spark_height = (int)(canvas_height * 0.06);
		mole_width = (int)(hole_width * 0.8);
		mole_height = (int)(canvas_height * 0.16);
		
		int posOffsetX;
		int posOffsetY;

        //place holes
		for (int i = 0; i < HOLE_AMOUNT; i++) {
			posOffsetX = i % HOLE_AMOUNT_X;
			posOffsetY = i / HOLE_AMOUNT_X;
			
			mHoles[i] = new HoleData(i,
					posOffsetX * hole_width 
					+ horizental_gap * (posOffsetX + 1),
					
					score_bar_height + posOffsetY * hole_height
					+ vertical_gap * (posOffsetY + 1),
					
					(posOffsetX + 1) * (horizental_gap + hole_width),
					
					score_bar_height 
					+ (posOffsetY + 1) * (vertical_gap + hole_height)
					);
		
/*			Log.d("hole "+Integer.toString(i)+" position:    ",
					Integer.toString(mHoles[i].mMinX)+","+
			Integer.toString(mHoles[i].mMinY)+","+
							Integer.toString(mHoles[i].mMaxX)+","+
			Integer.toString(mHoles[i].mMaxY));*/

		}

		for(int i =0;i<MOLE_AMOUNT;i++){
			mMoles[i] = new MoleData(MOLE_AMOUNT);
		}
		initStage();
		
		mGameStatus = GAME_STATUS_RUNNING;
		mEffectFlag = EFFECT_FLAG_NO_EFFECT;
	}

}
