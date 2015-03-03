package com.dwij.syc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dwij.syc.models.DatabaseHelper;
import com.dwij.syc.models.PublicProgram;
import com.dwij.syc.utils.Constants;
import com.syc.R;

public class ActivityProgramDisplay extends SherlockActivity implements OnClickListener {

	LinearLayout imgMap, setReminder;

	TextView tvProgramName, tvProgramType, tvProgramRegion, tvProgramAddress, tvProgramTime, tvProgramWorkStart, tvProgramAddressLiving,
			tvProgramDescription, tvCont1, tvCont2, tvCont1Phone, tvCont2Phone, tvCont1Email, tvCont2Email;

	DatabaseHelper db;
	String contact1Email, contact2Email, contact1Phone, contact2Phone, contact1Name, contact2Name;

	PublicProgram publicProgram;
	Button btnUpdateProgram;

	LinearLayout ll1, ll2;
	int first1 = 0;
	int last1 = 0;
	int first2 = 0;
	int last2 = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.program_view);
		db = new DatabaseHelper(this);
		Log.d(Constants.TAG, "ActivityProgramDisplay Started.");

		publicProgram = (PublicProgram) getIntent().getSerializableExtra("pp");
		if (publicProgram == null) {
			Toast.makeText(ActivityProgramDisplay.this, "Invalid Program Object", Toast.LENGTH_LONG).show();
			finish();
		}

		first1 = (publicProgram.getContact_1().lastIndexOf('('));
		last1 = (publicProgram.getContact_1().lastIndexOf(')'));
		first2 = (publicProgram.getContact_2().lastIndexOf('('));
		last2 = (publicProgram.getContact_2().lastIndexOf(')'));

		//	Log.d(Constants.TAG, "first1:"+first1+" last1;"+last1+" and first2:"+first2+" last2:"+last2);

		tvProgramName = (TextView) findViewById(R.id.pp_name);
		tvProgramType = (TextView) findViewById(R.id.pp_type);
		tvProgramRegion = (TextView) findViewById(R.id.pp_region);
		tvProgramAddress = (TextView) findViewById(R.id.pp_address);
		imgMap = (LinearLayout) findViewById(R.id.pp_google_map);
		setReminder = (LinearLayout) findViewById(R.id.ll_setreminder);
		tvProgramTime = (TextView) findViewById(R.id.pp_time);

		tvProgramWorkStart = (TextView) findViewById(R.id.pp_pworkstart);
		tvProgramAddressLiving = (TextView) findViewById(R.id.pp_addressliving);

		ll1 = (LinearLayout) findViewById(R.id.pp_ll_cont1);
		tvCont1 = (TextView) findViewById(R.id.pp_contact1);
		tvCont1Phone = (TextView) findViewById(R.id.pp_contact1_phone);
		tvCont1Email = (TextView) findViewById(R.id.pp_contact1_email);

		ll2 = (LinearLayout) findViewById(R.id.pp_ll_cont2);
		tvCont2 = (TextView) findViewById(R.id.pp_contact2);
		tvCont2Phone = (TextView) findViewById(R.id.pp_contact2_phone);
		tvCont2Email = (TextView) findViewById(R.id.pp_contact2_email);

		tvProgramDescription = (TextView) findViewById(R.id.pp_description);

		btnUpdateProgram = (Button) findViewById(R.id.pp_btn_update);

		tvProgramName.setText(publicProgram.getPname());
		tvProgramType.setText(publicProgram.getPtype());
		tvProgramRegion.setText(publicProgram.getPregion());
		tvProgramAddress.setText(publicProgram.getPaddress());

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formatter2 = new SimpleDateFormat("MMM dd, yyyy KK:mm:a");
			Date d = formatter.parse(publicProgram.getPtime());
			tvProgramTime.setText(formatter2.format(d));
		} catch (Exception e) {
			Log.e(Constants.TAG, "Error while Parsing Dtae", e);
		}

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formatter2 = new SimpleDateFormat("MMM dd, yyyy KK:mm:a");
			Date d = formatter.parse(publicProgram.getPwork_start());
			tvProgramWorkStart.setText(formatter2.format(d));
		} catch (Exception e) {
			Log.e(Constants.TAG, "Error while Parsing Dtae", e);
		}

		tvProgramAddressLiving.setText(publicProgram.getPaddress_living());

		contact1Email = publicProgram.getContact_1().substring(last1 + 1, publicProgram.getContact_1().length());
		contact2Email = publicProgram.getContact_2().substring(last2 + 1, publicProgram.getContact_2().length());

		if (contact1Email != null && !contact1Email.equals("") && !contact1Email.equals("Email")) {
			tvCont1Email.setText(contact1Email);
			tvCont1Email.setOnClickListener(this);
		} else {
			tvCont1Email.setVisibility(View.GONE);

		}
		if (contact2Email != null && !contact2Email.equals("") && !contact2Email.equals("Email")) {
			tvCont2Email.setText(contact2Email);
			tvCont2Email.setOnClickListener(this);
		} else {
			tvCont2Email.setVisibility(View.GONE);

		}

		contact1Name = publicProgram.getContact_1().substring(0, first1);
		contact2Name = publicProgram.getContact_2().substring(0, first2);
		contact1Phone = publicProgram.getContact_1().substring(first1 + 1, last1);
		contact2Phone = publicProgram.getContact_2().substring(first2 + 1, last2);

		if (contact1Phone != null && !contact1Phone.equals("") && !contact1Phone.equals("Phone")) {

			tvCont1.setText(contact1Name);
			tvCont1Phone.setText(contact1Phone);
			tvCont1Phone.setOnClickListener(this);
		} else {
			tvCont1Phone.setVisibility(View.GONE);
			ll1.setVisibility(View.GONE);
		}

		if (contact2Phone != null && !contact2Phone.equals("") && !contact2Phone.equals("Phone")) {
			tvCont2.setText(contact2Name);
			tvCont2Phone.setText(contact2Phone);
			tvCont2Phone.setOnClickListener(this);
		} else {
			tvCont2Phone.setVisibility(View.GONE);
			ll2.setVisibility(View.GONE);
		}

		tvProgramDescription.setText(publicProgram.getDescription());

		tvCont1Phone.setOnClickListener(this);
		tvCont2Phone.setOnClickListener(this);
		imgMap.setOnClickListener(this);
		setReminder.setOnClickListener(this);
		btnUpdateProgram.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#336699")));
		getSupportMenuInflater().inflate(R.menu.menu_programupdate, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home || item.getItemId() == 0) {
			finish();
			getSupportActionBar().setHomeButtonEnabled(false);
			return true;
		}

		switch (item.getItemId()) {
		case R.id.menu_updateprogram:
			Intent iUpdateProgram = new Intent(ActivityProgramDisplay.this, ActivityProgramUpdate.class);
			iUpdateProgram.putExtra("publicprogram", publicProgram);
			startActivity(iUpdateProgram);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (v == btnUpdateProgram) {
			Intent iUpdatePP = new Intent(ActivityProgramDisplay.this, ActivityProgramUpdate.class);
			iUpdatePP.putExtra("publicprogram", publicProgram);
			startActivity(iUpdatePP);
		} else

		if (v == tvCont1Email) {
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			String aEmailList[] = { contact1Email };
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
			emailIntent.setType("plain/text");
			startActivity(emailIntent);

		} else if (v == tvCont2Email) {
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			String aEmailList[] = { contact2Email };
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
			emailIntent.setType("plain/text");
			startActivity(emailIntent);

		} else

		if (v == tvCont1Phone) {
			if ((publicProgram.getContact_1().substring(first1 + 1, last1)) == null
					|| (publicProgram.getContact_1().substring(first1 + 1, last1)).equals("")) {
				Toast.makeText(getApplicationContext(), "Contact 1 not available.", Toast.LENGTH_SHORT).show();
			} else {
				Intent intent = new Intent(Intent.ACTION_CALL,
						Uri.parse("tel:" + publicProgram.getContact_1().substring(first1 + 1, last1)));
				startActivity(intent);
			}
		} else if (v == tvCont2Phone) {
			if ((publicProgram.getContact_2().substring(first2 + 1, last2)) == null
					|| (publicProgram.getContact_2().substring(first2 + 1, last2)).equals("")) {
				Toast.makeText(getApplicationContext(), "Contact 2 not available.", Toast.LENGTH_SHORT).show();
			} else {
				Intent intent = new Intent(Intent.ACTION_CALL,
						Uri.parse("tel:" + publicProgram.getContact_2().substring(first2 + 1, last2)));
				startActivity(intent);
			}
		}
		if (v == imgMap) {
			final double latitude = publicProgram.getPlat();
			final double longitude = publicProgram.getPlng();
			Log.d(Constants.TAG, "Latitude:" + latitude + " and Logitude:" + longitude);

			if (latitude == 0.0 || longitude == 0.0) {
				Toast.makeText(getApplicationContext(), "Map Not Available For This Place.", Toast.LENGTH_SHORT).show();
			} else {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("geo:0,0?q=" + ("" + latitude + ", " + longitude + "")));
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (v == setReminder) {
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = format.parse(publicProgram.getPtime());
				//Log.d(Constants.TAG, "PROGRAM TIME: " + date.getDay() + " " + publicProgram.getHOURS() + " " + publicProgram.getMINUTES() + " " + publicProgram.getAMPM());

				Calendar cal = Calendar.getInstance();

				cal.setTime(date);

				Intent intent = new Intent(Intent.ACTION_EDIT);
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra("beginTime", cal.getTimeInMillis());
				intent.putExtra("endTime", cal.getTimeInMillis() + 2 * 60 * 60 * 1000);
				intent.putExtra("allDay", false);
				// intent.putExtra("rrule", "FREQ=WEEKLY;");
				intent.putExtra("title", "SY Public Program: " + publicProgram.getPname());
				intent.putExtra("description",
						"Contact I: " + publicProgram.getContact_1() + "\n\n" + "Contact II: " + publicProgram.getContact_2()
								+ "\n\nDescription: " + publicProgram.getDescription());
				intent.putExtra("eventLocation", publicProgram.getPaddress());
				startActivity(intent);
			} catch (ParseException e) {
				Log.e(Constants.TAG, "Error while parding date: ", e);
			}

		}
	}
}