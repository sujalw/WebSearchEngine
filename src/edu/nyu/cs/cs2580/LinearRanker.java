package edu.nyu.cs.cs2580;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

/**
 * 
 * @author sujal
 *
 */
public class LinearRanker{
	
	private Ranker _ranker;
	
	public LinearRanker(Ranker _ranker) {
		this._ranker= _ranker;
	}

	/**
	 * 
	 * @param query
	 * @param wCosine Weight of cosine score
	 * @param wLMP Weight of Language model probabilities (Jenilek) score 
	 * @param wPhrase Weight of phrase model score
	 * @param wNumViews Weight of numviews score
	 * @return
	 */
	public Vector<ScoredDocument> runquery(String query, float wCosine, float wLMP, float wPhrase, float wNumViews) {
		
		Vector<ScoredDocument> retrieval_results = new Vector<ScoredDocument>();
		double noOfDocs = _ranker.numDocs();
		for (int i = 0; i < noOfDocs; ++i) {
			retrieval_results.add(runquery(query, i, wCosine, wLMP, wPhrase, wNumViews));

		}
		
		//retrieval_results = Utilities.getSorted(retrieval_results);
		return retrieval_results;
	}

	public ScoredDocument runquery(String query, int did, float wCosine, float wLMP, float wPhrase, float wNumViews) {
		// Build query vector
		Scanner s = new Scanner(query);
		Vector<String> qv = new Vector<String>();
		while (s.hasNext()) {
			String term = s.next();
			qv.add(term);
		}

		// Get the document vector.
		Document d = _ranker.getDoc(did);
		double score = getLinearScore(query, did, wCosine, wLMP, wPhrase, wNumViews);
		return new ScoredDocument(did, d.get_title_string(), score);
	}

	public double getLinearScore(String query, int did, float wCosine, float wLMP, float wPhrase, float wNumViews) {
		double cosineScore = new CosineRanker(_ranker).getCosineScore(query, did);
		double phraseScore = new PhraseRanker(_ranker).getPhraseScore(query, did, 2);
		double lmpScore = 0; // jelinek
		double numViewsScore = 0;
		
		double score = wCosine*cosineScore + wLMP*lmpScore + wPhrase*phraseScore + wNumViews*numViewsScore;
		
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

		HashMap<String, Double> dv = Utilities.getTermFreq(docvec);
		System.out.print("Document vector : ");
		for (String k : dv.keySet()) {
			System.out.print(k + " : " + dv.get(k) + ", ");
		}
		System.out.println("");
		/*
		Vector<String> queryvec = new Vector<String>();
		queryvec.add("auto");
		queryvec.add("insurance");

		HashMap<String, Integer> qv = Utilities.getTermFreq(queryvec);
		System.out.print("Query vector : ");
		for (String k : qv.keySet()) {
			System.out.print(k + " : " + qv.get(k) + ", ");
		}
		System.out.println("");

		HashMap<String, Double> duv = Utilities.getUnitVector(dv);
		System.out.print("Document unit vector : ");
		for (String k : duv.keySet()) {
			System.out.print(k + " : " + duv.get(k) + ", ");
		}
		System.out.println("");

		HashMap<String, Double> quv = Utilities.getUnitVector(qv);
		System.out.print("Query unit vector : ");
		for (String k : quv.keySet()) {
			System.out.print(k + " : " + quv.get(k) + ", ");
		}
		System.out.println("");

		double dp = Utilities.getDotProduct(duv, quv);
		System.out.println("dp = " + dp);
		*/
		
		/*Vector<String> ngramvec = Utilities.getNGram(docvec, 3);
		for(int i=0 ; i<ngramvec.size() ; i++) {
			System.out.println(ngramvec.get(i));
		}*/
	}

}
