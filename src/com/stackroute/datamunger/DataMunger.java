package com.stackroute.datamunger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.stackroute.datamunger.query.Query;
import com.stackroute.datamunger.writer.JsonWriter;

public class DataMunger {

	public static void main(String[] args) {

		
		// Read the query from the user
		BufferedReader qstring = new BufferedReader(new InputStreamReader(System.in));
		String queryString = null;
		try {
			queryString = qstring.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("You entered: '" + queryString + "'");
		System.out.println("-------------------------------------------------");

		/*
		 * Instantiate Query class. This class is responsible for: 1. Parsing the query
		 * 2. Select the appropriate type of query processor 3. Get the resultSet which
		 * is populated by the Query Processor
		 */
		Query queryObj = new Query();

		/*
		 * Instantiate JsonWriter class. This class is responsible for writing the
		 * ResultSet into a JSON file
		 */
		JsonWriter jsonwriter = new JsonWriter();
		
		/*
		 * call executeQuery() method of Query class to get the resultSet. Pass this
		 * resultSet as parameter to writeToJson() method of JsonWriter class to write
		 * the resultSet into a JSON file
		 */
		HashMap resultSet = new HashMap();
		resultSet  = queryObj.executeQuery(queryString);
		System.out.println("Writing status: ");
		System.out.println(jsonwriter.writeToJson(resultSet));
//		jsonwriter.writeToJson(resultSet);
		
	}
}
