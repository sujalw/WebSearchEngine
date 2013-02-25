package edu.nyu.cs.cs2580;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

/**
 * 
 * @author sujal
 * 
 */
public class PhraseRanker {

	private Ranker _ranker;

	public PhraseRanker(Ranker _ranker) {
		this._ranker = _ranker;
	}

	/**
	 * 
	 * @param n
	 *            length of phrase
	 * @param query
	 * @return
	 */
	public Vector<ScoredDocument> runquery(String query, int n) {

		if (n <= 0) {
			return null;
		}

		Vector<ScoredDocument> retrieval_results = new Vector<ScoredDocument>();
		int noOfDocs = _ranker.numDocs();
		for (int i = 0; i < noOfDocs; ++i) {
			retrieval_results.add(runquery(query, i, n));

		}

		// retrieval_results = Utilities.getSorted(retrieval_results);
		return retrieval_results;
	}

	public ScoredDocument runquery(String query, int did, int n) {

		double score = getPhraseScore(query, did, n);
		Document d = _ranker.getDoc(did);
		return new ScoredDocument(did, d.get_title_string(), score);
	}

	public double getPhraseScore(String query, int did, int n) {
		// Build query vector
		Scanner s = new Scanner(query);
		Vector<String> qv = new Vector<String>();
		while (s.hasNext()) {
			String term = s.next();
			qv.add(term);
		}
		
		// get n-gram terms for query vector
		qv = Utilities.getNGram(qv, n);

		// Get the document vector. 
		Document d = _ranker.getDoc(did);

		// get n-gram terms
		Vector<String> dv = Utilities.getNGram(d.get_body_vector(), n); 
		//Vector<String> dv = Utilities.getNGram(d.get_title_vector(), n);

		// get n-gram term frequencies in the query
		HashMap<String, Integer> termFreqQuery = Utilities.getTermFreq(qv);

		// get n-gram term frequencies in the document
		HashMap<String, Integer> termFreqDoc = Utilities.getTermFreq(dv);

		// get unit vectors
		HashMap<String, Double> termFreqQuery_UnitVec = Utilities
				.getUnitVector(termFreqQuery);
		HashMap<String, Double> termFreqDoc_UnitVec = Utilities
				.getUnitVector(termFreqDoc);

		double score = Utilities.getDotProduct(termFreqQuery_UnitVec,
				termFreqDoc_UnitVec);
		
		return score;
	}

	public static void main(String[] args) {
		Vector<String> docvec = new Vector<String>();
		docvec.add("car");
		docvec.add("insurance");
		docvec.add("auto");
		docvec.add("car");
		docvec.add("insurance");

		docvec = Utilities.getNGram(docvec, 2);

		HashMap<String, Integer> dv = Utilities.getTermFreq(docvec);
		System.out.print("Document vector : ");
		for (String k : dv.keySet()) {
			System.out.print(k + " : " + dv.get(k) + ", ");
		}
		System.out.println("");
		/*
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

		/*
		 * Vector<String> ngramvec = Utilities.getNGram(docvec, 3); for(int i=0
		 * ; i<ngramvec.size() ; i++) { System.out.println(ngramvec.get(i)); }
		 */
	}

}
