package com.dwij.syc.utils;

import java.util.ArrayList; 

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.dwij.syc.models.State;

public class MyXMLHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	public static ArrayList<State> stateList;
	
	State stateCurrent;
	ArrayList<String> districtsCurrent;
	

	/**
	 * Called when tag starts ( ex:- <name>AndroidPeople</name> -- <name> )
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("Country")) {
			/** Start */
			stateList = new ArrayList<State>();
		} else if (localName.equals("State")) {
			/** Get attribute value */
			String attr = attributes.getValue("id");
			stateCurrent = new State(attr);
			districtsCurrent = new ArrayList<String>();
		} else if  (localName.equals("Dist")) {
			String attr = attributes.getValue("id");
			districtsCurrent.add(attr);
		}
	}

	/**
	 * Called when tag closing ( ex:- <name>AndroidPeople</name> -- </name> )
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		currentElement = false;

		/** set value */
		if (localName.equalsIgnoreCase("State")) {
			stateCurrent.districts = districtsCurrent;
			stateList.add(stateCurrent);
		}
	}

	/**
	 * Called to get tag characters ( ex:- <name>AndroidPeople</name> -- to get
	 * AndroidPeople Character )
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {

		if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
		}

	}

}
