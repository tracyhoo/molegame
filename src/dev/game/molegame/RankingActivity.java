package dev.game.molegame;

import java.util.LinkedList;
import java.util.List;



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
		
		TableLayout table = (TableLayout) RankingActivity.this
				.findViewById(R.id.ranking_table);
		
		dh = new DataHelper(this.getApplicationContext());
		scorePairs = dh.selectAll();

		
		TableRow tableRow = null;
		TextView tableView = null;
		
/*		tableRow = new TableRow(GlobalRankingActivity.this);
		tableView = new TextView(GlobalRankingActivity.this);
		tableView.setText(R.string.ranking_title_no);
		tableView.setWidth(55);
		tableView.setPadding(10, 10, 0, 0);
		tableView.setTextColor(0xFF88C426);
		tableRow.addView(tableView);
		tableView = new TextView(GlobalRankingActivity.this);
		tableView.setText(R.string.ranking_title_name);
		tableView.setWidth(90);
		tableView.setPadding(10, 10, 0, 0);
		tableView.setTextColor(0xFFF28530);
		tableRow.addView(tableView);
		tableView = new TextView(GlobalRankingActivity.this);
		tableView.setText(R.string.ranking_title_record);
		tableView.setWidth(60);
		tableView.setPadding(10, 10, 0, 0);
		tableView.setTextColor(0xFFA7DBD7);
		tableRow.addView(tableView);
		tableView = new TextView(GlobalRankingActivity.this);
		tableView.setText(R.string.ranking_title_date);
		tableView.setWidth(85);
		tableView.setPadding(10, 10, 0, 0);
		tableView.setTextColor(0xFFFFEA21);
		tableRow.addView(tableView);
		mRankingDatas.add(tableRow);*/

		tableRow = new TableRow(table.getContext());
		tableView = new TextView(table.getContext());
		tableView.setText(R.string.ranking_title_no);
		tableView.setWidth(55);
		tableView.setPadding(10, 10, 0, 0);
		tableView.setTextColor(0xFF88C426);
		tableRow.addView(tableView);
		tableView = new TextView(RankingActivity.this);
		tableView.setText(R.string.ranking_title_no);
		tableView.setWidth(90);
		tableView.setPadding(10, 10, 0, 0);
		tableView.setTextColor(0xFFF28530);
		tableRow.addView(tableView);
		tableView = new TextView(RankingActivity.this);
		tableView.setText(R.string.ranking_title_no);
		tableView.setWidth(60);
		tableView.setPadding(10, 10, 0, 0);
		tableView.setTextColor(0xFFA7DBD7);
		tableRow.addView(tableView);
		tableView = new TextView(RankingActivity.this);
		tableView.setText(R.string.ranking_title_no);
		tableView.setWidth(85);
		tableView.setPadding(10, 10, 0, 0);
		tableView.setTextColor(0xFFFFEA21);
		tableRow.addView(tableView);
		int i = 1;
		for (ScorePair sp : scorePairs) {
			tableRow = new TableRow(table.getContext());
			tableView = new TextView(table.getContext());
			tableView.setText(Integer.toString(i));
			tableView.setWidth(55);
			tableView.setPadding(10, 10, 0, 0);
			tableView.setTextColor(0xFF88C426);
			tableRow.addView(tableView);
			tableView = new TextView(RankingActivity.this);
			tableView.setText(sp.name);
			tableView.setWidth(90);
			tableView.setPadding(10, 10, 0, 0);
			tableView.setTextColor(0xFFF28530);
			tableRow.addView(tableView);
			tableView = new TextView(RankingActivity.this);
			tableView.setText(Integer.toString(sp.score));
			tableView.setWidth(60);
			tableView.setPadding(10, 10, 0, 0);
			tableView.setTextColor(0xFFA7DBD7);
			tableRow.addView(tableView);
			tableView = new TextView(RankingActivity.this);
			tableView.setText(sp.time);
			tableView.setWidth(85);
			tableView.setPadding(10, 10, 0, 0);
			tableView.setTextColor(0xFFFFEA21);
			tableRow.addView(tableView);
			mRankingDatas.add(tableRow);
			i++;
		}

		for (TableRow row : mRankingDatas) {
/*			if (table.equals(row.getParent())) {
				table.removeView(row);
			}*/
			table.addView(row);
		}
		table.invalidate();

	}

}
