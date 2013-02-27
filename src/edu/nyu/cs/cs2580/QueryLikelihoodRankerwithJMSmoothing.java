/**
 * 
 */
package edu.nyu.cs.cs2580;

import java.util.Scanner;
import java.util.Vector;

/**
 * @author Amey
 * 
 */
public class QueryLikelihoodRankerwithJMSmoothing {

	private static double lambda = 0.0;
	private Ranker ranker;

	public QueryLikelihoodRankerwithJMSmoothing(Ranker ranker) {
		this.ranker = ranker;
	}

	public static void setLambda(double lambda) {
		QueryLikelihoodRankerwithJMSmoothing.lambda = lambda;
	}

	public Vector<ScoredDocument> runquery(String query) {
		Vector<ScoredDocument> retrieval_results = new Vector<ScoredDocument>();
		for (int i = 0; i < ranker.numDocs(); ++i) {
			retrieval_results.add(runquery(query, i));

		}
		return ranker.sortScoredDocumentAsPerScore(retrieval_results);
	}

	public ScoredDocument runquery(String query, int did) {

		// Build query vector
		Scanner s = new Scanner(query);
		Vector<String> qv = new Vector<String>();
		while (s.hasNext()) {
			String term = s.next();
			qv.add(term);
		}

		// Get the document vector. For hw1, you don't have to worry about the
		// details of how index works.
		Document d = ranker.getDoc(did);
		// Vector <String> content = d.get_body_vector();
		// Vector < String > dv = d.get_title_vector();

		double score = 0.0;
		for (int i = 0; i < qv.size(); ++i) {

			score += Math
					.log((1 - lambda)
							* (ranker.getQueryLikelihood(qv.get(i), did))
							+ (lambda)
							* (ranker.termFrequency(qv.get(i)) / ranker
									.termFrequency()));
		}

		// antilog
		score = Math.pow(Math.E, score);

		return new ScoredDocument(did, d.get_title_string(), score);
	}

}
