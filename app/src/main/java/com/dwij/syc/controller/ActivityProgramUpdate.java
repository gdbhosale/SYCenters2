package com.dwij.syc.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dwij.syc.models.DatabaseHelper;
import com.dwij.syc.models.PublicProgram;
import com.dwij.syc.models.State;
import com.dwij.syc.utils.Constants;
import com.dwij.syc.utils.MyXMLHandler;
import com.syc.R;

public class ActivityProgramUpdate extends SherlockActivity implements OnClickListener {

	static Context context;
	SharedPreferences settings;

	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
			+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	ProgressDialog proDialog;

	EditText etPName, etPAddress, etPAddressLiving, etPName1, etPPhone1, etPEmail1, etPName2, etPPhone2, etPEmail2, etPNoOfPeople, etPDesc,
			etPYourEmail, etPYourMobile;
	Spinner spPType, spPState, spPDistrict;
	DatePicker dpPDate, dpPWorkStart;
	TimePicker tpPTime;
	Button btnUpdateProgram;

	String strPName, strPType, strPStates, strPDisctricts, strPAddress, strPWorkStart, strPAddressLiving, strPName1, strPPhone1,
			strPEmail1, strPName2, strPPhone2, strPEmail2, strPNoOfPeople, strPDesc, strPYourEmail, strPYourMobile, strPTime;
	int pYear, pMonth, pDayOfMonth, pHour, pMinute;
	String AM_PM;
	int dd, mm, yyyy, thh, tmm;
	String pDt;

	ArrayAdapter<String> adptState, adptDistrict;

	PublicProgram publicprogram;
	DatabaseHelper db;

	List<String> stateList = new ArrayList<String>();
	List<String> districtList = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.program_update);
		db = new DatabaseHelper(this);
		settings = getSharedPreferences(Constants.TAG, Context.MODE_WORLD_WRITEABLE);
		Log.d(Constants.TAG, "ActivityProgramUpdate.onCreate");
		context = this;

		publicprogram = (PublicProgram) getIntent().getSerializableExtra("publicprogram");
		if (publicprogram == null) {
			Toast.makeText(ActivityProgramUpdate.this, "Invalid Program Object", Toast.LENGTH_LONG).show();
			finish();
		}

		etPName = (EditText) findViewById(R.id.pupdate_name);
		spPType = (Spinner) findViewById(R.id.pupdate_type);
		spPState = (Spinner) findViewById(R.id.pupdate_states);
		spPDistrict = (Spinner) findViewById(R.id.pupdate_districts);
		etPAddress = (EditText) findViewById(R.id.pupdate_address);
		dpPDate = (DatePicker) findViewById(R.id.pupdate_dpProgramDate);
		tpPTime = (TimePicker) findViewById(R.id.pupdate_tpProgramTime);
		dpPWorkStart = (DatePicker) findViewById(R.id.pupdate_WorkStart);
		etPAddressLiving = (EditText) findViewById(R.id.pupdate_livingaddress);
		etPName1 = (EditText) findViewById(R.id.pupdate_coordinator1_name);
		etPPhone1 = (EditText) findViewById(R.id.pupdate_coordinator1_phone);
		etPEmail1 = (EditText) findViewById(R.id.pupdate_coordinator1_email);
		etPName2 = (EditText) findViewById(R.id.pupdate_coordinator2_name);
		etPPhone2 = (EditText) findViewById(R.id.pupdate_coordinator2_phone);
		etPEmail2 = (EditText) findViewById(R.id.pupdate_coordinator2_email);

		etPNoOfPeople = (EditText) findViewById(R.id.pupdate_noOfPeople);
		etPDesc = (EditText) findViewById(R.id.pupdate_description);
		etPYourEmail = (EditText) findViewById(R.id.pupdate_useremail);
		etPYourMobile = (EditText) findViewById(R.id.pupdate_usermobile);

		//Set the data into related fields
		etPName.setText(publicprogram.getPname());
		etPAddress.setText(publicprogram.getPaddress());
		setDatePickerandTimePicker();
		etPAddressLiving.setText(publicprogram.getPaddress_living());

		etPName1.setText(publicprogram.getContact_1_name());
		etPPhone1.setText(publicprogram.getContact_1_phone());
		etPEmail1.setText(publicprogram.getContact_1_email());
		etPName2.setText(publicprogram.getContact_2_name());
		etPPhone2.setText(publicprogram.getContact_2_phone());
		etPEmail2.setText(publicprogram.getContact_2_email());

		etPNoOfPeople.setText("" + publicprogram.getNum_people());
		etPDesc.setText(publicprogram.getDescription());
		etPYourEmail.setText(publicprogram.getFeeder_email());
		etPYourMobile.setText(publicprogram.getFeeder_phone());

		strPYourEmail = settings.getString("userEmail", "");
		if (strPYourEmail != null)
			etPYourEmail.setText(strPYourEmail);

		btnUpdateProgram = (Button) findViewById(R.id.pupdate_btn_addprogram);

		stateList.clear();
		for (int i = 0; i < MyXMLHandler.stateList.size(); i++) {
			stateList.add(MyXMLHandler.stateList.get(i).stateName);
		}
		adptState = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateList);
		spPState.setAdapter(adptState);

		// Populate Districts
		adptDistrict = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, districtList);
		spPDistrict.setAdapter(adptDistrict);

		spPState.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adpt, View view, int position, long arg3) {
				String stateCnt = adptState.getItem(position);
				for (int i = 0; i < MyXMLHandler.stateList.size(); i++) {
					String statesTemp = MyXMLHandler.stateList.get(i).stateName;
					if (statesTemp.equals(stateCnt)) {
						State s = MyXMLHandler.stateList.get(i);
						districtList.clear();
						districtList.add("District");
						for (int j = 0; j < s.districts.size(); j++) {
							districtList.add(s.districts.get(j));
						}
						adptDistrict.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adpt) {
			}
		});
		spPState.setSelection(adptState.getPosition("Maharashtra"));
		spPDistrict.setSelection(adptDistrict.getPosition("pune"));

		btnUpdateProgram.setOnClickListener(this);

		if ((publicprogram.getContact_1_email()).equals("Email")) {
			etPEmail1.setText("");
			etPEmail1.setHint("Enter your email id");
		}

		if ((publicprogram.getContact_2_email()).equals("Email")) {
			etPEmail2.setText("");
			etPEmail2.setHint("Enter your email id");
		}

	}

	private void setDatePickerandTimePicker() {
		pDt = (publicprogram.getPtime().trim());

		yyyy = Integer.parseInt(pDt.substring(0, 4));
		mm = Integer.parseInt(pDt.substring(5, 7));
		dd = Integer.parseInt(pDt.substring(8, 10));
		thh = Integer.parseInt(pDt.substring(11, 13));
		tmm = Integer.parseInt(pDt.substring(14, 16));

		//set the date in date picker
		dpPDate.updateDate(yyyy, mm - 1, dd);
		tpPTime.setCurrentHour(thh);
		tpPTime.setCurrentMinute(tmm);

		pDt = (publicprogram.getPwork_start().trim());

		yyyy = Integer.parseInt(pDt.substring(0, 4));
		mm = Integer.parseInt(pDt.substring(5, 7));
		dd = Integer.parseInt(pDt.substring(8, 10));
		thh = Integer.parseInt(pDt.substring(11, 13));
		tmm = Integer.parseInt(pDt.substring(14, 16));

		dpPWorkStart.updateDate(yyyy, mm - 1, dd);

	}

	@Override
	protected void onResume() {

		super.onResume();

	}

	private boolean isValidEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	private class UpdateProgramTask extends AsyncTask<PublicProgram, Void, String> {

		String errorMessage;
		PublicProgram ppTemp;

		@Override
		protected void onPreExecute() {
			proDialog = ProgressDialog.show(context, "Updating Program", "Please wait while Updation...", true);
		}

		@Override
		protected String doInBackground(PublicProgram... programStr) {
			ppTemp = programStr[0];
			Log.d(Constants.TAG, "Updating Center: " + ppTemp.getPname());

			String tempNoOfPeople = "" + ppTemp.getNum_people();
			int tempId = ppTemp.getId();
			String tempLat = "" + ppTemp.getPlat();
			String tempLng = "" + ppTemp.getPlat();
			int tempCenter = 0;

			BufferedReader in = null;
			try {
				String query = "http://dwijitsolutions.com/apps/sahajatools/webservices/program_edit.php";
				query += "?pid=" + tempId;
				query += "&pname=" + ppTemp.getPname().replace(" ", "%20");
				query += "&ptype=" + ppTemp.getPtype().replace(" ", "%20");
				query += "&pregion=" + ppTemp.getPregion().replace(" ", "%20");
				query += "&paddress=" + ppTemp.getPaddress().replace(" ", "%20");
				query += "&plat=" + tempLat.replace(" ", "%20");
				query += "&plng=" + tempLng.replace(" ", "%20");
				query += "&ptime=" + ppTemp.getPtime().replace(" ", "%20");
				query += "&pwork_start=" + ppTemp.getPwork_start().replace(" ", "%20");
				query += "&paddress_living=" + ppTemp.getPaddress_living().replace(" ", "%20");
				query += "&contact_1=" + ppTemp.getContact_1().replace(" ", "%20");
				query += "&contact_2=" + ppTemp.getContact_2().replace(" ", "%20");
				query += "&num_people=" + tempNoOfPeople.replace(" ", "%20");
				query += "&description=" + ppTemp.getDescription().replace(" ", "%20");
				query += "&center=" + tempCenter;
				query += "&feeder_email=" + ppTemp.getFeeder_email().replace(" ", "%20");
				query += "&feeder_phone=" + ppTemp.getFeeder_phone().replace(" ", "%20");

				Log.d(Constants.TAG, "Query:" + query);

				HttpPost request = new HttpPost(query);

				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(request);
				Log.d(Constants.TAG, "Search Complete.");
				in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				String page = sb.toString();
				Log.d(Constants.TAG, "Result: " + page);
				if (page.contains("Error while editing center")) {
					errorMessage = page;
				}
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
			proDialog.dismiss();
			Log.d(Constants.TAG, "Updating Task Completed errorMessage:" + errorMessage);
			if (errorMessage != null) {
				Log.d(Constants.TAG, "Show Error");
				new AlertDialog.Builder(context).setMessage(errorMessage)
						.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
			} else {
				Toast.makeText(ActivityProgramUpdate.this, "Your Changes will be soon Authenticated.", Toast.LENGTH_LONG).show();
				Intent i = new Intent(context, ActivityProgramDisplay.class);

				i.putExtra("pp", ppTemp);
				startActivity(i);
			}
			errorMessage = null;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#336699")));
		actionBar.setIcon(R.drawable.icon);
		
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

	@Override
	public void onClick(View v) {

		if (v == btnUpdateProgram) {
			Log.d(Constants.TAG, "Update Program Button Clicked");

			strPName = etPName.getText().toString().trim();
			strPType = spPType.getSelectedItem().toString().trim();
			strPStates = spPState.getSelectedItem().toString().trim();
			strPDisctricts = spPDistrict.getSelectedItem().toString().trim();
			strPAddress = etPAddress.getText().toString().trim();
			pYear = dpPDate.getYear();
			pMonth = dpPDate.getMonth();
			pDayOfMonth = dpPDate.getDayOfMonth();

			pHour = tpPTime.getCurrentHour();
			pMinute = tpPTime.getCurrentMinute();

			strPTime = pYear + "-" + pMonth + "-" + pDayOfMonth + "%20" + pHour + ":" + pMinute + ":00";

			int pWStartYear, pWStartMonth, pWStartDayOfMonth;
			pWStartYear = dpPWorkStart.getYear();
			pWStartMonth = dpPWorkStart.getMonth();
			pWStartDayOfMonth = dpPWorkStart.getDayOfMonth();

			strPWorkStart = pWStartYear + "-" + pWStartMonth + "-" + pWStartDayOfMonth + " 10:00:00";

			strPAddressLiving = etPAddressLiving.getText().toString().trim();
			strPName1 = etPName1.getText().toString().trim();
			strPPhone1 = etPPhone1.getText().toString().trim();
			strPEmail1 = etPEmail1.getText().toString().trim();
			strPName2 = etPName2.getText().toString().trim();
			strPPhone2 = etPPhone2.getText().toString().trim();
			strPEmail2 = etPEmail2.getText().toString().trim();

			strPNoOfPeople = etPNoOfPeople.getText().toString().trim();
			strPDesc = etPDesc.getText().toString().trim();
			strPYourEmail = etPYourEmail.getText().toString().trim();
			strPYourMobile = etPYourMobile.getText().toString().trim();

			if (strPName == null || strPName.equals("")) {
				Toast.makeText(context, "Please fill the program name", Toast.LENGTH_LONG).show();
				Log.e(Constants.TAG, "Program Name Invalid");
				return;
			}
			if (strPDisctricts.equals("District")) {
				Toast.makeText(context, "Please select District", Toast.LENGTH_LONG).show();
				Log.e(Constants.TAG, "Region District Invalid");
				return;
			}
			if (strPAddress == null || strPAddress.equals("")) {
				Toast.makeText(context, "Please fill the program address", Toast.LENGTH_LONG).show();
				Log.e(Constants.TAG, "Program address Invalid");
				return;
			}
			if (strPWorkStart == null || strPWorkStart.equals("")) {
				Toast.makeText(context, "Please fill the program start work", Toast.LENGTH_LONG).show();
				Log.e(Constants.TAG, "Program start work Invalid");
				return;
			}
			if (strPAddressLiving == null || strPAddressLiving.equals("")) {
				Toast.makeText(context, "Please fill the address living", Toast.LENGTH_LONG).show();
				Log.e(Constants.TAG, "Program address living is Invalid");
				return;
			}
			if (strPName1 == null || strPName1.equals("")) {
				Toast.makeText(context, "Please fill Coordinator 1 Name", Toast.LENGTH_LONG).show();
				Log.e(Constants.TAG, "Program Coordinator 1 Name is Invalid");
				return;
			}
			if (strPPhone1 == null || strPPhone1.equals("")) {
				Toast.makeText(context, "Please fill Coordinator 1 Phone", Toast.LENGTH_LONG).show();
				Log.e(Constants.TAG, "Program address Coordinator 1 Phone");
				return;
			}

			if (strPYourEmail == null || strPYourEmail.equals("") || !isValidEmail(strPYourEmail)) {
				Toast.makeText(context, "Please Enter your Email for Authentication", Toast.LENGTH_LONG).show();
				Log.e(Constants.TAG, "User Mail Invalid");
				return;
			} else {
				Editor editor = settings.edit();
				editor.putString("userEmail", strPYourEmail);
				editor.commit();
			}
			if (strPYourMobile == null || strPYourMobile.equals("") || strPYourMobile.length() < 10) {
				Toast.makeText(context, "Please Enter your Mobile Number for Authentication", Toast.LENGTH_LONG).show();
				Log.e(Constants.TAG, "User Mobile Number Invalid");
				return;
			}

			if (strPNoOfPeople == null || strPNoOfPeople.equals("")) {
				strPNoOfPeople = "25";
			}

			if (strPDesc == null || strPDesc.equals("")) {
				Toast.makeText(context, "Please fill Description", Toast.LENGTH_LONG).show();
				Log.e(Constants.TAG, "Program Description Invalid");
				return;
			}

			PublicProgram pp = new PublicProgram(publicprogram.getId(), strPName, strPType, strPStates + ":" + strPDisctricts, strPAddress,
					0.0, 0.0, strPTime, strPWorkStart, strPAddressLiving, strPName1 + "(" + strPPhone1 + ")" + strPEmail1, strPName2 + "("
							+ strPPhone2 + ")" + strPEmail2, Integer.parseInt(strPNoOfPeople), strPDesc, 0, "", strPYourEmail,
					strPYourMobile, "", "");
			//Not Needed
			//db.updatePPData(pp);

			UpdateProgramTask task = new UpdateProgramTask();
			setTitle("Updating Program: " + strPName);
			task.execute(new PublicProgram[] { pp });

		}
	}

}
