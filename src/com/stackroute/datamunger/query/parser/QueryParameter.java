package com.stackroute.datamunger.query.parser;

import java.util.List;
/* 
 * This class will contain the elements of the parsed Query String such as conditions,
 * logical operators,aggregate functions, file name, fields group by fields, order by
 * fields, Query Type
 * */
public class QueryParameter {

	private String queryString, fileName, baseQuery, QUERY_TYPE = "select";
	private List<String> fields, logicalOperators, orderByFields, groupByFields;
	private List<AggregateFunction> aggregateFunctions;
	private List<Restriction> restrictions;
	
	public String getFileName() {
		return fileName;
	}

	public List<String> getFields() {
		return fields;
	}

	public List<Restriction> getRestrictions() {
		return restrictions;
	}

	public String getBaseQuery() {
		return baseQuery;
	}

	public List<AggregateFunction> getAggregateFunctions() {
		return aggregateFunctions;
	}

	public List<String> getLogicalOperators() {
		return logicalOperators;
	}
	
	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setBaseQuery(String baseQuery) {
		this.baseQuery = baseQuery;
	}

	public void setQUERY_TYPE(String qUERY_TYPE) {
		QUERY_TYPE = qUERY_TYPE;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public void setLogicalOperators(List<String> logicalOperators) {
		this.logicalOperators = logicalOperators;
	}

	public void setOrderByFields(List<String> orderByFields) {
		this.orderByFields = orderByFields;
	}

	public void setGroupByFields(List<String> groupByFields) {
		this.groupByFields = groupByFields;
	}

	public void setAggregateFunctions(List<AggregateFunction> aggregateFunctions) {
		this.aggregateFunctions = aggregateFunctions;
	}

	public void setRestrictions(List<Restriction> restrictions) {
		this.restrictions = restrictions;
	}

	public List<String> getGroupByFields() {
		return groupByFields;
	}

	public List<String> getOrderByFields() {
		return orderByFields;
	}

	public String getQUERY_TYPE() {
		return QUERY_TYPE;
	}

}
