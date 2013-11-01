package dev.game.molegame.data;

public class HoleData {
	
	
	public int mMinX;


	public int mMinY;


	public int mMaxX;


	public int mMaxY;
	
	private boolean isOccupied;
	private int mID;
	
	public HoleData(int id, int minX, int minY, int maxX, int maxY){
		mID = id;
		isOccupied = false;
		mMinX = minX;
		mMinY = minY;
		mMaxX = maxX;
		mMaxY = maxY;
	}
	
	public int getID(){
		return mID;
	}
	
	public boolean isOccupied(){
		return isOccupied;
	}
	
	public RectArea setOccupied(){
		isOccupied = true;
		return new RectArea(mMinX, mMinY, mMaxX, mMaxY);
	}
	
	public void unsetOccupied(){
		isOccupied = false;
	}
}
