package edu.nyu.cs.cs2580;

import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Vector;

class Ranker {
	private Index _index;

	public Ranker(String index_source) {
		_index = new Index(index_source);
	}

	public double documentFrequency(String s) {
		return (double) _index.documentFrequency(s);
	}

	public double termFrequency(String s) {
		return (double) _index.termFrequency(s);
	}

	public double termFrequency() {
		return (double) _index.termFrequency();
	}

	public double numDocs() {
		return (double) _index.numDocs();
	}

	public Document getDoc(int did) {
		return _index.getDoc(did);
	}

	/**
	 * @author Amey
	 * @return returns the fquid - no. of occurences of query term i in document
	 *         d
	 */
	public double getFrequid(String term, int documentId) {
		double freq = 0;
		Document d = getDoc(documentId);
		Vector<String> content = d.get_body_vector();
		content.addAll(d.get_title_vector());
		for (int i = 0; i < content.size(); i++) {
			if (term.equalsIgnoreCase(content.get(i))) {
				// System.out.println("hi "+ documentId);
				freq++;
			}
		}
		return freq;
	}

	/**
	 * @author Amey returns the |D| - total no. of words in document D
	 * @return
	 */
	public double getTotalNumberOfWordsInaDocument(int documentId) {
		double wordCount = 0;
		Document d = getDoc(documentId);
		Vector<String> content = d.get_body_vector();
		content.addAll(d.get_title_vector());
		wordCount = content.size();
		return wordCount;
	}

	/**
	 * @author Amey
	 * @param sds
	 * @return
	 */
	public Vector<ScoredDocument> sortScoredDocumentAsPerScore(
			Vector<ScoredDocument> sds) {
		if (sds.size() > 0) {
			Collections.sort(sds, new Comparator<ScoredDocument>() {
				@Override
				public int compare(final ScoredDocument obj1,
						final ScoredDocument obj2) {
					return Double.compare(obj2._score, obj1._score);
				}
			});
		}
		return sds;
	}

	/**
	 * @author Amey
	 * @param term
	 * @param did
	 * @return
	 */
	public double getQueryLikelihood(String term, int did) {
		return (getFrequid(term, did) / getTotalNumberOfWordsInaDocument(did));
	}

	public Vector<ScoredDocument> runquery(String query) {
		Vector<ScoredDocument> retrieval_results = new Vector<ScoredDocument>();
		for (int i = 0; i < numDocs(); ++i) {
			retrieval_results.add(runquery(query, i));

		}
		return retrieval_results;
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
		Document d = _index.getDoc(did);
		Vector<String> dv = d.get_title_vector();

		// Score the document. Here we have provided a very simple ranking
		// model,
		// where a document is scored 1.0 if it gets hit by at least one query
		// term.
		double score = 0.0;
		for (int i = 0; i < dv.size(); ++i) {
			for (int j = 0; j < qv.size(); ++j) {
				if (dv.get(i).equals(qv.get(j))) {
					score = 1.0;
					break;
				}
			}
		}

		return new ScoredDocument(did, d.get_title_string(), score);
	}

	/* Added by Ravi */
	/**
	 * This function calculates the number of times a given term occurs in a
	 * document
	 * 
	 * @param did
	 * @param term
	 * @return
	 */
	public int termFreqInDoc(int did, String term) {
		Document d = _index.getDoc(did);
		Vector<String> dBody = d.get_body_vector();
		dBody.addAll(d.get_title_vector());
		int termCount = 0;
		for (int i = 0; i < dBody.size(); i++) {
			if (dBody.get(i).equalsIgnoreCase(term)) {
				termCount++;
			}
		}
		return termCount;
	}

	/**
	 * This function returns the total number of words in a document
	 * 
	 * @param did
	 * @return
	 */
	public int wordCountInDoc(int did) {
		Document d = _index.getDoc(did);
		Vector<String> dBody = d.get_body_vector();
		dBody.addAll(d.get_title_vector());
		return dBody.size();
	}

	/**
	 * Returns total number of documents
	 * 
	 * @return
	 */
	public int documentCount() {
		return _index.numDocs();
	}

	public int getNumViews(int did) {
		return _index.getNumViews(did);
	}

	public String getTitleString(int did) {
		return _index.getTitleString(did);
	}

	public String getBodyString(int did) {
		return _index.getBodyString(did);
	}
	/* Added by Ravi */
}