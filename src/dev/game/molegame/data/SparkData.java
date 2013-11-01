package dev.game.molegame.data;

public class SparkData {
	
	public static final int PLACE_LIMIT = 500;
	public int mMinX;


	public int mMinY;


	public int mMaxX;


	public int mMaxY;
	public boolean visible;
	private long mPlacedTime;
	
	
	public SparkData(int minX, int minY, int maxX, int maxY){
		mPlacedTime = 0;
		mMinX = minX;
		mMinY = minY;
		mMaxX = maxX;
		mMaxY = maxY;
		visible = false;
	}
	
	public void checkElegible(){
		if(visible){
			mPlacedTime += 100;
			if(mPlacedTime >= PLACE_LIMIT)
				visible = false;
		}

	}
/*	public boolean checkElegible(){
		mPlacedTime += 100;
		if(mPlacedTime <= PLACE_LIMIT)
			return true;
		return false;
	}*/
}
