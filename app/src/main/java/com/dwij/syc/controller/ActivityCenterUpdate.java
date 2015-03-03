package com.dwij.syc.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dwij.syc.models.Center;
import com.dwij.syc.utils.Constants;
import com.syc.R;

public class ActivityCenterUpdate extends SherlockActivity {

	static Context context;
	SharedPreferences settings;

	Center center;

	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
			+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	ProgressDialog proDialog;

	EditText cityName, centerName, address, regionET, strength;
	Spinner spiDay, spiHour, spiMinute, spiAMPM;
	ArrayAdapter<String> adptDay, adptHour, adptMinute, adptAMPM;
	EditText cord1Name, cord1Phone, cord1Email, cord2Name, cord2Phone, cord2Email, userEmail, userMobile;
	Button updateCenter;

	String cityNameStr, centerNameStr, addressStr, strengthStr;
	String spiDayStr, spiHourStr, spiMinuteStr, spiAMPMStr;
	String cord1NameStr, cord1PhoneStr, cord1EmailStr, cord2NameStr, cord2PhoneStr, cord2EmailStr, userEmailStr, userMobileStr;

	public static String[] days = { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
	public static String[] hours = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
	public static String[] minutess = { "00", "15", "30", "45" };
	public static String[] ampms = { "AM", "PM" };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.center_edit);
		settings = getSharedPreferences(Constants.TAG, Context.MODE_WORLD_WRITEABLE);
		Log.d(Constants.TAG, "NewCenterActivity.onCreate");
		context = this;

		center = (Center) getIntent().getSerializableExtra("center");
		if (center == null) {
			Toast.makeText(ActivityCenterUpdate.this, "Invalid Center Object", Toast.LENGTH_LONG).show();
			finish();
		}

		cityName = (EditText) findViewById(R.id.cityNameET);
		centerName = (EditText) findViewById(R.id.centerNameET);
		address = (EditText) findViewById(R.id.addressET);

		regionET = (EditText) findViewById(R.id.regionET);

		adptDay = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
		adptHour = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hours);
		adptMinute = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, minutess);
		adptAMPM = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ampms);

		spiDay = (Spinner) findViewById(R.id.spiDayE);
		spiHour = (Spinner) findViewById(R.id.spiHourE);
		spiMinute = (Spinner) findViewById(R.id.spiMinuteE);
		spiAMPM = (Spinner) findViewById(R.id.spiAMPME);

		spiDay.setAdapter(adptDay);
		spiHour.setAdapter(adptHour);
		spiMinute.setAdapter(adptMinute);
		spiAMPM.setAdapter(adptAMPM);

		cord1Name = (EditText) findViewById(R.id.cord1NameET);
		cord1Phone = (EditText) findViewById(R.id.cord1PhoneET);
		cord1Email = (EditText) findViewById(R.id.cord1EmailET);
		cord2Name = (EditText) findViewById(R.id.cord2NameET);
		cord2Phone = (EditText) findViewById(R.id.cord2PhoneET);
		cord2Email = (EditText) findViewById(R.id.cord2EmailET);
		strength = (EditText) findViewById(R.id.strengthET);

		userMobile = (EditText) findViewById(R.id.userMobileET);
		userMobileStr = settings.getString("userMobile", "");
		if (userMobileStr != null)
			userMobile.setText(userMobileStr);

		userEmail = (EditText) findViewById(R.id.userEmailET);
		userEmailStr = settings.getString("userEmail", "");
		if (userEmailStr != null)
			userEmail.setText(userEmailStr);

		updateCenter = (Button) findViewById(R.id.updateCenter);
		updateCenter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d(Constants.TAG, "Add Button Clicked");
				cityNameStr = cityName.getText().toString().trim();
				centerNameStr = centerName.getText().toString().trim();
				addressStr = address.getText().toString().trim();

				//spiStatesStr = spiStates.getSelectedItem().toString().trim();
				//spiDistrictsStr = spiDistricts.getSelectedItem().toString().trim();
				spiDayStr = spiDay.getSelectedItem().toString().trim();
				spiHourStr = spiHour.getSelectedItem().toString().trim();
				spiMinuteStr = spiMinute.getSelectedItem().toString().trim();
				spiAMPMStr = spiAMPM.getSelectedItem().toString().trim();

				cord1NameStr = cord1Name.getText().toString().trim();
				cord1PhoneStr = cord1Phone.getText().toString().trim();
				cord1EmailStr = cord1Email.getText().toString().trim();
				cord2NameStr = cord2Name.getText().toString().trim();
				cord2PhoneStr = cord2Phone.getText().toString().trim();
				cord2EmailStr = cord2Email.getText().toString().trim();
				strengthStr = strength.getText().toString().trim();
				userEmailStr = userEmail.getText().toString().trim();
				userMobileStr = userMobile.getText().toString().trim();

				if (cityNameStr == null || cityNameStr.equals("")) {
					Toast.makeText(context, "Please fill the City Name", Toast.LENGTH_LONG).show();
					Log.e(Constants.TAG, "City Name Invalid");
					return;
				}
				if (centerNameStr == null || centerNameStr.equals("")) {
					Toast.makeText(context, "Please fill the Center Name", Toast.LENGTH_LONG).show();
					Log.e(Constants.TAG, "Center Name Invalid");
					return;
				}
				if (addressStr == null || addressStr.equals("")) {
					Toast.makeText(context, "Please fill address", Toast.LENGTH_LONG).show();
					Log.e(Constants.TAG, "Address Invalid");
					return;
				}
				if (cord1NameStr == null || cord1NameStr.equals("")) {
					Toast.makeText(context, "Please fill the Coordinator I Name", Toast.LENGTH_LONG).show();
					Log.e(Constants.TAG, "Coordinator I Name Invalid");
					return;
				}
				if (cord1PhoneStr == null || cord1PhoneStr.equals("")) {
					Toast.makeText(context, "Please fill the Coordinator II Phone", Toast.LENGTH_LONG).show();
					Log.e(Constants.TAG, "Coordinator I Phone Invalid");
					return;
				}
				if (userEmailStr == null || userEmailStr.equals("") || !isValidEmail(userEmailStr)) {
					Toast.makeText(context, "Please Enter your Email for Authentication", Toast.LENGTH_LONG).show();
					Log.e(Constants.TAG, "User Mail Invalid");
					return;
				} else {
					Editor editor = settings.edit();
					editor.putString("userEmail", userEmailStr);
					editor.commit();
				}
				if (userMobileStr == null || userMobileStr.equals("") || userMobileStr.length() < 10) {
					Toast.makeText(context, "Please Enter your Mobile Number for Authentication", Toast.LENGTH_LONG).show();
					Log.e(Constants.TAG, "User Mobile Number Invalid");
					return;
				} else {
					Editor editor = settings.edit();
					editor.putString("userMobile", userMobileStr);
					editor.commit();
				}

				if (strengthStr == null || strengthStr.equals("")) {
					strengthStr = "25";
				}

				int strengthInt = 25;
				try {
					strengthInt = Integer.parseInt(strengthStr);
				} catch (Exception e) {
					Log.e(Constants.TAG, "Error Parsing Strength");
				}
				Center center = new Center(cityNameStr + ":" + centerNameStr, regionET.getText().toString(), addressStr, "EVERY "
						+ spiDayStr + " " + spiHourStr + ":" + spiMinuteStr + spiAMPMStr, cord1NameStr + "(" + cord1PhoneStr + ")"
						+ cord1EmailStr, cord2NameStr + "(" + cord2PhoneStr + ")" + cord2EmailStr, strengthInt);
				center.setUpdatedBy(userEmailStr);
				/*
				 * Gson gson = new Gson(); Type type = new TypeToken<Center>()
				 * {}.getType(); String jsonData = gson.toJson(center, type);
				 */
				UpdateCenterTask task = new UpdateCenterTask();
				setTitle("Updating Center: " + cityNameStr + ":" + centerNameStr);
				task.execute(new Center[] { center });
			}
		});

		// Settings Form Fields
		cityName.setText(center.getPlace().substring(0, center.getPlace().indexOf(":")));
		centerName.setText(center.getPlace().substring(center.getPlace().indexOf(":") + 1));
		address.setText(center.getAddress());
		regionET.setText(center.getRegion());
		strength.setText("" + center.getStrength());

		spiDay.setSelection(adptDay.getPosition(center.getDAY()));
		spiHour.setSelection(adptHour.getPosition(center.getHOURS()));
		spiMinute.setSelection(adptMinute.getPosition(center.getMINUTES()));
		spiAMPM.setSelection(adptAMPM.getPosition(center.getAMPM()));

		cord1Name.setText(center.getContact_1_name());
		cord1Phone.setText(center.getContact_1_phone());
		cord1Email.setText(center.getContact_1_email());
		cord2Name.setText(center.getContact_2_name());
		cord2Phone.setText(center.getContact_2_phone());
		cord2Email.setText(center.getContact_2_email());
		//userEmail.setText(center.getStrength());
		//userMobile.setText(center.getStrength());;
	}

	private class UpdateCenterTask extends AsyncTask<Center, Void, String> {

		String errorMessage;
		Center centerTemp;

		@Override
		protected void onPreExecute() {
			proDialog = ProgressDialog.show(context, "Updating Center", "Please wait while Updation...", true);
		}

		@Override
		protected String doInBackground(Center... centerStr) {
			centerTemp = centerStr[0];
			Log.d(Constants.TAG, "Updating Center: " + centerTemp.getPlace());

			BufferedReader in = null;
			try {
				String query = "http://dwijitsolutions.com/direct/center_edit.php";
				query += "?centerPlace=" + centerTemp.getPlace().replace(" ", "%20");
				query += "&centerPlaceOld=" + center.getPlace().replace(" ", "%20");
				query += "&centerRegion=" + centerTemp.getRegion().replace(" ", "%20");
				query += "&centerAddress=" + centerTemp.getAddress().replace(" ", "%20").replace("<br>", "%20");
				query += "&centerTime=" + centerTemp.getCenter_time().replace(" ", "%20");
				query += "&contact1=" + centerTemp.getContact_1().replace(" ", "%20");
				query += "&contact2=" + centerTemp.getContact_2().replace(" ", "%20");
				query += "&strength=" + centerTemp.getStrength();
				query += "&updatedByM=" + userMobileStr;
				query += "&updatedBy=" + centerTemp.getUpdatedBy();
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
			Log.d(Constants.TAG, "New Center Task Completed errorMessage:" + errorMessage);
			if (errorMessage != null) {
				Log.d(Constants.TAG, "Show Error");
				new AlertDialog.Builder(context).setMessage(errorMessage)
						.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
			} else {
				Toast.makeText(ActivityCenterUpdate.this, "Your Changes will be soon Authenticated.", Toast.LENGTH_LONG).show();
				Intent i = new Intent(context, ActivityCenterDisplay.class);
				i.putExtra("center", centerTemp);
				startActivity(i);
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