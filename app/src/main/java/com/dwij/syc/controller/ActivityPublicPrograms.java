/*
 * Author : ErVaLt /	 techwavedev.com
 * Description : TabLayout Andorid App
 */
package com.dwij.syc.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dwij.syc.adapter.PublicProgramsAdapter;
import com.dwij.syc.models.DatabaseHelper;
import com.dwij.syc.models.PublicProgram;
import com.dwij.syc.utils.Constants;
import com.dwij.syc.widget.PullToRefreshListView;
import com.dwij.syc.widget.PullToRefreshListView.OnRefreshListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.syc.R;

public class ActivityPublicPrograms extends SherlockActivity implements OnItemClickListener {

    private PullToRefreshListView ppListView;
    private PublicProgramsAdapter ppAdapter;
    public static ArrayList<PublicProgram> pProgram;
    Cursor cur = null;
    String programsTableName = "programs";
    //SQLiteDatabase myDB = null;
    DatabaseHelper db;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_list);
        db = new DatabaseHelper(this);

        ppListView = (PullToRefreshListView) findViewById(R.id.publicprogramlistview);

        pProgram = new ArrayList<PublicProgram>();
        pProgram.clear();

        //Return Arraylist of Public Program in Database
        pProgram = db.getPPData();

        ppListView.onRefreshComplete();
        ppAdapter = new PublicProgramsAdapter(ActivityPublicPrograms.this, pProgram);
        ppListView.setAdapter(ppAdapter);

        ppListView.setOnItemClickListener(this);
        ppListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // refresh from online + db
                GetProgramTask task = new GetProgramTask();
                task.execute();
            }
        });

        Log.d(Constants.TAG, "ActivityPublicPrograms Started");

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
                Log.e(Constants.TAG, "Error Connecting Server", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            ppAdapter = new PublicProgramsAdapter(ActivityPublicPrograms.this, pProgram);
            ppListView.setAdapter(ppAdapter);
            ppListView.onRefreshComplete();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#336699")));
        getSupportMenuInflater().inflate(R.menu.menu_programadd, menu);
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
            case R.id.menu_addprogram:
                Intent iAddProgram = new Intent(ActivityPublicPrograms.this, ActivityProgramAdd.class);
                startActivity(iAddProgram);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adpt, View arg1, int position, long arg3) {
        if (adpt == ppListView) {

            Intent i = new Intent(ActivityPublicPrograms.this, ActivityProgramDisplay.class);
            i.putExtra("pp", pProgram.get(position));
            startActivity(i);

        }
    }
}
