package edu.nyu.cs.cs2580;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

/**
 * 
 * @author sujal
 * 
 */
public class CosineRanker {

	private Ranker _ranker;

	public CosineRanker(Ranker _ranker) {
		this._ranker = _ranker;
	}

	public Vector<ScoredDocument> runquery(String query) {
		Vector<ScoredDocument> retrieval_results = new Vector<ScoredDocument>();
		double noOfDocs = _ranker.numDocs();
		for (int i = 0; i < noOfDocs; ++i) {
			retrieval_results.add(runquery(query, i));
		}

		// retrieval_results = Utilities.getSorted(retrieval_results);
		return retrieval_results;
	}

	public ScoredDocument runquery(String query, int did) {
		double score = getCosineScore(query, did);
		Document d = _ranker.getDoc(did);
		return new ScoredDocument(did, d.get_title_string(), score);
	}

	public double getCosineScore(String query, int did) {
		
		// Build query vector
		
		Scanner s = new Scanner(query);
		Vector<String> qv = new Vector<String>();
		while (s.hasNext()) {			
			String term = s.next();
			qv.add(term);
		}
		
		// Get the document vector.
		Document d = _ranker.getDoc(did);

		// Vector<String> dv = d.get_title_vector();
		// Vector<String> dv = d.get_body_vector();
		
		// get unigram terms
		Vector<String> dv = Utilities.getNGram(d.get_body_vector(), 1);
		dv.addAll(d.get_title_vector());
		
		// get term frequencies in the document		
		HashMap<String, Double> termFreqDoc = Utilities.getTermFreq(dv);
		HashMap<String, Double> tfDoc = Utilities.getNormalizedVector(termFreqDoc, 1d);
		NormalizedTFIDF normTfIdf = new NormalizedTFIDF(_ranker);
		HashMap<String, Double> idfDoc = normTfIdf.invDocFreqVector(tfDoc);
		HashMap<String, Double> tfIdfDoc = Utilities.getTfIdf(tfDoc, idfDoc);
		HashMap<String, Double> tfIdfDocNormalised = Utilities.getNormalizedVector(tfIdfDoc, 2);
		
		//System.out.println("proc doc");
		
		HashMap<String, Double> termFreqQuery = Utilities.getTermFreq(qv);
		HashMap<String, Double> tfQuery = Utilities.getNormalizedVector(termFreqQuery, 1d);
		HashMap<String, Double> idfQuery = normTfIdf.invDocFreqVector(tfQuery);
		HashMap<String, Double> tfIdfQuery = Utilities.getTfIdf(tfQuery, idfQuery);
		HashMap<String, Double> tfIdfQueryNormalised = Utilities.getNormalizedVector(tfIdfQuery, 2);
		
		double score = Utilities.getDotProduct(tfIdfDocNormalised, tfIdfQueryNormalised);

		return score;
	}

	public static void main(String[] args) {
		Vector<String> docvec = new Vector<String>();
		docvec.add("car");
		docvec.add("insurance");
		docvec.add("auto");
		docvec.add("insurance");

		/*
		 * HashMap<String, Integer> dv = Utilities.getTermFreq(docvec);
		 * System.out.print("Document vector : "); for (String k : dv.keySet())
		 * { System.out.print(k + " : " + dv.get(k) + ", "); }
		 * System.out.println("");
		 * 
		 * Vector<String> queryvec = new Vector<String>(); queryvec.add("auto");
		 * queryvec.add("insurance");
		 * 
		 * HashMap<String, Integer> qv = Utilities.getTermFreq(queryvec);
		 * System.out.print("Query vector : "); for (String k : qv.keySet()) {
		 * System.out.print(k + " : " + qv.get(k) + ", "); }
		 * System.out.println("");
		 * 
		 * HashMap<String, Double> duv = Utilities.getUnitVector(dv);
		 * System.out.print("Document unit vector : "); for (String k :
		 * duv.keySet()) { System.out.print(k + " : " + duv.get(k) + ", "); }
		 * System.out.println("");
		 * 
		 * HashMap<String, Double> quv = Utilities.getUnitVector(qv);
		 * System.out.print("Query unit vector : "); for (String k :
		 * quv.keySet()) { System.out.print(k + " : " + quv.get(k) + ", "); }
		 * System.out.println("");
		 * 
		 * double dp = Utilities.getDotProduct(duv, quv);
		 * System.out.println("dp = " + dp);
		 */

		Vector<String> ngramvec = Utilities.getNGram(docvec, 3);
		for (int i = 0; i < ngramvec.size(); i++) {
			System.out.println(ngramvec.get(i));
		}
	}

}
