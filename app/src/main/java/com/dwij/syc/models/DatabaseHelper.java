package com.dwij.syc.models;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dwij.syc.utils.Constants;

public class DatabaseHelper extends SQLiteOpenHelper {

	final static String dbName = "SYCDB";

	final static int DATABASE_VERSION = 1;

	//SYCS TABLE NAMES
	final static String ppTable = "programs";
	final static String centersTable = "centers";

	//SYCDB centersTable(centers) Fields
	final static String center_id = "id";
	final static String center_place = "PLACE";
	final static String center_region = "REGION";
	final static String center_address = "ADDRESS";
	final static String center_time = "CENTER_TIME";
	final static String center_contact_1 = "CONTACT_1";
	final static String center_contact_2 = "CONTACT_2";
	final static String center_strength = "STRENGTH";
	final static String center_lat = "lat";
	final static String center_lng = "lng";
	final static String center_transaction = "TRANSACTIONS";
	final static String center_auth = "AUTH";

	//SYCDB ppTable(project table) Fields

	final static String program_id = "id";
	final static String program_name = "pname";
	final static String program_type = "ptype";
	final static String program_region = "pregion";
	final static String program_address = "paddress";
	final static String program_lat = "plat";
	final static String program_lng = "plng";
	final static String program_time = "ptime";
	final static String program_work_start = "pwork_start";
	final static String program_address_living = "paddress_living";
	final static String program_contact_1 = "contact_1";
	final static String program_contact_2 = "contact_2";
	final static String program_num_people = "num_people";
	final static String program_description = "description";
	final static String program_center = "center";
	final static String program_auth = "auth";
	final static String program_feeder_email = "feeder_email";
	final static String program_feeder_phone = "feeder_phone";
	final static String program_time_updated = "time_updated";

	public DatabaseHelper(Context context) {
		super(context, dbName, null, DATABASE_VERSION);

		Log.d(Constants.TAG, "DatabaseHelper Init()");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(Constants.TAG, "DatabaseHelper creating tables.");

		db.execSQL("CREATE TABLE " + centersTable + " (" + center_id + " INTEGER," + center_place + " TEXT," + center_region + " TEXT,"
				+ center_address + " TEXT," + center_time + " TEXT," + center_contact_1 + " TEXT," + center_contact_2 + " TEXT,"
				+ center_strength + " INTEGER," + center_lat + " DOUBLE," + center_lng + " DOUBLE," + center_transaction + " TEXT,"
				+ center_auth + " TEXT)");

		db.execSQL("CREATE TABLE " + ppTable + " (" + program_id + " INTEGER," + program_name + " TEXT," + program_type + " TEXT,"
				+ program_region + " TEXT," + program_address + " TEXT," + program_lat + " DOUBLE," + program_lng + " DOUBLE,"
				+ program_time + " TIME," + program_work_start + " TIME," + program_address_living + " TEXT," + program_contact_1
				+ " TEXT," + program_contact_2 + " TEXT," + program_num_people + " INTEGER," + program_description + " TEXT,"
				+ program_center + " INTEGER," + program_auth + " TEXT," + program_feeder_email + " TEXT," + program_feeder_phone
				+ " TEXT," + program_time_updated + " TEXT)");

		//db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void insertCentersData(Center c) {
		SQLiteDatabase mydb = this.getWritableDatabase();

		ContentValues cv = new ContentValues();

		cv.put(center_id, c.getId());
		cv.put(center_place, c.getPlace());
		cv.put(center_region, c.getRegion());
		cv.put(center_address, c.getAddress());
		cv.put(center_time, c.getCenter_time());
		cv.put(center_contact_1, c.getContact_1());
		cv.put(center_contact_2, c.getContact_2());
		cv.put(center_strength, c.getStrength());
		cv.put(center_lat, c.getLat());
		cv.put(center_lng, c.getLng());
		cv.put(center_transaction, c.getTRANSACTION());
		cv.put(center_auth, c.getAUTH());

		mydb.insert(centersTable, null, cv);

		Log.d(Constants.TAG, "Center Data Insertion in table is done.");

	}

	public void updateCentersData(Center c) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		Log.d(Constants.TAG, "In  updateCentersData()");

		//cv.put(center_id, c.getId());
		cv.put(center_place, c.getPlace());
		cv.put(center_region, c.getRegion());
		cv.put(center_address, c.getAddress());
		cv.put(center_time, c.getCenter_time());
		cv.put(center_contact_1, c.getContact_1());
		cv.put(center_contact_2, c.getContact_2());
		cv.put(center_strength, c.getStrength());
		cv.put(center_lat, c.getLat());
		cv.put(center_lng, c.getLng());
		cv.put(center_transaction, c.getTRANSACTION());
		cv.put(center_auth, c.getAUTH());

		// updating row
		db.update(centersTable, cv, center_id + " = ?", new String[] { String.valueOf(c.getId()) });
	}

	public boolean checkDataAvailOrNot(int id) {

		//Log.d(Constants.TAG, "In checkDataAvailOrNot() ");
		SQLiteDatabase mydb = this.getReadableDatabase();
		Cursor cur = mydb.rawQuery("SELECT *  FROM " + centersTable + " WHERE id='" + id + "'", new String[] {});
		if (cur != null && cur.moveToFirst()) {
			return true;
		}
		cur.close();
		return false;
	}

	public Cursor SortSearchedPlaces(String str) {

		//Log.d(Constants.TAG, "In SortSearchedPlaces() ");
		SQLiteDatabase mydb = this.getReadableDatabase();

		Cursor cur = mydb.rawQuery("SELECT *  FROM " + centersTable + " WHERE (PLACE LIKE '%" + str + "%') OR (REGION LIKE '%" + str
				+ "%') OR (CONTACT_1 LIKE '%" + str + "%') OR (CONTACT_2 LIKE '%" + str + "%') OR (ADDRESS LIKE '%" + str
				+ "%') ORDER BY PLACE ", new String[] {});

		if (cur != null)
			System.out.println("*********************************");

		return cur;
	}

	public void insertPPData(PublicProgram pp) {
		SQLiteDatabase mydb = this.getWritableDatabase();

		ContentValues cv = new ContentValues();

		cv.put(program_id, pp.getId());
		cv.put(program_name, pp.getPname());
		cv.put(program_type, pp.getPtype());
		cv.put(program_region, pp.getPregion());
		cv.put(program_address, pp.getPaddress());
		cv.put(program_lat, pp.getPlat());
		cv.put(program_lng, pp.getPlng());
		cv.put(program_time, pp.getPtime());
		cv.put(program_work_start, pp.getPwork_start());
		cv.put(program_address_living, pp.getPaddress_living());
		cv.put(program_contact_1, pp.getContact_1());
		cv.put(program_contact_2, pp.getContact_2());
		cv.put(program_num_people, pp.getNum_people());
		cv.put(program_description, pp.getDescription());
		cv.put(program_center, pp.getCenter());
		cv.put(program_auth, pp.getAuth());
		cv.put(program_feeder_email, pp.getFeeder_email());
		cv.put(program_feeder_phone, pp.getFeeder_phone());
		cv.put(program_time_updated, pp.getTime_updated());

		mydb.insert(ppTable, null, cv);

		Log.d(Constants.TAG, "Public Program Data Insertion in table is done.");

	}
	
	public void updatePPData(PublicProgram pp) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		Log.d(Constants.TAG, "In  updatePPData()");

		//cv.put(program_id, pp.getId());
		cv.put(program_name, pp.getPname());
		cv.put(program_type, pp.getPtype());
		cv.put(program_region, pp.getPregion());
		cv.put(program_address, pp.getPaddress());
		cv.put(program_lat, pp.getPlat());
		cv.put(program_lng, pp.getPlng());
		cv.put(program_time, pp.getPtime());
		cv.put(program_work_start, pp.getPwork_start());
		cv.put(program_address_living, pp.getPaddress_living());
		cv.put(program_contact_1, pp.getContact_1());
		cv.put(program_contact_2, pp.getContact_2());
		cv.put(program_num_people, pp.getNum_people());
		cv.put(program_description, pp.getDescription());
		cv.put(program_center, pp.getCenter());
		cv.put(program_auth, pp.getAuth());
		cv.put(program_feeder_email, pp.getFeeder_email());
		cv.put(program_feeder_phone, pp.getFeeder_phone());
		cv.put(program_time_updated, pp.getTime_updated());

		Log.d(Constants.TAG, "Updated Successfully");

		// updating row
		db.update(ppTable, cv, program_id + " = ?", new String[] { String.valueOf(pp.getId()) });
	}

	public ArrayList<PublicProgram> getPPData() {

		ArrayList<PublicProgram> ppList = new ArrayList<PublicProgram>();
		Log.d(Constants.TAG, "Started getting PPData from db ");
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT *  FROM " + ppTable + "", new String[] {});
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {

					ppList.add(new PublicProgram(cur.getInt(cur.getColumnIndex(program_id)),
							cur.getString(cur.getColumnIndex(program_name)), cur.getString(cur.getColumnIndex(program_type)), cur
									.getString(cur.getColumnIndex(program_region)), cur.getString(cur.getColumnIndex(program_address)), cur
									.getDouble(cur.getColumnIndex(program_lat)), cur.getDouble(cur.getColumnIndex(program_lng)), cur
									.getString(cur.getColumnIndex(program_time)), cur.getString(cur.getColumnIndex(program_work_start)),
							cur.getString(cur.getColumnIndex(program_address_living)),
							cur.getString(cur.getColumnIndex(program_contact_1)), cur.getString(cur.getColumnIndex(program_contact_2)), cur
									.getInt(cur.getColumnIndex(program_num_people)),
							cur.getString(cur.getColumnIndex(program_description)), cur.getInt(cur.getColumnIndex(program_center)), cur
									.getString(cur.getColumnIndex(program_auth)), cur.getString(cur.getColumnIndex(program_feeder_email)),
							cur.getString(cur.getColumnIndex(program_feeder_phone)),
							cur.getString(cur.getColumnIndex(program_time_updated)), ""));

					Log.d(Constants.TAG, "Place : " + cur.getString(cur.getColumnIndex(program_name)));

				} while (cur.moveToNext());
			}
			cur.close();
		}
		db.close();
		Log.d(Constants.TAG, "Get Public Programs data from Database is Done  and List Size is=" + ppList.size());

		return ppList;
	}
	
	public ArrayList<PublicProgram> getFuturePPData() {

		ArrayList<PublicProgram> ppList = new ArrayList<PublicProgram>();
		Log.d(Constants.TAG, "Started getting PPData from db ");
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cur = db.rawQuery("SELECT * FROM " + ppTable + " ORDER BY "+program_time+" DESC LIMIT 1", new String[] {});
		
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					
					ppList.add(new PublicProgram(cur.getInt(cur.getColumnIndex(program_id)),
							cur.getString(cur.getColumnIndex(program_name)), cur.getString(cur.getColumnIndex(program_type)), cur
									.getString(cur.getColumnIndex(program_region)), cur.getString(cur.getColumnIndex(program_address)), cur
									.getDouble(cur.getColumnIndex(program_lat)), cur.getDouble(cur.getColumnIndex(program_lng)), cur
									.getString(cur.getColumnIndex(program_time)), cur.getString(cur.getColumnIndex(program_work_start)),
							cur.getString(cur.getColumnIndex(program_address_living)),
							cur.getString(cur.getColumnIndex(program_contact_1)), cur.getString(cur.getColumnIndex(program_contact_2)), cur
									.getInt(cur.getColumnIndex(program_num_people)),
							cur.getString(cur.getColumnIndex(program_description)), cur.getInt(cur.getColumnIndex(program_center)), cur
									.getString(cur.getColumnIndex(program_auth)), cur.getString(cur.getColumnIndex(program_feeder_email)),
							cur.getString(cur.getColumnIndex(program_feeder_phone)),
							cur.getString(cur.getColumnIndex(program_time_updated)), ""));

					Log.d(Constants.TAG, "Place : " + cur.getString(cur.getColumnIndex(program_name)));

				} while (cur.moveToNext());
			}
			cur.close();
		}
		db.close();
		Log.d(Constants.TAG, "Get Public Programs data from Database is Done  and List Size is=" + ppList.size());

		return ppList;
	}

	public void deletePPData() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + ppTable, new String[] {});

		Log.d(Constants.TAG, "Empty Public Programs Table");
		db.close();
	}

}