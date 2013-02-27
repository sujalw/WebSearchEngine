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
		double noOfDocs = _ranker.numDocs();
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
		
		n = Math.min(n, qv.size());

		// get n-gram terms for query vector
		qv = Utilities.getNGram(qv, n);
		
		// Get the document vector.
		Document d = _ranker.getDoc(did);

		// get n-gram terms
		Vector<String> docVec = d.get_body_vector();
		docVec.addAll(d.get_title_vector());
		Vector<String> dv = Utilities.getNGram(docVec, n);
		// Vector<String> dv = Utilities.getNGram(d.get_title_vector(), n);

		// get n-gram term frequencies in the query
		HashMap<String, Double> termFreqQuery = Utilities.getTermFreq(qv);

		// get n-gram term frequencies in the document
		HashMap<String, Double> termFreqDoc = Utilities.getTermFreq(dv);

		// get unit vectors
		//HashMap<String, Double> termFreqQuery_UnitVec = Utilities
			//	.getUnitVector(termFreqQuery);
		//HashMap<String, Double> termFreqDoc_UnitVec = Utilities
			//	.getUnitVector(termFreqDoc);

		double score = Utilities.getDotProduct(termFreqQuery,
				termFreqDoc);

		return score;
	}

	public static void main(String[] args) {
		Vector<String> docvec = new Vector<String>();
		docvec.add("bing");
		docvec.add("web");
		docvec.add("search");
		docvec.add("live");
		docvec.add("search");

		docvec = Utilities.getNGram(docvec, 2);

		HashMap<String, Double> dv = Utilities.getTermFreq(docvec);
		System.out.print("Document vector : ");
		for (String k : dv.keySet()) {
			System.out.print(k + " : " + dv.get(k) + ", ");
		}
		System.out.println("");

		Vector<String> queryvec = new Vector<String>();
		queryvec.add("bing");
		//queryvec.add("insurance");

		HashMap<String, Double> qv = Utilities.getTermFreq(queryvec);
		System.out.print("Query vector : ");
		for (String k : qv.keySet()) {
			System.out.print(k + " : " + qv.get(k) + ", ");
		}
		System.out.println("");

		HashMap<String, Double> duv = Utilities.getNormalizedVector(dv, 2);
		System.out.print("Document unit vector : ");
		for (String k : duv.keySet()) {
			System.out.print(k + " : " + duv.get(k) + ", ");
		}
		System.out.println("");

		HashMap<String, Double> quv = Utilities.getNormalizedVector(qv, 2);
		System.out.print("Query unit vector : ");
		for (String k : quv.keySet()) {
			System.out.print(k + " : " + quv.get(k) + ", ");
		}
		System.out.println("");

		double dp = Utilities.getDotProduct(duv, quv);
		System.out.println("dp = " + dp);

		/*
		 * Vector<String> ngramvec = Utilities.getNGram(docvec, 3); for(int i=0
		 * ; i<ngramvec.size() ; i++) { System.out.println(ngramvec.get(i)); }
		 */
	}

}
