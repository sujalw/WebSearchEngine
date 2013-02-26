/**
 * 
 */
package edu.nyu.cs.cs2580;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author amey
 *
 */
public class DCG {
	HashMap < String , HashMap < Integer , Double > > relevance_judgments =
		      new HashMap < String , HashMap < Integer , Double > >();
	
	public DCG(HashMap < String , HashMap < Integer , Double > > relevance_judgments){
		//System.out.println("hi");
		this.relevance_judgments = relevance_judgments;
	}
	
	public static Map<Integer,Double> sortByComparator
		(HashMap<Integer,Double> unsortedMap) {
 
		List<Map.Entry<Integer,Double>> list = new 
				LinkedList<Map.Entry<Integer,Double>>(unsortedMap.entrySet());

		// sort list based on comparator
		Collections.sort(list, 
				new Comparator<Map.Entry<Integer,Double>>() {
			@Override
			public int compare(Map.Entry<Integer, Double> o1,
					Map.Entry<Integer, Double> o2) {
				return (o2.getValue()).compareTo( o1.getValue() );
			}
		});

		Map<Integer,Double> result = new LinkedHashMap<Integer,Double>();
		for (Map.Entry<Integer,Double> entry : list){
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}
	
	public double computeDCG(String query,int point){
		//System.out.println("hello");
		double dcg=0.0;
		HashMap<Integer,Double> qr = relevance_judgments.get(query);
//		for (Map.Entry<Integer, Double> entry : qr.entrySet()){ 
//			System.out.println("Key = " + entry.getKey() + "," +
//					" Value = " + entry.getValue()); 
//		}
		/*if(point==1){
			dcg = qr.get(0);
		}
		else{
			dcg = qr.get(0);
			for(int i=1;i<point;i++){
				dcg+=qr.get(i) / Math.log(i+1);
			}
		}*/
		if(point==1){
			//idealDcg = qrSorted.get(0);
			for (Map.Entry<Integer, Double> entry : qr.entrySet()){ 
				dcg=entry.getValue(); 
				break;
			}
		}
		else{
			for (Map.Entry<Integer, Double> entry : qr.entrySet()){ 
				dcg=entry.getValue(); 
				break;
			}
			int count=0;
			while(count<=point){
				for (Map.Entry<Integer, Double> entry : qr.entrySet()){ 
					count++;
					if(count==1){
						continue;
					}
					else{
						dcg+=entry.getValue() / Math.log(count);
					} 
				}
			}
		}
		return dcg;
	}
	
	
	
	public double computeIdealDCG(String query,int point){
		double idealDcg=0.0;
		HashMap<Integer,Double> qr = relevance_judgments.get(query);
		Map<Integer,Double> qrSorted = sortByComparator(qr);
		/*for (Map.Entry<Integer, Double> entry : qrSorted.entrySet()){ 
			System.out.println("Key = " + entry.getKey() + "," +
					" Value = " + entry.getValue()); 
		}*/
		if(point==1){
			//idealDcg = qrSorted.get(0);
			for (Map.Entry<Integer, Double> entry : qrSorted.entrySet()){ 
				idealDcg=entry.getValue(); 
				break;
			}
		}
		else{
			for (Map.Entry<Integer, Double> entry : qrSorted.entrySet()){ 
				idealDcg=entry.getValue(); 
				break;
			}
			int count=0;
			while(count<=point){
				for (Map.Entry<Integer, Double> entry : qrSorted.entrySet()){ 
					count++;
					if(count==1){
						continue;
					}
					else{
						idealDcg+=entry.getValue() / Math.log(count);
					} 
				}
			}
		}
		return idealDcg;
	}
	
	public double computeNDCG(String query,int point){
		return computeDCG(query,point) / computeIdealDCG(query,point);
	}
	
	public double computeReciprocalRank(String query){
		double reciprocalRank=0.0;
		HashMap<Integer,Double> qr = relevance_judgments.get(query);
		int count=0;
		for (Map.Entry<Integer, Double> entry : qr.entrySet()){ 
			System.out.println("Key = " + entry.getKey() + "," +
					" Value = " + entry.getValue()); 
		}
		for (Map.Entry<Integer, Double> entry : qr.entrySet()){ 
			count++;
			if(entry.getValue() > 2.0){
				System.out.println("count = " + count);
				reciprocalRank = 1.0 / (double) count;
				break;
			} 
		}
		return reciprocalRank;
	}

}
