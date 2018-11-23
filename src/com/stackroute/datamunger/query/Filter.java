package com.stackroute.datamunger.query;

import java.util.List;

import com.stackroute.datamunger.query.parser.Restriction;

import javax.script.*;

//This class contains methods to evaluate expressions
public class Filter {
	
	/* 
	 * The evaluateExpression() method of this class is responsible for evaluating 
	 * the expressions mentioned in the query. It has to be noted that the process 
	 * of evaluating expressions will be different for different data types. there 
	 * are 6 operators that can exist within a query i.e. >=,<=,<,>,!=,= This method 
	 * should be able to evaluate all of them. 
	 * Note: while evaluating string expressions, please handle uppercase and lowercase 
	 * 
	 */
	
	Header header = new Header();
	
	public Boolean evaluateExpression(List<Restriction> restrictions, List<String> logicalOps, String[] dataLineArray, Header header) {				
		
		String evaluateString = "";
		this.header= header;

		for(int i=0;i<restrictions.size();i++) {
			Boolean result = null;
			if((restrictions.get(i).getCondition()).equals("=")) {
				int headerIndex = header.get(restrictions.get(i).getPropertyName());
				String actual = dataLineArray[headerIndex-1];
				result = equalTo(actual, restrictions.get(i).getPropertyValue().toString());			
			}
			else if((restrictions.get(i).getCondition()).equals("!=")) {
				int headerIndex = header.get(restrictions.get(i).getPropertyName());
				String actual = dataLineArray[headerIndex-1];
				result = notEqualTo(actual, restrictions.get(i).getPropertyValue().toString());
			}
			else if((restrictions.get(i).getCondition()).equals(">")) {
				int headerIndex = header.get(restrictions.get(i).getPropertyName());
				String actual = dataLineArray[headerIndex-1];
				result = greaterThan(actual, restrictions.get(i).getPropertyValue().toString());				
			}
			else if((restrictions.get(i).getCondition()).equals(">=")) {
				int headerIndex = header.get(restrictions.get(i).getPropertyName());
				String actual = dataLineArray[headerIndex-1];
				result = greaterThanOrEqualTo(actual, restrictions.get(i).getPropertyValue().toString());
			}
			else if((restrictions.get(i).getCondition()).equals("<")) {
				int headerIndex = header.get(restrictions.get(i).getPropertyName());
				String actual = dataLineArray[headerIndex-1];
				result = lessThan(actual, restrictions.get(i).getPropertyValue().toString());
			}
			else if((restrictions.get(i).getCondition()).equals("<=")) {
				int headerIndex = header.get(restrictions.get(i).getPropertyName());
				String actual = dataLineArray[headerIndex-1];
				result = lessThanOrEqualTo(actual, restrictions.get(i).getPropertyValue().toString());
			}
						
			evaluateString = evaluateString.concat(result.toString()).concat(" ");
			
		}
		
		evaluateString.trim();
//		System.out.println("LogicalOps: "+logicalOps);
		if(logicalOps != null) {
			evaluateString = evaluateString.trim().replaceAll(" ", "<condition>");
		for(int i=0;i<logicalOps.size();i++) {
			if(logicalOps.get(i).equalsIgnoreCase("and"))
				evaluateString = evaluateString.replaceFirst("<condition>", " & ");
			else if(logicalOps.get(i).equalsIgnoreCase("or"))
				evaluateString = evaluateString.replaceFirst("<condition>", " | ");			
		}
		}
		Boolean eval = null;
		if(evaluateString.contains("&") || evaluateString.contains("|")) {
		Integer preEval = null;
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		try {
			preEval = Integer.parseInt(engine.eval(evaluateString).toString());
		} catch (NumberFormatException | ScriptException e) {
			System.out.println("Error in pre-evaluation - "+e.getMessage());
		}
		
		
		if(preEval == 1)
			eval = true;
		else if(preEval == 0)
			eval = false;
		}
		else {
			if(evaluateString.trim().equals("true"))
				eval = true;
			else
				eval = false;
		}
		
		return eval;
	}
	
	
	
	
	//Method containing implementation of equalTo operator
	public Boolean equalTo(String actual, String expected) {
		Boolean result = false;
		if(expected.matches("[0-9]+") || expected.contains(".")) {
			result =  (Integer.parseInt(actual) == Integer.parseInt(expected));
		}		
		else {
			result =  (expected.equals(actual));
		}
		
		return result;
	}
	

	//Method containing implementation of notEqualTo operator
	public Boolean notEqualTo(String actual, String expected) {
		Boolean result = false;
		if(expected.matches("[0-9]+") || expected.contains(".")) {
			result =  (Integer.parseInt(actual) != Integer.parseInt(expected));
		}		
		else {
			result =  (!expected.equals(actual));
		}
		
		return result;
	}

	
	//Method containing implementation of greaterThan operator
	public Boolean greaterThan(String actual, String expected) {
		Boolean result = false;		
		if(expected.matches("[0-9]+") || expected.contains(".")) {
			result =  (Integer.parseInt(actual) > Integer.parseInt(expected));
		}
		
		return result;
	}
	
	
	//Method containing implementation of greaterThanOrEqualTo operator
	public Boolean greaterThanOrEqualTo(String actual, String expected) {
		Boolean result = false;
		if(expected.matches("[0-9]+") || expected.contains(".")) {
			result =  (Integer.parseInt(actual) >= Integer.parseInt(expected));
		}
		
		return result;
	}
	

	//Method containing implementation of lessThan operator
	public Boolean lessThan(String actual, String expected) {
		Boolean result = false;
		if(expected.matches("[0-9]+") || expected.contains(".")) {
			result =  (Integer.parseInt(actual) < Integer.parseInt(expected));
		}
		
		return result;
	}
	

	//Method containing implementation of lessThanOrEqualTo operator
	public Boolean lessThanOrEqualTo(String actual, String expected) {
		Boolean result = false;
		if(expected.matches("[0-9]+") || expected.contains(".")) {
			result =  (Integer.parseInt(actual) <= Integer.parseInt(expected));
		}
		
		return result;
	}
	
	
}
