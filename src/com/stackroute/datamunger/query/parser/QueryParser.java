package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.List;

public class QueryParser {

	private QueryParameter queryParameter = new QueryParameter();
	/*
	 * This method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		getFileName(queryString);
		getBaseQuery(queryString);
		getOrderByFields(queryString);
		getGroupByFields(queryString);
		getFields(queryString);
		getAggregateFunctions(queryString);
		getRestrictions(queryString);
		getLogicalOperators(queryString);
		return queryParameter;
	}
		/*
		 * extract the name of the file from the query. File name can be found after the
		 * "from" clause.
		 */
	public String getFileName(String queryString) {

		String[] resArray;
		String res="";
		resArray = queryString.split(" ");

		for (int i=0;i<resArray.length;i++){
			if(resArray[i].toString().equals("from")){
				res = resArray[i+1].toString();				
			}
		}
				
		queryParameter.setFileName(res);
		return res;
	}
	
	/*
	 * 
	 * Extract the baseQuery from the query.This method is used to extract the
	 * baseQuery from the query string. BaseQuery contains from the beginning of the
	 * query till the where clause
	 */
	public String getBaseQuery(String queryString) {
		String baseQuery;
		if(queryString.indexOf("where") > 0)
			baseQuery = queryString.substring(0, queryString.indexOf("where")-1);
		else if(queryString.indexOf("group by") > 0)
			baseQuery = queryString.substring(0, queryString.indexOf("group by")-1);
		else
			baseQuery = queryString;

		queryParameter.setBaseQuery(baseQuery);
		return baseQuery;
	}
		
		
		/*
		 * extract the order by fields from the query string. Please note that we will
		 * need to extract the field(s) after "order by" clause in the query, if at all
		 * the order by clause exists. For eg: select city,winner,team1,team2 from
		 * data/ipl.csv order by city from the query mentioned above, we need to extract
		 * "city". Please note that we can have more than one order by fields.
		 */
	public List<String> getOrderByFields(String queryString) {
		String[] orderByFieldsArray;
		List<String> orderByFieldsList = new ArrayList<String>();	
		
		if((queryString.toLowerCase()).indexOf("order by") < 0) {
			return null;
		}
		else {
		String orderByFields = queryString.substring((queryString.toLowerCase()).indexOf("order by") + 9, queryString.length());
		orderByFieldsArray = orderByFields.split(" ");
		}
		
		for(int i=0;i<orderByFieldsArray.length;i++)
			orderByFieldsList.add(orderByFieldsArray[i]);
		
		queryParameter.setOrderByFields(orderByFieldsList);
		return orderByFieldsList;
	}
		
		
		/*
		 * extract the group by fields from the query string. Please note that we will
		 * need to extract the field(s) after "group by" clause in the query, if at all
		 * the group by clause exists. For eg: select city,max(win_by_runs) from
		 * data/ipl.csv group by city from the query mentioned above, we need to extract
		 * "city". Please note that we can have more than one group by fields.
		 */
	public List<String> getGroupByFields(String queryString) {
		String[] groupByFieldsArray;
		List<String> groupByFieldsList = new ArrayList<String>();
		String groupByFields;
		if((queryString.toLowerCase()).indexOf("group by") < 0) {
			return null;
		}
		else {
			if((queryString.toLowerCase()).indexOf("order by") > 0) {
				groupByFields = queryString.substring((queryString.toLowerCase()).indexOf("group by") + 9, (queryString.toLowerCase()).indexOf("order by"));
			}
			else
				groupByFields = queryString.substring((queryString.toLowerCase()).indexOf("group by") + 9, queryString.length());
		groupByFieldsArray = groupByFields.split(" ");
		}
		
		for(int i=0;i<groupByFieldsArray.length;i++)
			groupByFieldsList.add(groupByFieldsArray[i]);
		
		queryParameter.setGroupByFields(groupByFieldsList);
		return groupByFieldsList;
	}
		
		
		/*
		 * extract the selected fields from the query string. Please note that we will
		 * need to extract the field(s) after "select" clause followed by a space from
		 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
		 * query mentioned above, we need to extract "city" and "win_by_runs". Please
		 * note that we might have a field containing name "from_date" or "from_hrs".
		 * Hence, consider this while parsing.
		 */
	public List<String> getFields(String queryString) {
		String[] fieldsArray = new String[1];
		List<String> fieldsList = new ArrayList<String>();
		if(queryString.indexOf("*") > 0) {
			fieldsArray[0] = "*";
		}
		else {
		String fields = queryString.substring((queryString.indexOf("select"))+7, queryString.indexOf("from"));

		fieldsArray = (fields.replaceAll(" ", "")).split(",");
		}
		
//		for(int i=0;i<fieldsArray.length;i++) {
//			if(fieldsArray[i].toLowerCase().contains("count") || fieldsArray[i].toLowerCase().contains("sum") || fieldsArray[i].toLowerCase().contains("min") || fieldsArray[i].toLowerCase().contains("max")) {
//				fieldsArray[i] = fieldsArray[i].substring(fieldsArray[i].indexOf("(")+1,fieldsArray[i].indexOf(")"));
//			}
//		}
		
		for(int i=0;i<fieldsArray.length;i++) {
			fieldsList.add(fieldsArray[i]);
			}
		
		queryParameter.setFields(fieldsList);
		return fieldsList;
	}
		
		
		
		/*
		 * extract the conditions from the query string(if exists). for each condition,
		 * we need to capture the following: 
		 * 1. Name of field 
		 * 2. condition 
		 * 3. value
		 * 
		 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
		 * where season >= 2008 or toss_decision != bat
		 * 
		 * here, for the first condition, "season>=2008" we need to capture: 
		 * 1. Name of field: season 
		 * 2. condition: >= 
		 * 3. value: 2008
		 * 
		 * the query might contain multiple conditions separated by OR/AND operators.
		 * Please consider this while parsing the conditions.
		 * 
		 */
	public String getConditionsPartQuery(String queryString) {
		String conditionsPart = null;
		if(queryString.indexOf("where") < 0)			
		return null;
		else if((queryString.indexOf("where") > 0) && (queryString.indexOf("group by") > 0)) {
			conditionsPart = queryString.substring(queryString.indexOf("where")+6, queryString.indexOf("group by")-1);
		}
		else if((queryString.indexOf("where") > 0) && (queryString.indexOf("order by") > 0)) {
			conditionsPart = queryString.substring(queryString.indexOf("where")+6, queryString.indexOf("order by")-1);
		}		
		else if(queryString.indexOf("where") > 0) {
			conditionsPart = queryString.substring((queryString.indexOf("where"))+6, (queryString.length()));			
		}
		return conditionsPart;
	}
	
	public List<Restriction> getRestrictions(String queryString) {		
		String conditionsPart = getConditionsPartQuery(queryString);
		List<Restriction> restrictList = null;
		
		if(!(conditionsPart == null)) {
			restrictList = new ArrayList<>();
		String[] conditionsArray = conditionsPart.split(" and | or | not | AND |  OR | NOT ");
		
		for(int i=0;i<conditionsArray.length;i++) {
			
			String symbol = null;
			String[] conditionElements = null;
			
			if(conditionsArray[i].contains(">=")) {
				symbol = ">=";
				conditionElements = conditionsArray[i].split(">=");
			}
			else if(conditionsArray[i].contains("<=")) {
				symbol = "<=";
				conditionElements = conditionsArray[i].split("<=");
			}
			else if(conditionsArray[i].contains("!=")) {
				symbol = "!=";
				conditionElements = conditionsArray[i].split("!=");
			}
			else if(conditionsArray[i].contains("=")) {
				symbol = "=";
				conditionElements = conditionsArray[i].split("=");
			}			
			else if(conditionsArray[i].contains("<")) {
				symbol = "<";
				conditionElements = conditionsArray[i].split("<");
			}
			else if(conditionsArray[i].contains(">")) {
				symbol = ">";
				conditionElements = conditionsArray[i].split(">");
			}
			
			
			String name = conditionElements[0].trim();
			String condition = symbol;
			String value = conditionElements[1].replace("'","").trim();		
			
			Restriction restrict = new Restriction(name, value, condition);
			
			restrictList.add(restrict);
		}
		}

		queryParameter.setRestrictions(restrictList);
		return restrictList;
	}
		
		
		/*
		 * extract the logical operators(AND/OR) from the query, if at all it is
		 * present. For eg: select city,winner,team1,team2,player_of_match from
		 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
		 * bangalore
		 * 
		 * the query mentioned above in the example should return a List of Strings
		 * containing [or,and]
		 */
	public String[] getSplitStrings(String queryString) {
		String[] resArray;
		resArray = queryString.toLowerCase().split(" ");
		return resArray;
	}
	
	public List<String> getLogicalOperators(String queryString) {
		String[] splitString = getSplitStrings(queryString);
		
		List<String> opList = null;
		
		for(int i=0;i<splitString.length;i++) {
			if(((splitString[i].toLowerCase()).equals("and")) || ((splitString[i].toLowerCase()).equals("or")) || ((splitString[i].toLowerCase()).equals("not"))) {
				if(opList == null)
					opList = new ArrayList<>();
				opList.add(splitString[i].toLowerCase());
			}
		}
				
			queryParameter.setLogicalOperators(opList);
			return opList;

	}
		
		/*
		 * extract the aggregate functions from the query. The presence of the aggregate
		 * functions can determined if we have either "min" or "max" or "sum" or "count"
		 * or "avg" followed by opening braces"(" after "select" clause in the query
		 * string. in case it is present, then we will have to extract the same. For
		 * each aggregate functions, we need to know the following: 
		 * 1. type of aggregate function(min/max/count/sum/avg) 
		 * 2. field on which the aggregate function is being applied
		 * 
		 * Please note that more than one aggregate function can be present in a query
		 * 
		 * 
		 */
	public String[] getField(String queryString) {
		String[] fieldsArray = new String[1];
		if(queryString.indexOf("*") > 0) {
			fieldsArray[0] = "*";
		}
		else {
		String fields = queryString.substring((queryString.indexOf("select"))+7, queryString.indexOf("from"));

		fieldsArray = (fields.replaceAll(" ", "")).split(",");
		}
		return fieldsArray;
	}
	
	public List<AggregateFunction> getAggregateFunctions(String queryString) {
		String[] queryFields = getField(queryString);
		List<AggregateFunction> aggrList = new ArrayList<>();
		AggregateFunction af;
		String field, function;
		
		for (int i=0;i<queryFields.length;i++) {
			if ((queryFields[i].toString()).contains("avg") || (queryFields[i].toString()).contains("count") || (queryFields[i].toString()).contains("min") || (queryFields[i].toString()).contains("max") || (queryFields[i].toString()).contains("sum")) {
				function = queryFields[i].substring(0, (queryFields[i].indexOf("(")));
				field = queryFields[i].substring((queryFields[i].indexOf("(")+1), (queryFields[i].indexOf(")")));
				af = new AggregateFunction(field, function); 
				aggrList.add(af);
			}
		}
		
		if(!aggrList.isEmpty()) {
			queryParameter.setAggregateFunctions(aggrList);
			return aggrList;
		}
		else {
			return null;
		}
	}
	
	
}
