package edu.nyu.cs.cs2580;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

public class CosineRanker extends Ranker{
	
	public CosineRanker(String index_source) {
		super(index_source);
	}
	
	public ScoredDocument runquery(String query, int did){
		// Build query vector
	    Scanner s = new Scanner(query);
	    Vector < String > qv = new Vector < String > ();
	    while (s.hasNext()){
	      String term = s.next();
	      qv.add(term);
	    }

	    // Get the document vector. For hw1, you don't have to worry about the
	    // details of how index works.
	    Document d = _index.getDoc(did);
	    Vector < String > dv = d.get_body_vector();//d.get_title_vector();
	    
	    // get term frequencies in the document
	    HashMap<String, Integer> termFreqQuery = Utilities.getTermFreq(qv);
	    HashMap<String, Integer> termFreqDoc = Utilities.getTermFreq(dv);

	    float score1 = Utilities.getDotProduct(termFreqQuery, termFreqDoc);

	    // Score the document. Here we have provided a very simple ranking model,
	    // where a document is scored 1.0 if it gets hit by at least one query term.
	    double score = 0.0;
	    for (int i = 0; i < dv.size(); ++i){
	      for (int j = 0; j < qv.size(); ++j){
	        if (dv.get(i).equals(qv.get(j))){
	          score = 1.0;
	          break;
	        }
	      }
	    }

	    return new ScoredDocument(did, d.get_title_string(), score);
	}
	
	public static void main(String []args) {
		Vector <String> docvec = new Vector<String>();
		docvec.add("car");
		docvec.add("insurance");
		docvec.add("auto");
		docvec.add("insurance");
		
		HashMap<String, Integer> tf = Utilities.getTermFreq(docvec);
		for(String k : tf.keySet()) {
			System.out.println(k + " : " + tf.get(k));
		}
	}

}
