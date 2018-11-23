package com.stackroute.datamunger.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.stackroute.datamunger.query.DataSet;
import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Filter;
import com.stackroute.datamunger.query.Header;
import com.stackroute.datamunger.query.Row;
import com.stackroute.datamunger.query.RowDataTypeDefinitions;
import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.Restriction;

public class CsvQueryProcessor implements QueryProcessingEngine {
	/*
	 * This method will take QueryParameter object as a parameter which contains the
	 * parsed query and will process and populate the ResultSet
	 */
	
	DataSet resultSet = new DataSet();
	
	public DataSet getResultSet(QueryParameter queryParameter) {

		/*
		 * initialize BufferedReader to read from the file which is mentioned in
		 * QueryParameter. Consider Handling Exception related to file reading.
		 */
		BufferedReader line = null;
		try {
			line = new BufferedReader(new FileReader(queryParameter.getFileName()));
		} catch (FileNotFoundException e) {
			System.out.println("File not found - "+ e.getMessage());
		}

		/*
		 * read the first line which contains the header. Please note that the headers
		 * can contain spaces in between them. For eg: city, winner
		 */
		String headerLine = null;
		try {
			headerLine = line.readLine();
		} catch (IOException e) {
			System.out.println("Error reading header - "+e.getMessage());
		}
		String[] headerArray = headerLine.split(",");

		/*
		 * read the next line which contains the first row of data. We are reading this
		 * line so that we can determine the data types of all the fields. Please note
		 * that ipl.csv file contains null value in the last column. If you do not
		 * consider this while splitting, this might cause exceptions later
		 */
		String dataLine = null;
		try {
			dataLine = line.readLine();
		} catch (IOException e) {
			System.out.println("Error reading first data line - "+e.getMessage());
		}
		String[] firstDataLineArray = dataLine.split(",",-1);

		/*
		 * populate the header Map object from the header array. header map is having
		 * data type <String,Integer> to contain the header and it's index.
		 */
		HashMap<String, Integer> headerMap = new HashMap<String, Integer>();
		Header header = new Header();
		
		for(int i=0;i<headerArray.length;i++)
			header.put(headerArray[i], (i+1));
			
//		System.out.println("headerMap: "+headerMap);
//		header.setHeaderMap(headerMap);

		/*
		 * We have read the first line of text already and kept it in an array. Now, we
		 * can populate the RowDataTypeDefinition Map object. RowDataTypeDefinition map
		 * is having data type <Integer,String> to contain the index of the field and
		 * it's data type. To find the dataType by the field value, we will use
		 * getDataType() method of DataTypeDefinitions class
		 */
		HashMap<Integer, String> rowDataTypeMap = new HashMap<Integer, String>();
		RowDataTypeDefinitions rowDataTypeDefinition = new RowDataTypeDefinitions();
		String dataType=null;
		
		for(int i=0;i<firstDataLineArray.length;i++) {
			dataType = (DataTypeDefinitions.getDataType(firstDataLineArray[i]).toString());
			rowDataTypeMap.put((i+1), dataType);
		}
			
		rowDataTypeDefinition.setRowDataTypeDefinitions(rowDataTypeMap);

		/*
		 * once we have the header and dataTypeDefinitions maps populated, we can start
		 * reading from the first line. We will read one line at a time, then check
		 * whether the field values satisfy the conditions mentioned in the query,if
		 * yes, then we will add it to the resultSet. Otherwise, we will continue to
		 * read the next line. We will continue this till we have read till the last
		 * line of the CSV file.
		 */
		List<String> fields = queryParameter.getFields();
		List<Restriction> restrictions = queryParameter.getRestrictions();
		List<String> logicalOps = queryParameter.getLogicalOperators();
		
		DataSet dataSet = new DataSet();
		
		Long dataSetCount = (long) 1;
		
		String[] dataLineArray = firstDataLineArray;
		
		Filter filter = new Filter();
		
		Boolean restrictionResult = true;		
		
		while(dataLine != null) {
			dataLineArray = dataLine.split(",",-1);

			if( restrictions != null )
				restrictionResult = filter.evaluateExpression(restrictions, logicalOps, dataLineArray, header);
			if(restrictionResult) {
				Row rowObj = new Row();
				
				if(fields.get(0).equals("*")) {
					for(int i=0;i<headerArray.length;i++) {
						String colName = headerArray[i];
						String colValue = dataLineArray[i];
						
						rowObj.put(colName, colValue);
					}
				}
				else {
					for(int i=0;i<fields.size();i++) {
						String colName = fields.get(i);
						String colValue = dataLineArray[(header.get(colName))-1];
						rowObj.put(colName, colValue);
					}
				}
				
				dataSet.put(dataSetCount, rowObj);
				dataSetCount = dataSetCount + 1;
				try {
					dataLine = line.readLine();
				} catch (IOException e) {
					System.out.println("Error reading line - "+e.getMessage());
				}				
			}
			else {
			try {
				dataLine = line.readLine();			
			} catch (IOException e) {
				System.out.println("Error reading line - "+e.getMessage());
			}
			}
		}

		/* reset the buffered reader so that it can start reading from the first line */
		try {
			line.close();
		} catch (IOException e) {
			System.out.println("Error closing BufferedReader - "+e.getMessage());
		}

		/*
		 * skip the first line as it is already read earlier which contained the header
		 */

		/* read one line at a time from the CSV file till we have any lines left */

		/*
		 * once we have read one line, we will split it into a String Array. This array
		 * will continue all the fields of the row. Please note that fields might
		 * contain spaces in between. Also, few fields might be empty.
		 */

		/*
		 * if there are where condition(s) in the query, test the row fields against
		 * those conditions to check whether the selected row satifies the conditions
		 */

		/*
		 * from QueryParameter object, read one condition at a time and evaluate the
		 * same. For evaluating the conditions, we will use evaluateExpressions() method
		 * of Filter class. Please note that evaluation of expression will be done
		 * differently based on the data type of the field. In case the query is having
		 * multiple conditions, you need to evaluate the overall expression i.e. if we
		 * have OR operator between two conditions, then the row will be selected if any
		 * of the condition is satisfied. However, in case of AND operator, the row will
		 * be selected only if both of them are satisfied.
		 */

		/*
		 * check for multiple conditions in where clause for eg: where salary>20000 and
		 * city=Bangalore for eg: where salary>20000 or city=Bangalore and dept!=Sales
		 */

		/*
		 * if the overall condition expression evaluates to true, then we need to check
		 * if all columns are to be selected(select *) or few columns are to be
		 * selected(select col1,col2). In either of the cases, we will have to populate
		 * the row map object. Row Map object is having type <String,String> to contain
		 * field Index and field value for the selected fields. Once the row object is
		 * populated, add it to DataSet Map Object. DataSet Map object is having type
		 * <Long,Row> to hold the rowId (to be manually generated by incrementing a Long
		 * variable) and it's corresponding Row Object.
		 */
		resultSet = dataSet;
		/* return dataset object */
		return resultSet;
	}

}
