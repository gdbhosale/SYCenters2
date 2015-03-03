package com.dwij.syc.models;

import java.util.ArrayList;

public class State {
	public String country;
	public String stateName;
	public ArrayList<String> districts = new ArrayList<String>();

	public State(String stateName) {
		super();
		this.stateName = stateName;
	}
}