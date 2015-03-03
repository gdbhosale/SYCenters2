package com.dwij.syc.widget;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.dwij.syc.controller.ActivityPublicPrograms;
import com.dwij.syc.models.DatabaseHelper;
import com.dwij.syc.models.PublicProgram;
import com.dwij.syc.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.syc.R;

public class ProgramWidget extends AppWidgetProvider {

	Context context;

	DatabaseHelper db;

	RemoteViews views;

	AppWidgetManager appWidgetManager;
	int[] appWidgetIds;

	public static ArrayList<PublicProgram> pProgram;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		this.context = context;
		this.appWidgetManager = appWidgetManager;
		this.appWidgetIds = appWidgetIds;
		
		db = new DatabaseHelper(context);

		// Perform this loop procedure for each App Widget that belongs to this provider
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			// Create an Intent to launch ActivityPublicPrograms
			Intent intent = new Intent(context, ActivityPublicPrograms.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

			// Get the layout for the App Widget and attach an on-click listener to the button
			views = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider_layout);
			views.setOnClickPendingIntent(R.id.sync_button, pendingIntent);
			views.setOnClickPendingIntent(R.id.wid_prog_title, pendingIntent);
			new GetProgramTask().execute();

		}
	}

	private class GetProgramTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... centerStr) {
			Log.d(Constants.TAG, "Get program task started");
			String region = "Maharashtra:Pune";
			try {
				Log.d(Constants.TAG, "Searching programs for:" + region);

				HttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost("http://dwijitsolutions.com/apps/sahajatools/webservices/program_get.php?add=" + region);
				HttpResponse response = client.execute(request);

				BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				String page = sb.toString();

				Gson gson = new Gson();
				Type type = new TypeToken<ArrayList<PublicProgram>>() {
				}.getType();
				pProgram = gson.fromJson(page, type);
				if (pProgram == null || pProgram.size() == 0) {
					Log.e(Constants.TAG, "No Center Found:" + page);
					return null;
				} else {
					db.deletePPData();
					for (PublicProgram program : pProgram) {
						db.insertPPData(program);
					}
				}
			} catch (Exception e) {
				Log.e(Constants.TAG, "Error while updating widget", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			pProgram = db.getFuturePPData();

			if (pProgram.size() > 0) {
				PublicProgram pro = pProgram.get(0);
				Log.i(Constants.TAG, "Program Widget: " + pro.getPname());
				views.setTextViewText(R.id.wid_prog_title, pro.getPname());
				
				
				try {
					//String tempDate=pro.getTime_updated().substring(startIndex, endIndex); 
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					SimpleDateFormat formatter2 = new SimpleDateFormat("MMM dd, yyyy KK:mm:a");
					Date d = formatter.parse(pro.getPtime());
					views.setTextViewText(R.id.wid_prog_time, formatter2.format(d)+" | "+pro.getPregion());
				} catch (ParseException e) {
					Log.e(Constants.TAG, "Error while Parsing Dtae", e);
				}
				
				
			} else {
				Log.e(Constants.TAG, "Not programs to display in widget.");
			}

			// Tell the AppWidgetManager to perform an update on the current App Widget
			appWidgetManager.updateAppWidget(appWidgetIds, views);

		}

	}
}