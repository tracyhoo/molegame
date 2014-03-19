package dev.game.molegame.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static android.provider.BaseColumns._ID;

public class DataHelper {
	private static final String TAG="DATA HELPER";
	private static final String TABLE_NAME = "score";
	private static final String SCORE = "score";
	private static final String NAME = "name";
	private static final String TIME = "time";

	private Context context;
	private SQLiteDatabase db;
	private List<ScorePair> scores;
	private OpenHelper openHelper;

	public DataHelper(Context context) {
		scores = new ArrayList<ScorePair>();
		
		Log.v(TAG,"1");
		this.context = context;
		Log.v(TAG,"2");
		openHelper = new OpenHelper(this.context);
		Log.v(TAG,"3");
		
		getAllRecords();
	}
	
	public void close(){
		this.db.close();
	}

    /**
     * check if this is the new highest score
     * @param score
     * @return
     */
	public boolean isNewHighScore(int score){

		for(ScorePair sp:scores){
			if(sp.score > score){
                return false;
			}
		}
		return true;
	}
	
	public int getBest(){
		return scores.get(0).score;
	}

	public void update(ScorePair pair){
		
		int i=0;
		Log.v(TAG,"score to update is "+Long.toString(pair.score));

        //todo
		while(scores.get(i).score >= pair.score && i <4){
			i++;
		}
		if(i==4&&scores.get(i).score >= pair.score)
			i++;
		
		if(i<5){
			scores.add(i, pair);
			scores.remove(5);
			
			for(i=0;i<5;i++){
				ContentValues values = new ContentValues();
				values.put(SCORE, scores.get(i).score);
				values.put(NAME, scores.get(i).name);
				values.put(TIME, scores.get(i).time);
				
				this.db = openHelper.getWritableDatabase();
				db.update(TABLE_NAME,
						values,
						"_ID=?",
						new String[] {Integer.toString(i+1)});
				this.db.close();
			}
		}
	}



	public void deleteAll() {
		this.db = openHelper.getWritableDatabase();
		this.db.delete(TABLE_NAME, null, null);	
		this.db.close();
	}
	
	public List<ScorePair> getAllRecords() {
		scores.clear();
		this.db = openHelper.getReadableDatabase();
        //todo check better way to use sqlite
		Cursor cursor = this.db.query(
				TABLE_NAME,
				new String[] { "_ID","score","name","time" },
				null, null, null, null, "_ID asc");

		if (cursor.moveToFirst()) {
			do {
				ScorePair sp = new ScorePair();
				sp.score = cursor.getInt(1);
				sp.name = cursor.getString(2);
				sp.time = cursor.getLong(3);
				scores.add(sp);
				Log.v(TAG,"name is"+sp.name);
				Log.v(TAG,"_ID "+cursor.getInt(0));

			} while (cursor.moveToNext());
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		this.db.close();

		return scores;
	}
	
	public class ScorePair{
		public String name;
		public int score;
		public long time;
		
		public ScorePair(String _name,int _score){
			name = _name;
			score = _score;
			time = System.currentTimeMillis();
		}
		
		public ScorePair(){
			
		}
	}

	protected static class OpenHelper extends SQLiteOpenHelper {
		private static final String TAG = "Ranking Open Helper";

		private static final int DATABASE_VERSION = 2;
		private static final String DATABASE_NAME = "score.db";
//		private static final String TABLE_NAME = "scores";
//		private static final String SCORE = "score";
		private static final String SCORE_TABLE_CREATE =
			"CREATE TABLE " + TABLE_NAME + " (" + 
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			SCORE + " INT, " + 
			NAME + " STRING, " + 
			TIME + " LONG" + ");";

		public OpenHelper(Context context) {

			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			
			Log.v(TAG,SCORE_TABLE_CREATE);
			Log.v(TAG,"openhepler");
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.v(TAG,"on create");
			db.execSQL(SCORE_TABLE_CREATE);
			
			for(int i=1;i<=5;i++){
				
				insert(0, "", 0l, db);
			}	
			Log.v(TAG,"on create finished");
		}

		private void insert(long score,String name,Long time,SQLiteDatabase db) {
			ContentValues values = new ContentValues();
			values.put(SCORE, score);
			values.put(NAME, name);
			values.put(TIME, time);
			db.insertOrThrow(TABLE_NAME, null, values);
			Log.v(TAG,"insert");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db,
				int oldVersion,
				int newVersion) {
			Log.w("Example", "Upgrading database, this will drop tables and recreate.");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}
}
