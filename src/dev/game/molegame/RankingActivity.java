package dev.game.molegame;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


import android.graphics.Color;
import dev.game.molegame.data.DataHelper;
import dev.game.molegame.data.DataHelper.ScorePair;

import android.app.Activity;
import android.os.Bundle;

import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RankingActivity extends Activity {

	private LinkedList<TableRow> mRankingDatas = new LinkedList<TableRow>();

	private DataHelper dh;
	
	private List<ScorePair> scorePairs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.ranking);
		
		TableLayout table = (TableLayout) RankingActivity.this.findViewById(R.id.ranking_table);
        table.setBackground(getResources().getDrawable(R.drawable.scoreboard));
		
		dh = new DataHelper(this.getApplicationContext());
		scorePairs = dh.getAllRecords();

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        TableRow tableRow = null;
		TextView tableView = null;

		tableRow = new TableRow(table.getContext());
		tableView = new TextView(table.getContext());
		tableView.setText(R.string.ranking_title_no);
		tableView.setWidth(60);
		tableView.setPaddingRelative(25, 60, 0, 0);
        //todo these should all be constant values
		tableView.setTextColor(0xFF88C426);
		tableRow.addView(tableView);
		tableView = new TextView(RankingActivity.this);
		tableView.setText(R.string.ranking_title_name);
		tableView.setWidth(80);
		tableView.setPaddingRelative(25, 60, 0, 0);
		tableView.setTextColor(0xFFF28530);
		tableRow.addView(tableView);
		tableView = new TextView(RankingActivity.this);
		tableView.setText(R.string.ranking_title_record);
		tableView.setWidth(55);
		tableView.setPaddingRelative(25, 60, 0, 0);
		tableView.setTextColor(0xFFA7DBD7);
		tableRow.addView(tableView);
		tableView = new TextView(RankingActivity.this);
		tableView.setText(R.string.ranking_title_date);
		tableView.setWidth(85);
		tableView.setPaddingRelative(25, 60, 0, 0);
		tableView.setTextColor(0xFFFFEA21);
		tableRow.addView(tableView);
        mRankingDatas.add(tableRow);

        int i = 1;
		for (ScorePair sp : scorePairs) {
			tableRow = new TableRow(table.getContext());
			tableView = new TextView(table.getContext());
			tableView.setText(Integer.toString(i));
			tableView.setWidth(55);
			tableView.setPaddingRelative(25, 10, 0, 0);
			tableView.setTextColor(0xFF88C426);
			tableRow.addView(tableView);
			tableView = new TextView(RankingActivity.this);
			tableView.setText(sp.name);
			tableView.setWidth(90);
			tableView.setPaddingRelative(25, 10, 0, 0);
			tableView.setTextColor(0xFFF28530);
			tableRow.addView(tableView);
			tableView = new TextView(RankingActivity.this);
			tableView.setText(Integer.toString(sp.score));
			tableView.setWidth(60);
			tableView.setPaddingRelative(25, 10, 0, 0);
			tableView.setTextColor(0xFFA7DBD7);
			tableRow.addView(tableView);
			tableView = new TextView(RankingActivity.this);
            if(sp.time != 0) tableView.setText(dt.format(new Date(sp.time)));
			tableView.setWidth(85);
			tableView.setPaddingRelative(25, 10, 0, 0);
			tableView.setTextColor(0xFFFFEA21);
			tableRow.addView(tableView);
			mRankingDatas.add(tableRow);
			i++;
		}

		for (TableRow row : mRankingDatas) {
			table.addView(row);
		}
        //repaint
		table.invalidate();

	}

}
