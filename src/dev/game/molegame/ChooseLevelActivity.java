package dev.game.molegame;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.game.molegame.util.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class ChooseLevelActivity extends Activity{

	private GridView gv;
	private Intent intent;
	private ChooseLevelActivity me;
	protected int level;
	private static final String TAG = "ChooseLevelActivity";

/*	private LinearLayout top_padding;
	private LinearLayout bottom_padding;
	private LinearLayout gridview_layout;*/
	
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		Bundle extras = getIntent().getExtras();
		level = extras.getInt(Constants.LEVEL);
		Log.d(TAG,"level is "+Integer.toString(level));
		
		Display display = getWindowManager().getDefaultDisplay(); 
		int width = display.getWidth();
		int height = display.getHeight();
		
//		Log.d("width and height:",Integer.toString(width)+","+Integer.toString(height));
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.levels);
		
/*		top_padding = (LinearLayout) findViewById(R.id.top_padding_layout);
		bottom_padding = (LinearLayout) findViewById(R.id.botomm_padding_layout);
		gridview_layout = (LinearLayout) findViewById(R.id.gridview_layout);
		
		top_padding.setLayoutParams(params);*/
		
		me = this;
		
		intent = new Intent(this, MoleGameActivity.class);

		gv = (GridView) findViewById(R.id.gridView1);
		
		
		
/*		List<Map<String, Object>> items = new ArrayList<Map<String,Object>>(); 
		
        for (int i = 0; i < 9; i++) { 
          Map<String, Object> item = new HashMap<String, Object>(); 
          if(i<=level)
        	  item.put("imageItem", R.drawable.unlocked); 
          else
        	  item.put("imageItem", R.drawable.locked);
          item.put("textItem", "Level " + i);
          items.add(item); 
        } 
        
        SimpleAdapter adapter = new SimpleAdapter(this,  
                items,  
                R.layout.grid_item,  
                new String[]{"imageItem", "textItem"},  
                new int[]{R.id.image_item, R.id.text_item}); 
*/

        ImageAdapter ada = new ImageAdapter(this,width,height);
        gv.setAdapter(ada); 
        gv.setOnItemClickListener(new OnItemClickListener() {


			public void onItemClick(AdapterView parent,
					View view, 
					int position,
					long id) {
				
				if(position <= level){
					intent.putExtra(Constants.CURRENT_LEVEL, position);
					startActivity(intent);
					me.finish();
				}else{
					Toast toast = Toast.makeText(me, 
							"This level is not unlocked yet.", 
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 220);
					toast.show();
					
				}
			//	parent.getRootView().
				/*this.finish();*/
				
			}
			});

		
	}
	
	public class ImageAdapter extends BaseAdapter {  
		  
        private Context context;  
        private Bitmap unlocked;
        private Bitmap locked;
/*        private List<Map<String, Bitmap>> items = 
        		new ArrayList<Map<String,Bitmap>>(); */
          
        public ImageAdapter(Context c,int width, int height) {  
            context = c;  


            int w = width / 4;
            int h = height / 5;
            
            Bitmap tmp_unlocked = BitmapFactory.decodeResource(
            		context.getResources(),R.drawable.unlocked);  
            Bitmap tmp_locked = BitmapFactory.decodeResource(
            		context.getResources(), R.drawable.locked);
            unlocked = Bitmap.createScaledBitmap(tmp_unlocked, w, h, true);
            locked = Bitmap.createScaledBitmap(tmp_locked, w, h, true);
            
            Log.d("image adapter","------------------------------");
        }  
  

        public Object getItem(int position) {  
            return position;  
        }  
  
        public long getItemId(int position) {  
            return position;  
        }  
  

		public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);  
      //      imageView.setLayoutParams(new GridView.LayoutParams(90,-2));
            
            Bitmap bmp = null;
            Log.d(TAG,"position is "+ Integer.toString(position));

            //place lock/unlock icon to the canvas
            if(position <= level) {
                bmp = unlocked;
            } else {
                bmp = locked;
            }


            imageView.setImageBitmap(bmp);  
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);  
            return imageView;  
		}


		public int getCount() {
			// TODO Auto-generated method stub
			return Constants.LEVELS;
		}  
     
}  

}
