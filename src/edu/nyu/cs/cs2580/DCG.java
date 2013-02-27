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
import java.util.Vector;

/**
 * @author amey
 * 
 */
public class DCG {
	HashMap<String, HashMap<Integer, Double>> scored_judgments = new HashMap<String, HashMap<Integer, Double>>();
	HashMap<String, HashMap<Integer, Double>> relevance_judgments = new HashMap<String, HashMap<Integer, Double>>();
	Vector<Vector<String>> data = new Vector<Vector<String>>();

	public DCG(HashMap<String, HashMap<Integer, Double>> scored_judgments,
			Vector<Vector<String>> data,
			HashMap<String, HashMap<Integer, Double>> relevance_judgments) {

		this.scored_judgments = scored_judgments;
		this.relevance_judgments = relevance_judgments;
		this.data = data;
	}

	public static Map<Integer, Double> sortByComparator(
			HashMap<Integer, Double> unsortedMap) {

		List<Map.Entry<Integer, Double>> list = new LinkedList<Map.Entry<Integer, Double>>(
				unsortedMap.entrySet());

		// sort list based on comparator
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
			@Override
			public int compare(Map.Entry<Integer, Double> o1,
					Map.Entry<Integer, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<Integer, Double> result = new LinkedHashMap<Integer, Double>();
		for (Map.Entry<Integer, Double> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public double computeDCG(String query, int point) {
		double dcg = 1.0;
		HashMap<Integer, Double> qr = scored_judgments.get(query);

		// System.out.println("qr.size " + qr.size());
		// for (Map.Entry<Integer, Double> entry : qr.entrySet()){
		// System.out.println("Key = " + entry.getKey() + "," +
		// " Value = " + entry.getValue());
		// }

		if (point == 1) {
			// idealDcg = qrSorted.get(0);
			for (Vector<String> queryDid : data) {
				if (qr.get(Integer.parseInt(queryDid.get(1))) != null) {
					dcg = qr.get(Integer.parseInt(queryDid.get(1)));
				}
				break;
			}
		} else {
			for (Vector<String> queryDid : data) {
				if (qr.get(Integer.parseInt(queryDid.get(1))) != null) {
					dcg = qr.get(Integer.parseInt(queryDid.get(1)));
				}
				break;
			}

			int count = 0;
			for (Vector<String> queryDid : data) {
				count++;
				if (count == 1) {
					continue;
				} else if (count > point) {
					break;
				} else {
					if (qr.get(Integer.parseInt(queryDid.get(1))) != null) {
						dcg += (qr.get(Integer.parseInt(queryDid.get(1))) / Math
								.log(count)) / Math.log(2d);
					} else {
						dcg += (1.0d / Math.log(count)) / Math.log(2d);

					}
				}
			}
		}

		// System.out.println("dcg = " + dcg);
		return dcg;
	}

	public double computeIdealDCG(String query, int point) {
		double idealDcg = 0.0;
		HashMap<Integer, Double> qr = scored_judgments.get(query);
		Map<Integer, Double> qrSorted = sortByComparator(qr);
		// for (Map.Entry<Integer, Double> entry : qrSorted.entrySet()){
		// System.out.println("Key = " + entry.getKey() + "," +
		// " Value = " + entry.getValue());
		// }
		if (point == 1) {
			for (Map.Entry<Integer, Double> entry : qrSorted.entrySet()) {
				idealDcg = entry.getValue();
				break;
			}
		} else {
			for (Map.Entry<Integer, Double> entry : qrSorted.entrySet()) {
				idealDcg = entry.getValue();
				break;
			}
			int count = 0;

			for (Map.Entry<Integer, Double> entry : qrSorted.entrySet()) {
				count++;
				if (count == 1) {
					continue;
				} else if (count > point) {
					break;
				} else {
					idealDcg += (entry.getValue() / Math.log(count))
							/ Math.log(2d);
				}
			}
		}

		// System.out.println("idealDcg = " + idealDcg);
		return idealDcg;
	}

	public double computeNDCG(String query, int point) {
		return computeDCG(query, point) / computeIdealDCG(query, point);
	}

	public double computeReciprocalRank(String query) {
		double reciprocalRank = 0.0;
		HashMap<Integer, Double> qr = relevance_judgments.get(query);
		int count = 0;
		/*
		 * for (Map.Entry<Integer, Double> entry : qr.entrySet()){
		 * System.out.println("Key = " + entry.getKey() + "," + " Value = " +
		 * entry.getValue()); }
		 */
		for (Vector<String> queryDid : data) {
			count++;

			if (qr.get(Integer.parseInt(queryDid.get(1))) != null) {
				if (qr.get(Integer.parseInt(queryDid.get(1))) > 0.0) {
					reciprocalRank = 1.0 / (double) count;
					break;
				}
			}
		}
		return reciprocalRank;
	}

}
