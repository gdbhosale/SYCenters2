package com.dwij.syc.models;

import java.io.Serializable;

public class Center implements Serializable {
	private static final long serialVersionUID = 1L;

	String place, region, address, center_time, contact_1, contact_2;
	int strength, id;

	String contact_1_name, contact_1_phone, contact_1_email;
	String contact_2_name, contact_2_phone, contact_2_email;

	String updatedBy;

	String DAY, HOURS, MINUTES, AMPM;
	double lat;
	double lng;
	String TRANSACTION, AUTH;

	public Center(int id, String place, String region, String address, String center_time, String contact_1, String contact_2, int strength) {
		super();
		this.id = id;
		this.place = place;
		this.strength = strength;
		this.region = region;
		this.address = address;
		this.center_time = center_time;
		this.contact_1 = contact_1;
		this.contact_2 = contact_2;

		contact_1_name = contact_1.substring(0, contact_1.indexOf("(")).trim();
		contact_1_phone = contact_1.substring(contact_1.indexOf("(") + 1, contact_1.indexOf(")")).trim();
		contact_1_email = contact_1.substring(contact_1.indexOf(")") + 1).trim();

		contact_2_name = contact_2.substring(0, contact_2.indexOf("(")).trim();
		contact_2_phone = contact_2.substring(contact_2.indexOf("(") + 1, contact_2.indexOf(")")).trim();
		contact_2_email = contact_2.substring(contact_2.indexOf(")") + 1).trim();

		String[] strs = center_time.split(" ");
		//EVERY SATURDAY 4:00AM
		DAY = strs[1];
		HOURS = strs[2].substring(0, strs[2].indexOf(':'));
		MINUTES = strs[2].substring(strs[2].indexOf(':') + 1, strs[2].length() - 2);
		AMPM = strs[2].substring(strs[2].length() - 2, strs[2].length());

	}

	public Center(String place, String region, String address, String center_time, String contact_1, String contact_2, int strength) {
		super();

		this.place = place;
		this.strength = strength;
		this.region = region;
		this.address = address;
		this.center_time = center_time;
		this.contact_1 = contact_1;
		this.contact_2 = contact_2;

		contact_1_name = contact_1.substring(0, contact_1.indexOf("(")).trim();
		contact_1_phone = contact_1.substring(contact_1.indexOf("(") + 1, contact_1.indexOf(")")).trim();
		contact_1_email = contact_1.substring(contact_1.indexOf(")") + 1).trim();

		contact_2_name = contact_2.substring(0, contact_2.indexOf("(")).trim();
		contact_2_phone = contact_2.substring(contact_2.indexOf("(") + 1, contact_2.indexOf(")")).trim();
		contact_2_email = contact_2.substring(contact_2.indexOf(")") + 1).trim();

		String[] strs = center_time.split(" ");
		//EVERY SATURDAY 4:00AM
		DAY = strs[1];
		HOURS = strs[2].substring(0, strs[2].indexOf(':'));
		MINUTES = strs[2].substring(strs[2].indexOf(':') + 1, strs[2].length() - 2);
		AMPM = strs[2].substring(strs[2].length() - 2, strs[2].length());

	}

	public void populate() {
		contact_1_name = contact_1.substring(0, contact_1.indexOf("(")).trim();
		contact_1_phone = contact_1.substring(contact_1.indexOf("(") + 1, contact_1.indexOf(")")).trim();
		contact_1_email = contact_1.substring(contact_1.indexOf(")") + 1).trim();

		contact_2_name = contact_2.substring(0, contact_2.indexOf("(")).trim();
		contact_2_phone = contact_2.substring(contact_2.indexOf("(") + 1, contact_2.indexOf(")")).trim();
		contact_2_email = contact_2.substring(contact_2.indexOf(")") + 1).trim();

		String[] strs = center_time.split(" ");
		//EVERY SATURDAY 4:00AM
		DAY = strs[1];
		HOURS = strs[2].substring(0, strs[2].indexOf(':'));
		MINUTES = strs[2].substring(strs[2].indexOf(':') + 1, strs[2].length() - 2);
		AMPM = strs[2].substring(strs[2].length() - 2, strs[2].length());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTRANSACTION() {
		return TRANSACTION;
	}

	public void setTRANSACTION(String tRANSACTION) {
		TRANSACTION = tRANSACTION;
	}

	public String getAUTH() {
		return AUTH;
	}

	public void setAUTH(String aUTH) {
		AUTH = aUTH;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCenter_time() {
		return center_time;
	}

	public void setCenter_time(String centerTime) {
		center_time = centerTime;
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

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public String getContact_1_name() {
		return contact_1_name;
	}

	public void setContact_1_name(String contact_1Name) {
		contact_1_name = contact_1Name;
	}

	public String getContact_1_phone() {
		return contact_1_phone;
	}

	public void setContact_1_phone(String contact_1Phone) {
		contact_1_phone = contact_1Phone;
	}

	public String getContact_1_email() {
		return contact_1_email;
	}

	public void setContact_1_email(String contact_1Email) {
		contact_1_email = contact_1Email;
	}

	public String getContact_2_name() {
		return contact_2_name;
	}

	public void setContact_2_name(String contact_2Name) {
		contact_2_name = contact_2Name;
	}

	public String getContact_2_phone() {
		return contact_2_phone;
	}

	public void setContact_2_phone(String contact_2Phone) {
		contact_2_phone = contact_2Phone;
	}

	public String getContact_2_email() {
		return contact_2_email;
	}

	public void setContact_2_email(String contact_2Email) {
		contact_2_email = contact_2Email;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getDAY() {
		return DAY;
	}

	public void setDAY(String dAY) {
		DAY = dAY;
	}

	public String getHOURS() {
		return HOURS;
	}

	public void setHOURS(String hOURS) {
		HOURS = hOURS;
	}

	public String getMINUTES() {
		return MINUTES;
	}

	public void setMINUTES(String mINUTES) {
		MINUTES = mINUTES;
	}

	public String getAMPM() {
		return AMPM;
	}

	public void setAMPM(String aMPM) {
		AMPM = aMPM;
	}

	@Override
	public String toString() {
		return "Center [AMPM=" + AMPM + ", DAY=" + DAY + ", HOURS=" + HOURS + ", MINUTES=" + MINUTES + ", address=" + address
				+ ", center_time=" + center_time + ", contact_1=" + contact_1 + ", contact_1_email=" + contact_1_email
				+ ", contact_1_name=" + contact_1_name + ", contact_1_phone=" + contact_1_phone + ", contact_2=" + contact_2
				+ ", contact_2_email=" + contact_2_email + ", contact_2_name=" + contact_2_name + ", contact_2_phone=" + contact_2_phone
				+ ", place=" + place + ", region=" + region + ", strength=" + strength + ", updatedBy=" + updatedBy + "]";
	}
}