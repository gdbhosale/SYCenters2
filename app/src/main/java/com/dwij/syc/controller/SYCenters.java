package com.dwij.syc.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.dwij.syc.models.Center;
import com.dwij.syc.models.DatabaseHelper;
import com.dwij.syc.models.PublicProgram;
import com.dwij.syc.models.State;
import com.dwij.syc.utils.Constants;
import com.dwij.syc.utils.MyXMLHandler;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.syc.R;

public class SYCenters extends SherlockActivity implements OnClickListener {

	EditText editsearch;

	SQLiteDatabase myDB = null;

	String page, pageSearchName;
	int flagTextChange = 0;
	ArrayList<Center> centers;
	ArrayList<PublicProgram> ppAryLst;
	String center_list_items[];
	ArrayAdapter<String> aAdapter;

	List<String> stateList = new ArrayList<String>();
	List<String> districtList = new ArrayList<String>();
	ArrayAdapter<String> stateAdapter, districtAdapter;
	Spinner spState, spDistrict;

	ProgressDialog mProgressDialog;
	TextView errMsg;
	Activity mainActivity;
	

	LinearLayout ll, ll2;
	Button btnText, btnRegion;

	AlertDialog alertDialog, alertDialog2;
	DatabaseHelper db;

	ListView centerList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
		Log.d(Constants.TAG, "SYCenters Started");
		db = new DatabaseHelper(this);

		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#336699")));
		bar.setIcon(R.drawable.icon);
				


		spState = new Spinner(SYCenters.this);
		spDistrict = new Spinner(SYCenters.this);
		btnText = new Button(SYCenters.this);
		btnRegion = new Button(SYCenters.this);

		ll = new LinearLayout(SYCenters.this);
		ll2 = new LinearLayout(SYCenters.this);

		mainActivity = this;

		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Searching Centers...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(false);

		centerList = (ListView) findViewById(R.id.SearchResult);
		errMsg = (TextView) findViewById(R.id.errorMsg);
		
		

		centerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				//	Toast.makeText(getApplicationContext(), "Clicked list item!.", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(SYCenters.this, ActivityCenterDisplay.class);
				i.putExtra("center", centers.get(position));
				startActivity(i);

			}

		});

		alertDialog = new AlertDialog.Builder(SYCenters.this).create(); // You can use activity context directly.

		alertDialog.setTitle("Search By");

		ll.setOrientation(LinearLayout.VERTICAL);

		btnText.setText("Text");
		btnRegion.setText("Region");

		btnText.setTextColor(Color.BLACK);
		btnRegion.setTextColor(Color.BLACK);

		ll.addView(btnText);
		ll.addView(btnRegion);

		alertDialog.setView(ll);

		btnText.setOnClickListener(this);
		btnRegion.setOnClickListener(this);

		alertDialog2 = new AlertDialog.Builder(SYCenters.this).create();

		xmlParsing();
	}

	private void insertCenters() {

		Log.d(Constants.TAG, "Inserting in Database and Center size=" + centers.size());

		for (int i = 0; i < centers.size(); i++) {
			Center c = centers.get(i);

			if (c != null) {

				//To Check whether data is available or not in a database.
				boolean flag = db.checkDataAvailOrNot(c.getId());

				if (flag == true) {
					//	Log.d(Constants.TAG, "Flag=" + flag+" Updated");	
					db.updateCentersData(new Center(c.getId(), c.getPlace().toString(), c.getRegion().toString(),
							c.getAddress().toString(), c.getCenter_time().toString(), c.getContact_1().toString(), c.getContact_2()
									.toString(), c.getStrength()));
				} else {
					//	Log.d(Constants.TAG, "Flag=" + flag+" Inserted");                                                                                                              		
					db.insertCentersData(new Center(c.getId(), c.getPlace().toString(), c.getRegion().toString(),
							c.getAddress().toString(), c.getCenter_time().toString(), c.getContact_1().toString(), c.getContact_2()
									.toString(), c.getStrength()));
				}

			}
		}

		Log.d(Constants.TAG, "Done With Inserting or Updating Data in Database.");

	}


	@Override
	protected void onResume() {
		//new PublicProgramTask().execute();
		super.onResume();

	}

	private void xmlParsing() {

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
			//spState.setAdapter(stateAdapter);

			// Populate Districts
			districtAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, districtList);
			//spDistrict.setAdapter(districtAdapter);

			spState.setOnItemSelectedListener(new OnItemSelectedListener() {
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
			spState.setSelection(stateAdapter.getPosition("Maharashtra"));
		} catch (Exception e) {
			Log.e(Constants.TAG, "XML Parsing Exception", e);
			Toast.makeText(this, "XML Parsing Exception. Please insert SD Card if havent Inserted.", Toast.LENGTH_LONG).show();
			finish();
		}

	}

	private class SearchCenterTask extends AsyncTask<String, Void, String> {

		String errorMessage;

		@Override
		protected void onPreExecute() {
			mProgressDialog.setProgress(20);
			errMsg.setText("Searching...");
			errMsg.setVisibility(View.VISIBLE);

		}

		@Override
		protected String doInBackground(String... centerStr) {
			String add = centerStr[0];
			Log.d(Constants.TAG, "Starting searchCenter: " + add);

			if (add == null || add.equals("")) {
				errorMessage = "Invalid Input: add:" + add;
				Log.e(Constants.TAG, "Invalid Input: add:" + add);
				return null;
			}
			add = add.replace(" ", "%20");
			add = add.replace(",", "");
			add = add.replace("\n", "");
			add = add.replace("\"", "");
			add = add.replace("\'", "");

			BufferedReader in = null;
			try {
				Log.d(Constants.TAG, "Searching for:" + add);

				// ACCESS_NETWORK_STATE
				ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = conMan.getActiveNetworkInfo();
				Log.d(Constants.TAG, "Network State: " + networkInfo.getState().toString());
				if (networkInfo.isConnected()) {
					Log.d(Constants.TAG, "Network Info: " + networkInfo.toString());
				}
				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://www.dwijitsolutions.com/direct/searchsycenter.php?add=" + add);
				//HttpPost request = new HttpPost("http://192.168.1.104/dwijitso3/direct/searchsycenter.php?add=" + add);
				HttpResponse response = client.execute(request);
				Log.d(Constants.TAG, "Search Complete.");

				mProgressDialog.setProgress(40);
				in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				mProgressDialog.setProgress(60);
				page = sb.toString();
				Log.d(Constants.TAG, "Result: " + page);

				Gson gson = new Gson();
				Type type = new TypeToken<ArrayList<Center>>() {
				}.getType();
				centers = gson.fromJson(page, type);
				if (centers == null) {
					errorMessage = "No Center Found";
					Log.e(Constants.TAG, "No Center Found:" + page);
					page = null;
					return null;
				}

				insertCenters();

				// TODO update/insert these centers to db.

				Iterator<Center> iter = centers.iterator();
				center_list_items = new String[centers.size()];
				for (int i = 0; iter.hasNext(); i++) {
					Center center = iter.next();
					center.populate();
					center_list_items[i] = center.getPlace();
				}
				aAdapter = new ArrayAdapter<String>(SYCenters.this, R.layout.rowlayout, R.id.label, center_list_items);
				mProgressDialog.setProgress(80);
			} catch (JsonSyntaxException jse) {
				Log.e(Constants.TAG, "Error parsing Server Data", jse);
				errorMessage = "Error parsing Server Data: " + jse.getMessage();
			} catch (ClientProtocolException cpe) {
				Log.e(Constants.TAG, "Protocol Error Connecting Server", cpe);
				errorMessage = "Protocol Error Connecting Server: " + cpe.getMessage();
			} catch (IOException ioe) {
				Log.e(Constants.TAG, "IO Error Connecting Server", ioe);
				errorMessage = "IO Error Connecting Server: " + ioe.getMessage();
			} catch (Exception e) {
				Log.e(Constants.TAG, "Error Connecting Server", e);
				errorMessage = "Error Connecting Server: " + e.getMessage();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						Log.e(Constants.TAG, "Error Closing Connection", e);
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				mProgressDialog.setProgress(100);
				centerList.setAdapter(aAdapter);
				if (errorMessage != null) {
					errMsg.setVisibility(View.VISIBLE);
					errMsg.setText(errorMessage);
					errMsg.setTextColor(Color.WHITE);
					errMsg.setTextSize(20);
				} else {
					errMsg.setVisibility(View.GONE);
				}
				if (mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				spDistrict.setSelection(0);

			} catch (Exception e) {
				Log.e(Constants.TAG, "Error onPostExecute", e);
			}
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i = null;
		switch (item.getItemId()) {
		case R.id.menu_addcenter:
			//Toast.makeText(getApplicationContext(), "Pending Add New Center", Toast.LENGTH_SHORT).show();

			Log.d(Constants.TAG, "Menu Click: NewCenterActivity");
			i = new Intent(SYCenters.this, ActivityCenterAdd.class);
			startActivity(i);

			return true;

		case R.id.menu_savecenter:
			//Toast.makeText(getApplicationContext(), "Save Center", Toast.LENGTH_SHORT).show();
			Log.d(Constants.TAG, "Menu Click: Save Center");
			i = new Intent(SYCenters.this, ActivityCenterSave.class);
			i.putExtra("centersJson", page);
			i.putExtra("centerListName", pageSearchName);
			Log.d(Constants.TAG, pageSearchName + " " + page);
			startActivity(i);
			return true;

		case R.id.menu_publicprogram:
			//Toast.makeText(getApplicationContext(), "About App", Toast.LENGTH_SHORT).show();
			Log.d(Constants.TAG, "Menu Click: PublicProgram");
			i = new Intent(SYCenters.this, ActivityPublicPrograms.class);
			startActivity(i);
			return true;

		case R.id.menu_aboutapp:
			//Toast.makeText(getApplicationContext(), "About App", Toast.LENGTH_SHORT).show();
			Log.d(Constants.TAG, "Menu Click: AboutActivity");
			i = new Intent(SYCenters.this, ActivityAbout.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Create the options menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Get the options menu view from menu.xml in menu folder
		getSupportMenuInflater().inflate(R.menu.menu_main, menu);

		// Locate the EditText in menu.xml
		editsearch = (EditText) menu.findItem(R.id.menu_search).getActionView();

		editsearch.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);
		EditText.OnEditorActionListener exampleListener = new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				String str = editsearch.getText().toString();
				Log.e(Constants.TAG, "Edit action." + str);

				//if((event != null && (event.getAction() == KeyEvent.KEYCODE_ENTER)) || actionId == KeyEvent.KEYCODE_ENTER) {

				Log.e(Constants.TAG, "Search Called." + str);
				mProgressDialog.show();
				errMsg.setVisibility(View.GONE);
				SearchCenterTask task = new SearchCenterTask();
				mainActivity.setTitle("SY Centers:" + str);
				task.execute(new String[] { str });

				return false;
			}
		};
		editsearch.setOnEditorActionListener(exampleListener);
		// Capture Text in EditText
		editsearch.addTextChangedListener(textWatcher);
		// Show the search menu item in menu.xml
		MenuItem menuSearch = menu.findItem(R.id.menu_search);

		//ON CLICK OF MENU SEARCH ALERT IS OPEN FOR SEARCH.
		menuSearch.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {

				ll2.removeAllViews();
				alertDialog.show();

				return false;
			}
		});

		menuSearch.setOnActionExpandListener(new OnActionExpandListener() {
			// Menu Action Collapse
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				// Empty EditText to remove text filtering
				editsearch.setText("");
				editsearch.clearFocus();
				return true;
			}

			// Menu Action Expand
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				// Focus on EditText
				editsearch.requestFocus();
				// Force the keyboard to show on EditText focus
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				return true;
			}
		});

		// Show the settings menu item in menu.xml
		MenuItem menuSettings = menu.findItem(R.id.menu_settings);
		// Capture menu item clicks
		menuSettings.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// Do something here
				//	Toast.makeText(getApplicationContext(), "Nothing heren in setting ok!", Toast.LENGTH_SHORT).show();

				return false;
			}
		});

		return true;
	}

	// EditText TextWatcher
	private TextWatcher textWatcher = new TextWatcher() {
		String str = "";

		@Override
		public void afterTextChanged(Editable s) {
			String strNew = editsearch.getText().toString().toLowerCase(Locale.getDefault());
			if (!str.equals(strNew)) {
				str = strNew;
				// adapter.filter(text);
				// Start with second packages and classes
				//Toast.makeText(getApplicationContext(), "Changed text=" + text, Toast.LENGTH_SHORT).show();
				if (str.length() < 1)
					flagTextChange = 0;

				if (str.length() > 3) {

					Cursor cur = db.SortSearchedPlaces(str);

					cur.moveToFirst();
					centers = new ArrayList<Center>();
					center_list_items = new String[cur.getCount()];

					for (int i = 0; cur.isAfterLast() == false; i++) {
						Center center = new Center(cur.getString(cur.getColumnIndex("PLACE")), cur.getString(cur.getColumnIndex("REGION")),
								cur.getString(cur.getColumnIndex("ADDRESS")), cur.getString(cur.getColumnIndex("CENTER_TIME")),
								cur.getString(cur.getColumnIndex("CONTACT_1")), cur.getString(cur.getColumnIndex("CONTACT_2")),
								cur.getInt(cur.getColumnIndex("STRENGTH")));
						centers.add(center);
						center_list_items[i] = center.getPlace();
						cur.moveToNext();
					}
					Log.d(Constants.TAG, "@ cur.getCount()" + cur.getCount());
					cur.close();
					aAdapter = new ArrayAdapter<String>(SYCenters.this, R.layout.rowlayout, R.id.label, center_list_items);
					centerList.setAdapter(aAdapter);
				}

			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

		}
	};

	@Override
	public void onClick(View v) {
		if (v == btnText) {
			alertDialog.dismiss();
			//Toast.makeText(getApplicationContext(), "Text Clicked", Toast.LENGTH_SHORT).show();
		} else if (v == btnRegion) {
			alertDialog.dismiss();

			// You can use activity context directly.
			alertDialog2.setTitle("Select Region.");

			ll2.setOrientation(LinearLayout.VERTICAL);
			ll2.addView(spState);
			ll2.addView(spDistrict);

			alertDialog2.setView(ll2);

			spState.setPrompt("Select State");
			spDistrict.setPrompt("Select City");

			stateAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_row, stateList);
			spState.setAdapter(stateAdapter);

			// Populate Districts
			districtAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_row, districtList);
			spDistrict.setAdapter(districtAdapter);

			stateAdapter.setDropDownViewResource(R.layout.spinner_row);
			districtAdapter.setDropDownViewResource(R.layout.spinner_row);

			spState.setAdapter(stateAdapter);
			spDistrict.setAdapter(districtAdapter);

			alertDialog2.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					Log.d(Constants.TAG, "Clicked on Ok Button of Alert.");
					String str = districtAdapter.getItem(spDistrict.getSelectedItemPosition());
					if (!str.equals("District")) {
						mProgressDialog.show();
						errMsg.setVisibility(View.GONE);
						SearchCenterTask task = new SearchCenterTask();
						str = stateAdapter.getItem(spState.getSelectedItemPosition()) + ":" + str;
						mainActivity.setTitle("SY Centers:" + str);
						pageSearchName = str;
						task.execute(new String[] { str });
					} else {
						mProgressDialog.show();
						errMsg.setVisibility(View.GONE);
						SearchCenterTask task = new SearchCenterTask();
						str = editsearch.getText().toString();
						mainActivity.setTitle("SY Centers:" + str);

						pageSearchName = str;
						task.execute(new String[] { str });
					}

				}
			});
			alertDialog2.show();
			//Toast.makeText(getApplicationContext(), "Region Clicked", Toast.LENGTH_SHORT).show();
		}
	}
}
