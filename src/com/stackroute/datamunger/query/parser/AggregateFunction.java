package com.stackroute.datamunger.query.parser;

/* This class is used for storing name of field, aggregate function for 
 * each aggregate function
 * */
public class AggregateFunction {
	
	String field, function;

	public AggregateFunction(String field, String function) {		
		this.field = field;
		this.function = function;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getFunction() {
		return function;
	}

	public String getField() {
		return field;
	}

	@Override
	public String toString() {
		return "AggregateFunction [field=" + field + ", function=" + function + "]";
	}
	
}
