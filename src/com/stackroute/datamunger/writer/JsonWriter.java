package com.stackroute.datamunger.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonWriter {
	/*
	 * This method will write the resultSet object into a JSON file. On successful
	 * writing, the method will return true, else will return false
	 */
	@SuppressWarnings("rawtypes")
	public boolean writeToJson(Map resultSet) {

		/*
		 * Gson is a third party library to convert Java object to JSON. We will use
		 * Gson to convert resultSet object to JSON
		 */
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		@SuppressWarnings("unused")
		String result = gson.toJson(resultSet);

		/*
		 * write JSON string to data/result.json file. As we are performing File IO,
		 * consider handling exception
		 */
		Boolean writeStatus = null;
		File file = new File("data/result.json");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Error creating result.json - "+e.getMessage());
			}
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
		} catch (IOException e1) {
			System.out.println("Error creating BufferedWriter - "+e1.getMessage());
		}
		try {			
			bw.write(result);
			writeStatus = true;
		} catch (IOException e) {
			writeStatus = false;
			System.out.println("Error writing to result.json - "+e.getMessage());
		}

		/* return true if file writing is successful */

		/* return false if file writing is failed */

		/* close BufferedWriter object */
		try {
			bw.close();
		} catch (IOException e) {
			System.out.println("Error clsing BufferedReader - "+e.getMessage());
		}

		return writeStatus;
	}

}
