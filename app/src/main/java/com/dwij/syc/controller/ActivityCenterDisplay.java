package com.dwij.syc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dwij.syc.models.Center;
import com.dwij.syc.utils.Constants;
import com.syc.R;

public class ActivityCenterDisplay extends SherlockActivity implements OnClickListener {

	TextView textCName, textCRegion, textCAdd, textCTime, textCCont1Label, textCCont2Label, tvCCont1Phone, tvCCont2Phone, tvCCont1Email,
			tvCCont2Email, tvCStrength;
	Button btnShare, btnUpdateCenter, btnCloseCenter;
	// EditText smsUserPhone;

	LinearLayout llCenterTime, llCenterAddress, llCenterCont1, llCenterCont2;
	Center center;
	String address, strReview, strEmail, strMobile;

	int selectedItem;
	Boolean emailcheck = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.center_view);

		Log.d(Constants.TAG, "CenterDisplayActivity Started.");

		center = (Center) getIntent().getSerializableExtra("center");
		if (center == null) {
			Toast.makeText(ActivityCenterDisplay.this, "Invalid Center Object", Toast.LENGTH_LONG).show();
			finish();
		}

		textCName = (TextView) findViewById(R.id.center_name);
		textCRegion = (TextView) findViewById(R.id.center_region);
		textCAdd = (TextView) findViewById(R.id.center_address);
		llCenterAddress = (LinearLayout) findViewById(R.id.center_ll_address);
		textCTime = (TextView) findViewById(R.id.center_time);
		llCenterTime = (LinearLayout) findViewById(R.id.center_ll_time);
		textCCont1Label = (TextView) findViewById(R.id.center_contact1_lable);
		textCCont2Label = (TextView) findViewById(R.id.center_contact2_lable);

		llCenterCont1 = (LinearLayout) findViewById(R.id.center_ll_contact1);
		llCenterCont2 = (LinearLayout) findViewById(R.id.center_ll_contact2);

		tvCCont1Phone = (TextView) findViewById(R.id.center_contact1_phone);
		tvCCont2Phone = (TextView) findViewById(R.id.center_contact2_phone);

		tvCCont1Email = (TextView) findViewById(R.id.center_contact1_email);
		tvCCont2Email = (TextView) findViewById(R.id.center_contact2_email);

		tvCStrength = (TextView) findViewById(R.id.center_strength);

		btnShare = (Button) findViewById(R.id.center_btn_sharethis);
		btnUpdateCenter = (Button) findViewById(R.id.center_btn_updatecenter);
		btnCloseCenter = (Button) findViewById(R.id.center_btn_closecenter);

		address = center.getAddress();
		address = address.replaceAll("<br>", " ");
		address = address.replaceAll("<br/>", " ");
		address = address.replaceAll("\n", " ");

		textCName.setText(center.getPlace());
		textCRegion.setText(center.getRegion());
		textCAdd.setText(address);
		textCTime.setText(center.getCenter_time());
		tvCStrength.setText("" + center.getStrength());

		llCenterAddress.setOnClickListener(this);
		llCenterTime.setOnClickListener(this);
		btnShare.setOnClickListener(this);
		btnUpdateCenter.setOnClickListener(this);
		btnCloseCenter.setOnClickListener(this);

		if (center.getContact_1_email() != null && !center.getContact_1_email().equals("") && !center.getContact_1_email().equals("Email")) {
			tvCCont1Email.setText(center.getContact_1_email());
			tvCCont1Email.setOnClickListener(this);
		} else {
			tvCCont1Email.setVisibility(View.GONE);

		}
		if (center.getContact_2_email() != null && !center.getContact_2_email().equals("") && !center.getContact_2_email().equals("Email")) {
			tvCCont2Email.setText(center.getContact_2_email());
			tvCCont2Email.setOnClickListener(this);
		} else {
			tvCCont2Email.setVisibility(View.GONE);

		}

		if (center.getContact_1_phone() != null && !center.getContact_1_phone().equals("") && !center.getContact_1_phone().equals("Phone")) {

			textCCont1Label.setText(center.getContact_1_name());
			tvCCont1Phone.setText(center.getContact_1_phone());
			tvCCont1Phone.setOnClickListener(this);
		} else {
			tvCCont1Phone.setVisibility(View.GONE);
			llCenterCont1.setVisibility(View.GONE);
		}

		if (center.getContact_2_phone() != null && !center.getContact_2_phone().equals("") && !center.getContact_2_phone().equals("Phone")) {
			textCCont2Label.setText(center.getContact_2_name());
			tvCCont2Phone.setText(center.getContact_2_phone());
			tvCCont2Phone.setOnClickListener(this);
		} else {
			tvCCont2Phone.setVisibility(View.GONE);
			llCenterCont2.setVisibility(View.GONE);
		}
	}

	private class ClosedCenterTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {

			String line = null;
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(params[0]);

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				line = EntityUtils.toString(httpEntity);

			} catch (IOException e) {
				Log.d(Constants.TAG, "Return Closed Centers:" + line);
			}
			return line;

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

	@Override
	public void onClick(View v) {

		if (v == tvCCont1Email) {

			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			String aEmailList[] = { center.getContact_1_email() };
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
			emailIntent.setType("plain/text");
			startActivity(emailIntent);

		} else if (v == tvCCont2Email) {

			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			String aEmailList[] = { center.getContact_2_email() };
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
			emailIntent.setType("plain/text");
			startActivity(emailIntent);

		} else if (v == tvCCont1Phone) {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + center.getContact_1_phone()));
			startActivity(intent);
		} else if (v == tvCCont2Phone) {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + center.getContact_2_phone()));
			startActivity(intent);
		} else if (v == llCenterAddress) {

			final double latitude = center.getLat();
			final double longitude = center.getLng();
			Log.d(Constants.TAG, "Latitude:" + latitude + " and Logitude:" + longitude);

			if (latitude == 0.0 || longitude == 0.0) {
				Toast.makeText(getApplicationContext(), "Map coordinates not available for this place.", Toast.LENGTH_SHORT).show();
			} else {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("geo:0,0?q=" + ("" + latitude + ", " + longitude + "")));
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else if (v == llCenterTime) {

			ArrayList<String> weekDaysStr = new ArrayList<String>();
			weekDaysStr.add(0, "");
			weekDaysStr.add(1, "SUNDAY");
			weekDaysStr.add(2, "MONDAY");
			weekDaysStr.add(3, "TUESDAY");
			weekDaysStr.add(4, "WEDNESDAY");
			weekDaysStr.add(5, "THURSDAY");
			weekDaysStr.add(6, "FRIDAY");
			weekDaysStr.add(7, "SATURDAY");

			Log.d(Constants.TAG,
					"CENTER TIME: " + center.getDAY() + " " + center.getHOURS() + " " + center.getMINUTES() + " " + center.getAMPM());

			int givenDay = weekDaysStr.indexOf(center.getDAY());
			Calendar cal = Calendar.getInstance();
			Log.d(Constants.TAG, "givenDay: " + givenDay + " DAY_OF_WEEK: " + cal.get(Calendar.DAY_OF_WEEK));
			if (cal.get(Calendar.DAY_OF_WEEK) <= givenDay) {
				cal.add(Calendar.DATE, givenDay - cal.get(Calendar.DAY_OF_WEEK));
			} else {
				int daysToAdd = 0;
				if (givenDay == 1)
					daysToAdd = 8 - cal.get(Calendar.DAY_OF_WEEK);
				else
					daysToAdd = cal.get(Calendar.DAY_OF_WEEK);
				Log.d(Constants.TAG, "Adding days: " + daysToAdd);
				cal.add(Calendar.DATE, daysToAdd);
			}
			Log.d(Constants.TAG, " New Date:" + cal.get(Calendar.DATE));
			cal.set(Calendar.HOUR, Integer.parseInt(center.getHOURS()));
			cal.set(Calendar.MINUTE, Integer.parseInt(center.getMINUTES()));
			if (center.getAMPM().equals("AM"))
				cal.set(Calendar.AM_PM, Calendar.AM);
			else
				cal.set(Calendar.AM_PM, Calendar.PM);

			Intent intent = new Intent(Intent.ACTION_EDIT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra("beginTime", cal.getTimeInMillis());
			intent.putExtra("endTime", cal.getTimeInMillis() + 2 * 60 * 60 * 1000);
			intent.putExtra("allDay", false);
			// intent.putExtra("rrule", "FREQ=WEEKLY;");
			intent.putExtra("title", "Sahaja Yoga Center: " + center.getPlace());
			intent.putExtra("description", "Contact I: " + center.getContact_1() + "\n\n" + "Contact II: " + center.getContact_2());
			intent.putExtra("eventLocation", address);
			startActivity(intent);
		} else if (v == btnShare) {

			final Dialog diag = new Dialog(ActivityCenterDisplay.this);

			diag.setContentView(R.layout.sharethis_dialog);
			diag.setTitle("Share Center");

			Button bt1 = (Button) diag.findViewById(R.id.btn_yes1);
			Button bt2 = (Button) diag.findViewById(R.id.btn_no1);

			bt1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					diag.dismiss();
					String strs = "Sahaja Yoga Center: " + center.getPlace() + "\nAddress:" + address + "\nTime:" + center.getCenter_time()
							+ "\nContact I:" + center.getContact_1() + "\nContact II:" + center.getContact_2();
					Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
					sharingIntent.setType("text/plain");
					String shareBody = strs;
					sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "SY Center " + center.getPlace());
					sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
					startActivity(Intent.createChooser(sharingIntent, "Share via"));

				}

			});

			bt2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					diag.dismiss();

				}
			});

			diag.show();

		} else if (v == btnUpdateCenter) {
			Intent intent = new Intent(ActivityCenterDisplay.this, ActivityCenterUpdate.class);
			intent.putExtra("center", center);
			startActivity(intent);

		} else if (v == btnCloseCenter) {

			final Dialog diag = new Dialog(ActivityCenterDisplay.this);

			diag.setContentView(R.layout.close_dialog);
			diag.setTitle("Closed Center");

			final EditText et1 = (EditText) diag.findViewById(R.id.etReview);
			final EditText et2 = (EditText) diag.findViewById(R.id.etEmail);
			final EditText et3 = (EditText) diag.findViewById(R.id.etMobile);
			Button bt1 = (Button) diag.findViewById(R.id.btn_yes);
			Button bt2 = (Button) diag.findViewById(R.id.btn_no);

			bt1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					


					strReview = et1.getText().toString();
					strEmail = et2.getText().toString();
					strMobile = et3.getText().toString();

					checkEmail(strEmail);

					
					if (strReview.length() < 10) {
						Toast.makeText(getApplicationContext(), "Write atleast 10 digit review.", Toast.LENGTH_SHORT).show();
						et1.requestFocus();
					} else

					if (emailcheck == false) {
						Toast.makeText(getApplicationContext(), "Invalide email or empty field ", Toast.LENGTH_SHORT).show();
						et2.requestFocus();

					} else if (strMobile.length() < 10) {
						Toast.makeText(getApplicationContext(), "Invalide mobile no. or empty field. ", Toast.LENGTH_SHORT).show();
						et3.requestFocus();

					}

					else {
						diag.dismiss();
						String output = "";
						try {
							output = new ClosedCenterTask()
									.execute(
											"http://dwijitsolutions.com/apps/sahajatools/webservices/center_closed.php?id=2&remark=TestRemark&email=gdb.sci123@gmail.com&phone=7350558900")
									.get();
						} catch (Exception e) {
							Log.d(Constants.TAG, "Exception" + output);
						}

						Log.d(Constants.TAG, "Output=" + output);
						if (output != null && output.startsWith("Mail sentSUCCESS")) {
							Toast.makeText(getApplicationContext(), "Closing of this center has been reported to Administrator.",
									Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
						}

					}

				
				}
				private void checkEmail(String email) {

					Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
					Matcher matcher = pattern.matcher(email);
					emailcheck = matcher.matches();

				}

			});

			bt2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					diag.dismiss();
				}

				
			});

			diag.show();

		}
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(id, args);
	}

	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog, args);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(id);
	}
}
