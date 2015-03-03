package com.dwij.syc.models;

import java.io.Serializable;

import com.dwij.syc.utils.Constants;

import android.util.Log;

public class PublicProgram implements Serializable {

	private static final long serialVersionUID = 1L;

	int id;
	String pname, ptype, pregion, paddress;
	double plat, plng;
	String ptime;
	String pwork_start, paddress_living, contact_1, contact_2;
	int num_people;
	String description;
	int center;
	String auth, feeder_email, feeder_phone, time_updated;
	private String thumbUrl;
	
	String contact_1_name, contact_1_phone, contact_1_email;
	String contact_2_name, contact_2_phone, contact_2_email;

	public PublicProgram(int id, String pname, String ptype, String pregion, String paddress, double plat, double plng, String ptime,
			String pwork_start, String paddress_living, String contact_1, String contact_2, int num_people, String description, int center,
			String auth, String feeder_email, String feeder_phone, String time_updated, String thumUrl) {
		super();
		this.id = id;
		this.pname = pname;
		this.ptype = ptype;
		this.pregion = pregion;
		this.paddress = paddress;
		this.plat = plat;
		this.plng = plng;
		this.ptime = ptime;
		this.pwork_start = pwork_start;
		this.paddress_living = paddress_living;
		this.contact_1 = contact_1;
		this.contact_2 = contact_2;
		this.num_people = num_people;
		this.description = description;
		this.center = center;
		this.auth = auth;
		this.feeder_email = feeder_email;
		this.feeder_phone = feeder_phone;
		this.time_updated = time_updated;
		this.thumbUrl = thumUrl;
		
		contact_1_name = contact_1.substring(0, contact_1.indexOf("(")).trim();
		contact_1_phone = contact_1.substring(contact_1.indexOf("(") + 1, contact_1.indexOf(")")).trim();
		contact_1_email = contact_1.substring(contact_1.indexOf(")") + 1).trim();

		contact_2_name = contact_2.substring(0, contact_2.indexOf("(")).trim();
		contact_2_phone = contact_2.substring(contact_2.indexOf("(") + 1, contact_2.indexOf(")")).trim();
		contact_2_email = contact_2.substring(contact_2.indexOf(")") + 1).trim();

	}
	
	

	public String getContact_1_name() {
		return contact_1_name;
	}



	public void setContact_1_name(String contact_1_name) {
		this.contact_1_name = contact_1_name;
	}



	public String getContact_1_phone() {
		return contact_1_phone;
	}



	public void setContact_1_phone(String contact_1_phone) {
		this.contact_1_phone = contact_1_phone;
	}



	public String getContact_1_email() {
		return contact_1_email;
	}



	public void setContact_1_email(String contact_1_email) {
		this.contact_1_email = contact_1_email;
	}



	public String getContact_2_name() {
		return contact_2_name;
	}



	public void setContact_2_name(String contact_2_name) {
		this.contact_2_name = contact_2_name;
	}



	public String getContact_2_phone() {
		return contact_2_phone;
	}



	public void setContact_2_phone(String contact_2_phone) {
		this.contact_2_phone = contact_2_phone;
	}



	public String getContact_2_email() {
		return contact_2_email;
	}



	public void setContact_2_email(String contact_2_email) {
		this.contact_2_email = contact_2_email;
	}



	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	public String getPregion() {
		return pregion;
	}

	public void setPregion(String pregion) {
		this.pregion = pregion;
	}

	public String getPaddress() {
		return paddress;
	}

	public void setPaddress(String paddress) {
		this.paddress = paddress;
	}

	public double getPlat() {
		return plat;
	}

	public void setPlat(double plat) {
		this.plat = plat;
	}

	public double getPlng() {
		return plng;
	}

	public void setPlng(double plng) {
		this.plng = plng;
	}

	public String getPtime() {
		return ptime;
	}

	public void setPtime(String ptime) {
		this.ptime = ptime;
	}

	public String getPwork_start() {
		return pwork_start;
	}

	public void setPwork_start(String pwork_start) {
		this.pwork_start = pwork_start;
	}

	public String getPaddress_living() {
		return paddress_living;
	}

	public void setPaddress_living(String paddress_living) {
		this.paddress_living = paddress_living;
	}

	public String getContact_1() {
		return contact_1;
	}

	public void setContact_1(String contact_1) {
		this.contact_1 = contact_1;
	}

	public String getContact_2() {
		return contact_2;
	}

	public void setContact_2(String contact_2) {
		this.contact_2 = contact_2;
	}

	public int getNum_people() {
		return num_people;
	}

	public void setNum_people(int num_people) {
		this.num_people = num_people;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCenter() {
		return center;
	}

	public void setCenter(int center) {
		this.center = center;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getFeeder_email() {
		return feeder_email;
	}

	public void setFeeder_email(String feeder_email) {
		this.feeder_email = feeder_email;
	}

	public String getFeeder_phone() {
		return feeder_phone;
	}

	public void setFeeder_phone(String feeder_phone) {
		this.feeder_phone = feeder_phone;
	}

	public String getTime_updated() {
		return time_updated;
	}

	public void setTime_updated(String time_updated) {
		this.time_updated = time_updated;
	}

}
