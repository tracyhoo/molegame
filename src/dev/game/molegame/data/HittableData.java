package dev.game.molegame.data;

import android.graphics.RectF;

public class HittableData {
	public static final int PLACE_LIMIT = 1000;
	
	public int mMinX;


	public int mMinY;


	public int mMaxX;

	public int mMaxY;
	
	public int imageID;
	
	private boolean mPlaced;
	private long mPlacedTime;

	private int lastPosition;
	
	
	public boolean isExpired(long interval){
		return (mPlacedTime + interval) >= PLACE_LIMIT;
	}
	

	
	
	public HittableData(int k){
		mPlaced = false;
		mPlacedTime = 0;
		lastPosition = -1;
	}
	
	public int refresh(long interval){
		int retVal = -1;
		
		if(mPlaced){
			if((mPlacedTime + interval) >= PLACE_LIMIT){
				
				mPlacedTime = 0;
				mPlaced = false;
				retVal = mHoleID;
				
				lastPosition = mHoleID;
				
				mHoleID = -1;
				mMinX = 0;
				mMinY = 0;
				mMaxX = 0;
				mMaxY = 0;
			}else{
				mPlacedTime += interval;
			}
		}
		
		return retVal;
	}
	
	public boolean isPlaced(){
		return mPlaced;
	}
	
	public void place(int holeID, int minX, int minY, int maxX, int maxY){
		mPlaced = true;
		mHoleID = holeID;
		mMinX = minX;
		mMinY = minY;
		mMaxX = maxX;
		mMaxY = maxY;

		
	}
	
	
	public int free(){
		mPlaced = false;
		int retVal = mHoleID;
		
		lastPosition = mHoleID;
		
		mHoleID = -1;
		mPlacedTime = 0;
		
		mMinX = 0;
		mMinY = 0;
		mMaxX = 0;
		mMinY = 0;
		
		return retVal;
	}
	
	public int getLastPosition(){
		return lastPosition;
	}
	
	private int mBgImage;
	private int mHoleID;
	
	public int getHoleID(){
		return mHoleID;
	}
	
	
	public RectF getRectF() {
		return new RectF(mMinX, mMinY, mMaxX, mMaxY);
	}
	
	public int getMBgImage() {
		return mBgImage;
	}

	public void setMBgImage(int bgImage) {
		mBgImage = bgImage;
	}
}
