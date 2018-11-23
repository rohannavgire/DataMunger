package com.stackroute.datamunger.query;

import java.util.HashMap;

//Header class containing a Collection containing the headers
public class Header extends HashMap<String, Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	HashMap<String, Integer> headerMap = new HashMap<String, Integer>();

	public HashMap<String, Integer> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(HashMap<String, Integer> headerMap) {
		this.headerMap = headerMap;
	}
	
	
}
