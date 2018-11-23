package com.stackroute.datamunger.query;

import java.util.HashMap;

//Contains the row object as ColumnName/Value. Hence, HashMap is being used
public class Row extends HashMap<String, String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	HashMap<String, String> rowObj = new HashMap<String, String>();

	public HashMap<String, String> getRowObj() {
		return rowObj;
	}

	public void setRowObj(HashMap<String, String> rowObj) {
		this.rowObj = rowObj;
	}
	
}
