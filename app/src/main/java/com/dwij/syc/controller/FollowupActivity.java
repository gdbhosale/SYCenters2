/*
 * Author : ErVaLt / techwavedev.com
 * Description : TabLayout Andorid App
 */
package com.dwij.syc.controller;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dwij.syc.utils.Constants;
import com.syc.R;

public class FollowupActivity extends SherlockActivity implements OnClickListener {

	Button btnAddSeeker, btnRemoveSeeker, btnStatistics, btnSettings;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.follow_dashboard);
		Log.d(Constants.TAG, "FollowupActivity Started");

		btnAddSeeker = (Button) findViewById(R.id.btnAddSeeker);
		btnRemoveSeeker = (Button) findViewById(R.id.btnRemoveSeeker);
		btnStatistics = (Button) findViewById(R.id.btnStatistics);
		btnSettings = (Button) findViewById(R.id.btnSettings);

		btnAddSeeker.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnAddSeeker) {
			Intent i = new Intent(FollowupActivity.this, FollowupAddSeekerActivity.class);
			startActivity(i);
		} else if (v == btnRemoveSeeker) {
			Intent i = new Intent(FollowupActivity.this, FollowupRemoveSeekerActivity.class);
			startActivity(i);
		} else if (v == btnStatistics) {
			Intent i = new Intent(FollowupActivity.this, FollowupStatsActivity.class);
			startActivity(i);
		} else if (v == btnSettings) {
			Intent i = new Intent(FollowupActivity.this, FollowupSettingsActivity.class);
			startActivity(i);
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
