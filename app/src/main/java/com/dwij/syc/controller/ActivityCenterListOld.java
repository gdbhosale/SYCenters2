package com.dwij.syc.controller;

import java.util.ArrayList;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dwij.syc.models.Center;
import com.dwij.syc.models.CenterList;
import com.dwij.syc.utils.Constants;
import com.syc.R;

/**
 * Offline Center List Display
 * @author admin
 *
 */
public class ActivityCenterListOld extends SherlockActivity {

	SQLiteDatabase myDB = null;
	String TableName = "CenterListTable";

	ArrayList<Center> centers;
	CenterList centerListObj;
	ListView centerList;
	Button btDelete;
	String center_list_items[];
	ArrayAdapter<String> aAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oldcenterlist);

		myDB = this.openOrCreateDatabase("CenterListDB", MODE_PRIVATE, null);

		Log.d(Constants.TAG, "Starting OldCenterList.onCreate()");
		btDelete = (Button) findViewById(R.id.btDelete);

		centerList = (ListView) findViewById(R.id.ResultList);
		centerListObj = (CenterList) getIntent().getSerializableExtra("centerlist");
		if (centerListObj != null) {
			centers = centerListObj.getCenters();
			center_list_items = centerListObj.getCenter_list_items();
			aAdapter = new ArrayAdapter<String>(ActivityCenterListOld.this, R.layout.rowlayout, R.id.label, center_list_items);
			centerList.setAdapter(aAdapter);

			centerList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adptv, View view, int position, long id) {
					Intent i = new Intent(ActivityCenterListOld.this, ActivityCenterDisplay.class);
					i.putExtra("center", centers.get(position));
					startActivity(i);
				}
			});
			setTitle("Old List: " + centerListObj.getListname() + " - " + centerListObj.getSaveddate());

			btDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					myDB.execSQL("DELETE FROM " + TableName + " WHERE listname='" + centerListObj.getListname() + "';");
					ActivityCenterSave.isResumingFromDeletion = true;
					ActivityCenterListOld.this.finish();
				}
			});
		} else {
			Toast.makeText(ActivityCenterListOld.this, "Error getting Center List", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#336699")));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home || item.getItemId() == 0) {
			finish();
			getSupportActionBar().setHomeButtonEnabled(false);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}