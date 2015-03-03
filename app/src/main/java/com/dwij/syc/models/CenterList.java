package com.dwij.syc.models;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CenterList implements Serializable {
	String listname;
	String data;
	ArrayList<Center> centers;
	String[] center_list_items;
	String saveddate;

	public CenterList(String listname, String data, String saveddate) {
		super();
		this.listname = listname;
		this.data = data;
		this.saveddate = saveddate;

		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<Center>>() {
		}.getType();
		centers = gson.fromJson(data, type);
		if (centers == null) {
			centers = new ArrayList<Center>();
		} else {
			Iterator<Center> iter = centers.iterator();
			center_list_items = new String[centers.size()];
			for (int i = 0; iter.hasNext(); i++) {
				Center center = iter.next();
				center_list_items[i] = center.getPlace();
			}
		}
	}

	public String getListname() {
		return listname;
	}

	public void setListname(String listname) {
		this.listname = listname;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public ArrayList<Center> getCenters() {
		return centers;
	}

	public void setCenters(ArrayList<Center> centers) {
		this.centers = centers;
	}

	public String[] getCenter_list_items() {
		return center_list_items;
	}

	public void setCenter_list_items(String[] center_list_items) {
		this.center_list_items = center_list_items;
	}

	public String getSaveddate() {
		return saveddate;
	}

	public void setSaveddate(String saveddate) {
		this.saveddate = saveddate;
	}
}