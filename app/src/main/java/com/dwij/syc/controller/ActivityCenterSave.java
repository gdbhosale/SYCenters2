package com.dwij.syc.controller;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dwij.syc.models.CenterList;
import com.dwij.syc.utils.Constants;
import com.syc.R;

public class ActivityCenterSave extends SherlockActivity {

	

	public static boolean isResumingFromDeletion = false;

	SQLiteDatabase myDB = null;
	String TableName = "CenterListTable";

	Button saveButton;
	EditText listNameET;
	ListView savedList;
	ArrayAdapter<String> aAdapter;
	TextView errorMsg;
	LinearLayout saveArea;
	TextView listTV;

	String cntJsonData = null;
	String cntCenterListName = null;
	ArrayList<CenterList> centerLists = new ArrayList<CenterList>();
	String[] center_list_items;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.center_saved_list);
		Log.d(Constants.TAG, "CenterSaveActivity.onCreate");

		isResumingFromDeletion = false;

		listNameET = (EditText) findViewById(R.id.listNameET);
		saveButton = (Button) findViewById(R.id.saveButton);
		savedList = (ListView) findViewById(R.id.savedList);
		errorMsg = (TextView) findViewById(R.id.errorMsg);
		saveArea = (LinearLayout) findViewById(R.id.saveArea);
		listTV = (TextView) findViewById(R.id.listTV);

		myDB = this.openOrCreateDatabase("CenterListDB", MODE_PRIVATE, null);
		myDB.execSQL("CREATE TABLE IF NOT EXISTS " + TableName + " (listname VARCHAR(100), data TEXT, saveddate datetime);");

		cntJsonData = (String) getIntent().getSerializableExtra("centersJson");
		cntCenterListName = (String) getIntent().getSerializableExtra("centerListName");
		Log.e(Constants.TAG, "Inputs to Save " + cntCenterListName + " " + cntJsonData);
		if (cntJsonData != null && !cntJsonData.trim().equals("[]") && cntCenterListName != null) {

			Cursor c = myDB.rawQuery("SELECT * FROM " + TableName + " WHERE listname=\'" + cntCenterListName + "\'", null);
			if (c != null && c.moveToFirst()) {
				saveArea.setVisibility(View.GONE);
				listTV.setVisibility(View.GONE);
				Log.e(Constants.TAG, "List Already Exists.");
			} else {
				Log.e(Constants.TAG, "List not Exists.");
				listNameET.setText(cntCenterListName);
				saveArea.setVisibility(View.VISIBLE);
				listTV.setVisibility(View.VISIBLE);
			}
		} else {
			saveArea.setVisibility(View.GONE);
			listTV.setVisibility(View.GONE);
			Log.e(Constants.TAG, "List not Given.");
		}
		//loadSavedLists();

		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cntJsonData != null && cntCenterListName != null) {
					listNameET.setText(cntCenterListName);
					myDB.execSQL("INSERT INTO " + TableName + " (listname, data, saveddate)" + " VALUES ('" + cntCenterListName + "', '"
							+ cntJsonData + "', datetime('now'));");
					//loadSavedLists();
					ActivityCenterSave.this.finish();
					Toast.makeText(ActivityCenterSave.this, "Center List " + cntCenterListName + " Saved.", Toast.LENGTH_LONG).show();
					saveArea.setVisibility(View.GONE);
					listTV.setVisibility(View.GONE);
				} else {
					Toast.makeText(ActivityCenterSave.this, "Cannot Store this List", Toast.LENGTH_LONG).show();
					saveArea.setVisibility(View.GONE);
					listTV.setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(Constants.TAG, "CenterSaveActivity.onResume");
		if (isResumingFromDeletion) {
			isResumingFromDeletion = false;
			finish();
		} else {
			loadSavedLists();
		}
	}

	void loadSavedLists() {
		Cursor c = myDB.rawQuery("SELECT * FROM " + TableName, null);
		int Column1 = c.getColumnIndex("listname");
		int Column2 = c.getColumnIndex("data");
		int Column3 = c.getColumnIndex("saveddate");
		if (c != null && c.moveToFirst()) {
			centerLists.clear();
			do {
				String listname = c.getString(Column1);
				String data = c.getString(Column2);
				String saveddate = c.getString(Column3);
				CenterList list = new CenterList(listname, data, saveddate);
				centerLists.add(list);
			} while (c.moveToNext());

			Iterator<CenterList> iter = centerLists.iterator();
			center_list_items = new String[centerLists.size()];
			for (int i = 0; iter.hasNext(); i++) {
				CenterList centerlist = iter.next();
				center_list_items[i] = centerlist.getListname();
			}
			if (aAdapter == null) {
				aAdapter = new ArrayAdapter<String>(ActivityCenterSave.this, R.layout.rowlayout, R.id.label, center_list_items);
				savedList.setAdapter(aAdapter);
				aAdapter.notifyDataSetChanged();
			} else {
				aAdapter.notifyDataSetChanged();
			}

			savedList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adptv, View view, int position, long id) {
					Intent i = new Intent(ActivityCenterSave.this, ActivityCenterListOld.class);
					i.putExtra("centerlist", centerLists.get(position));
					startActivity(i);
				}
			});
		} else {
			errorMsg.setVisibility(View.VISIBLE);
			errorMsg.setText("Delete old center lists to get new centers.");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		myDB.close();
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