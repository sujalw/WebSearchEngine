package edu.nyu.cs.cs2580;

import java.util.HashMap;
import java.util.Vector;

public class Utilities {
	public static HashMap<String, Integer> getTermFreq(Vector<String> dv) {
		HashMap<String, Integer> termFreq = new HashMap<String, Integer>();
		
		for(String term : dv) {
			Integer freq = termFreq.get(term);
			if(freq == null) {
				termFreq.put(term, 1);
			} else {
				termFreq.put(term, freq+1);
			}
		}			
		
		return termFreq;
	}

	public static float getDotProduct(HashMap<String, Integer> vec1, HashMap<String, Integer> vec2) {
		HashMap<String, Integer> v1, v2;
		
		// v1 holds the smaller vector and v2 holds the larger one
		if(vec1.keySet().size() < vec2.keySet().size()) {
			v1 = vec1;
			v2 = vec2;
		} else {
			v1 = vec2;
			v2 = vec1;
		}
		
		float score = 0;
		for(String k : v1.keySet()) {
			
		}
		
		return 0;
	}
}
