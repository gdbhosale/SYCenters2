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
import com.dwij.syc.models.PublicProgram;
import com.dwij.syc.models.State;
import com.dwij.syc.utils.Constants;
import com.dwij.syc.utils.MyXMLHandler;
import com.syc.R;

public class ActivityProgramAdd extends SherlockActivity implements OnClickListener {

	static Context context;
	SharedPreferences settings;

	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
			+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	ProgressDialog proDialog;

	EditText etPName, etPAddress,  etPAddressLiving, etPName1, etPPhone1, etPEmail1, etPName2, etPPhone2, etPEmail2,
			etPNoOfPeople, etPDesc, etPYourEmail, etPYourMobile;
	Spinner spPType, spPState, spPDistrict;
	DatePicker dpPDate,dpPWorkStart;
	TimePicker tpPTime;
	Button btnAddProgram;

	String strPName, strPType, strPStates, strPDisctricts, strPAddress, strPWorkStart, strPAddressLiving, strPName1, strPPhone1,
			strPEmail1, strPName2, strPPhone2, strPEmail2, strPNoOfPeople, strPDesc, strPYourEmail, strPYourMobile,strPTime;
	int pYear, pMonth, pDayOfMonth, pHour, pMinute;
	String AM_PM;

	ArrayAdapter<String> adptState, adptDistrict;

	List<String> stateList = new ArrayList<String>();
	List<String> districtList = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.program_add);
		settings = getSharedPreferences(Constants.TAG, Context.MODE_WORLD_WRITEABLE);
		Log.d(Constants.TAG, "ActivityProgramAdd.onCreate");
		context = this;

		etPName = (EditText) findViewById(R.id.padd_name);
		spPType = (Spinner) findViewById(R.id.padd_type);
		spPState = (Spinner) findViewById(R.id.padd_states);
		spPDistrict = (Spinner) findViewById(R.id.padd_districts);
		etPAddress = (EditText) findViewById(R.id.padd_address);
		dpPDate = (DatePicker) findViewById(R.id.dpProgramTime);
		tpPTime = (TimePicker) findViewById(R.id.timePicker1);
		dpPWorkStart = (DatePicker) findViewById(R.id.dpWorkStart);
		etPAddressLiving = (EditText) findViewById(R.id.padd_livingaddress);
		etPName1 = (EditText) findViewById(R.id.padd_coordinator1_name);
		etPPhone1 = (EditText) findViewById(R.id.padd_coordinator1_phone);
		etPEmail1 = (EditText) findViewById(R.id.padd_coordinator1_email);
		etPName2 = (EditText) findViewById(R.id.padd_coordinator2_name);
		etPPhone2 = (EditText) findViewById(R.id.padd_coordinator2_phone);
		etPEmail2 = (EditText) findViewById(R.id.padd_coordinator2_email);

		etPNoOfPeople = (EditText) findViewById(R.id.padd_noOfPeople);
		etPDesc = (EditText) findViewById(R.id.padd_description);
		etPYourEmail = (EditText) findViewById(R.id.padd_useremail);
		etPYourMobile = (EditText) findViewById(R.id.padd_usermobile);

		strPYourEmail = settings.getString("userEmail", "");
		if (strPYourEmail != null)
			etPYourEmail.setText(strPYourEmail);

		btnAddProgram = (Button) findViewById(R.id.padd_btn_addprogram);

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

		btnAddProgram.setOnClickListener(this);

	}

	@Override
	protected void onResume() {

		super.onResume();

	}

	private class AddProgramTask extends AsyncTask<PublicProgram, Void, String> {

		String errorMessage;
		PublicProgram pprogram;

		@Override
		protected void onPreExecute() {
			proDialog = ProgressDialog.show(context, "Adding New Program", "Please wait while adding new Program...", true);
		}

		@Override
		protected String doInBackground(PublicProgram... pprogramStr) {
			pprogram = pprogramStr[0];
			Log.d(Constants.TAG, "Adding Program: " + pprogram.getPname());

			String tempPLat = "" + pprogram.getPlat();
			String tempPLng = "" + pprogram.getPlng();
			String tempNoOfPeople = "" + pprogram.getNum_people();
			String tempCenter = "" + pprogram.getCenter();
			BufferedReader in = null;
			try {
				String query = "http://www.dwijitsolutions.com/apps/sahajatools/webservices/program_add.php";
				query += "?pname=" + pprogram.getPname().replace(" ", "%20");
				query += "&ptype=" + pprogram.getPtype().replace(" ", "%20");
				query += "&pregion=" + pprogram.getPregion().replace(" ", "%20");
				query += "&paddress=" + pprogram.getPaddress().replace(" ", "%20");
				query += "&plat=" + tempPLat.replace(" ", "%20");
				query += "&plng=" + tempPLng.replace(" ", "%20");
				query += "&ptime=" + pprogram.getPtime().replace(" ", "%20");
				query += "&pwork_start=" + pprogram.getPwork_start().replace(" ", "%20");
				query += "&paddress_living=" + pprogram.getPaddress_living().replace(" ", "%20");
				query += "&contact_1=" + pprogram.getContact_1().replace(" ", "%20");
				query += "&contact_2=" + pprogram.getContact_2().replace(" ", "%20");
				query += "&num_people=" + tempNoOfPeople.replace(" ", "%20");
				query += "&description=" + pprogram.getDescription().replace(" ", "%20");
				query += "&center=" + tempCenter.replace(" ", "%20");
				query += "&feeder_email=" + pprogram.getFeeder_email().replace(" ", "%20");
				query += "&feeder_phone=" + pprogram.getFeeder_phone().replace(" ", "%20");

				Log.i(Constants.TAG, query);
				
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
				if (page.contains("Error while adding program")) {
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
			Log.d(Constants.TAG, "New Program Task Completed errorMessage:" + errorMessage);
			if (errorMessage != null) {
				Log.d(Constants.TAG, "Show Error");
				new AlertDialog.Builder(context).setMessage(errorMessage)
						.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
			} else {
				/*Intent i = new Intent(context, ActivityProgramDisplay.class);
				i.putExtra("pprogram", pprogram);
				startActivity(i);*/
				Toast.makeText(getApplicationContext(), "Program added successfully.", Toast.LENGTH_SHORT).show();
				finish();
			}
			errorMessage = null;
		}

	}

	private boolean isValidEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
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

		if (v == btnAddProgram) {
			Log.d(Constants.TAG, "Add Program Button Clicked");

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
			
			strPTime=pYear+ "-" + pMonth + "-" + pDayOfMonth + "%20" + pHour + ":" + pMinute + ":00";
			
			int pWStartYear,pWStartMonth,pWStartDayOfMonth;
			pWStartYear=dpPWorkStart.getYear();
			pWStartMonth=dpPWorkStart.getMonth();
			pWStartDayOfMonth=dpPWorkStart.getDayOfMonth();
			
			
			strPWorkStart = pWStartYear+"-"+pWStartMonth+"-"+pWStartDayOfMonth+" 10:00:00";
			
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

			PublicProgram pp = new PublicProgram(0, strPName, strPType, strPStates + ":" + strPDisctricts, strPAddress, 0.0, 0.0, strPTime, strPWorkStart, strPAddressLiving,
					strPName1 + "(" + strPPhone1 + ")" + strPEmail1, strPName2 + "(" + strPPhone2 + ")" + strPEmail2,
					Integer.parseInt(strPNoOfPeople), strPDesc, 0, "", strPYourEmail, strPYourMobile, "", "");
			
			
			AddProgramTask task = new AddProgramTask();
			setTitle("Adding Program: " + strPName );
			task.execute(new PublicProgram[] { pp });

			
		}
	}

}
