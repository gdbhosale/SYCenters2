/*
 * Author : ErVaLt / techwavedev.com
 * Description : TabLayout Andorid App
 */
package com.dwij.syc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dwij.syc.models.State;
import com.dwij.syc.utils.Constants;
import com.dwij.syc.utils.MyXMLHandler;
import com.syc.R;

public class FollowupAddSeekerActivity extends SherlockActivity {

	Spinner spinnerStates, spinnerDistricts;
	ArrayAdapter<String> stateAdapter, districtAdapter;

	List<String> stateList = new ArrayList<String>();
	List<String> districtList = new ArrayList<String>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.follow_addseeker);
		Log.d(Constants.TAG, "FollowupAddSeekerActivity Started.");

		spinnerStates = (Spinner) findViewById(R.id.spinnerStatesTwo);
		spinnerDistricts = (Spinner) findViewById(R.id.spinnerDistrictsTwo);

		xmlParsing();
	}

	void xmlParsing() {
		try {
			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			/** Create handler to handle XML Tags ( extends DefaultHandler ) */
			MyXMLHandler myXMLHandler = new MyXMLHandler();
			xr.setContentHandler(myXMLHandler);
			// InputStream is = getResources().openRawResource(R.raw.location);

			File f = new File("/mnt/sdcard/location.xml");
			if (!f.exists()) {
				f.createNewFile();
				FileOutputStream fos = new FileOutputStream(f);
				InputStream is = getResources().openRawResource(R.raw.location);
				byte[] buffer = new byte[1000];
				int bytesRead = 0;
				while ((bytesRead = is.read(buffer)) != -1) {
					fos.write(buffer, 0, bytesRead);
					Log.d(Constants.TAG, "Write " + bytesRead);
				}
				fos.close();
				is.close();
			}

			xr.parse(new InputSource(new FileInputStream(f)));

			// Populate State List
			stateList.clear();
			for (int i = 0; i < MyXMLHandler.stateList.size(); i++) {
				stateList.add(MyXMLHandler.stateList.get(i).stateName);
			}
			stateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateList);
			spinnerStates.setAdapter(stateAdapter);

			// Populate Districts
			districtAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, districtList);
			spinnerDistricts.setAdapter(districtAdapter);

			spinnerStates.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> adpt, View view, int position, long arg3) {
					String stateCnt = stateAdapter.getItem(position);
					for (int i = 0; i < MyXMLHandler.stateList.size(); i++) {
						String statesTemp = MyXMLHandler.stateList.get(i).stateName;
						if (statesTemp.equals(stateCnt)) {
							State s = MyXMLHandler.stateList.get(i);
							districtList.clear();
							districtList.add("District");
							for (int j = 0; j < s.districts.size(); j++) {
								districtList.add(s.districts.get(j));
							}
							districtAdapter.notifyDataSetChanged();
						}
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> adpt) {
				}
			});
			spinnerStates.setSelection(stateAdapter.getPosition("Maharashtra"));
		} catch (Exception e) {
			Log.e(Constants.TAG, "XML Parsing Exception", e);
			Toast.makeText(this, "XML Parsing Exception. Please insert SD Card if havent Inserted.", Toast.LENGTH_LONG).show();
			finish();
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
