package edu.nyu.cs.cs2580;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

public class Output {
	Vector<ScoredDocument> _outputVector;
	Map<String, String> _query_map;
	String _clickLoggingData;

	public enum action {
		RENDER, CLICK
	};

	public Output(final Vector<ScoredDocument> outputVector,
			final Map<String, String> query_map) {
		_outputVector = outputVector;
		_query_map = query_map;
		_clickLoggingData = "";
	}

	/**
	 * @author sujal
	 * @param sessionId
	 * @return
	 */
	public String generateTextOutput(String sessionId) {
		String queryResponse = "";

		Properties prop = new Properties();
		
		try {
			// load a properties file
			prop.load(this.getClass().getResourceAsStream("config.properties"));
			String resultsDir = prop.getProperty("results_dir");
			String click_logging_results = prop.getProperty("click_logging_results");
			
			Iterator<ScoredDocument> itr = _outputVector.iterator();
			while (itr.hasNext()) {
				ScoredDocument sd = itr.next();
				if (queryResponse.length() > 0) {
					queryResponse = queryResponse + "\n";
				}
				queryResponse = queryResponse + _query_map.get("query") + "\t"
						+ sd.asString();

				_clickLoggingData = _clickLoggingData + sessionId + "\t"
						+ _query_map.get("query") + "\t" + sd._did + "\t"
						+ action.RENDER + "\t" + new Date() + "\n";
			}
			
			// write _clickLogging data to the file						
			Utilities.writeToFile(resultsDir + click_logging_results, _clickLoggingData, true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return queryResponse;
		// return _clickLogging;
	}
	
	public String generateHtmlOutput(String sessionId) {
		String queryResponse = "";

		Properties prop = new Properties();
		
		try {
			// load a properties file
			prop.load(this.getClass().getResourceAsStream("config.properties"));
			String resultsDir = prop.getProperty("results_dir");
			String click_logging_results = prop.getProperty("click_logging_results");
			
			Iterator<ScoredDocument> itr = _outputVector.iterator();
			while (itr.hasNext()) {
				ScoredDocument sd = itr.next();
				if (queryResponse.length() > 0) {
					queryResponse = queryResponse + "\n";
				}
				queryResponse = queryResponse + _query_map.get("query") + "\t"
						+ sd.asString();

				_clickLoggingData = _clickLoggingData + sessionId + "\t"
						+ _query_map.get("query") + "\t" + sd._did + "\t"
						+ action.RENDER + "\t" + new Date() + "\n";
			}
			
			// write _clickLogging data to the file						
			//Utilities.writeToFile(resultsDir + click_logging_results, _clickLoggingData, true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return queryResponse;
		// return _clickLogging;
	}
}
