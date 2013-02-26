package edu.nyu.cs.cs2580;

import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Vector;
import java.util.HashMap;
import java.util.Scanner;

class Evaluator {
	
	static String query;

  public static void main(String[] args) throws IOException {
    HashMap < String , HashMap < Integer , Double > > relevance_judgments =
      new HashMap < String , HashMap < Integer , Double > >();
    if (args.length < 1){
      System.out.println("need to provide relevance_judgments");
      return;
    }
    String p = args[0];
    // first read the relevance judgments into the HashMap
    readRelevanceJudgments(p,relevance_judgments);
    // now evaluate the results from stdin
    DCG dcg = new DCG(relevance_judgments);
    evaluateStdin(relevance_judgments);
    
    String evaluatorOutput = "";
    
    Vector<Double> recallVec = new Vector<Double>(); 
	Vector<Double> precisionVec = new Vector<Double>();
	
	//evaluatorOutput = getPrecision(1) + "\t" + getPrecision(5) + "\t" + getPrecision(10);
	//evaluatorOutput = "\t" + getRecall(1) + "\t" + getRecall(5) + "\t" + getRecall(10);
	//evaluatorOutput = "\t" + getF0.5(1) + "\t" + getF0.5(5) + "\t" + getF0.5(10);	
	
	double[] recallPoints = new double[]{0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
	for(int i=0 ; i<recallPoints.length ; i++) {
		evaluatorOutput += "\t" + Utilities.getPrecisionAtRecall(recallVec, precisionVec, recallPoints[i]);
	}
	
	//evaluatorOutput += "\t" + getAvgPrecision();
	
	//evaluatorOutput += "\t" + dcg.computeNDCG("web search",1) + "\t" + dcg.computeNDCG("web search",5) + "\t" + dcg.computeNDCG("web search",8);
	//evaluatorOutput += "\t" + dcg.computeReciprocalRank("web search");
	
	evaluatorOutput += "\t" + dcg.computeNDCG(query,1) + "\t" + dcg.computeNDCG(query,5) + "\t" + dcg.computeNDCG(query,8);
	evaluatorOutput += "\t" + dcg.computeReciprocalRank(query);
	
    //System.out.println("NDCG "+dcg.computeNDCG("web search",5));
    //System.out.println("RR "+ dcg.computeReciprocalRank("web search"));
	
	//System.out.println("Evaluator output = ");
	System.out.println("reciprocal rank for google = " + dcg.computeReciprocalRank("google"));
	System.out.println(query + ": " + evaluatorOutput);
  }

  public static void readRelevanceJudgments(
    String p,HashMap < String , HashMap < Integer , Double > > relevance_judgments){
	//System.out.println("hi");
    try {
      BufferedReader reader = new BufferedReader(new FileReader(p));
      try {
        String line = null;
        while ((line = reader.readLine()) != null){
          // parse the query,did,relevance line
          Scanner s = new Scanner(line).useDelimiter("\t");
          String query = s.next();
          int did = Integer.parseInt(s.next());
          String grade = s.next();
          double rel = 1.0; //un-judged document
          // convert to binary relevance
          if(grade.equals("Perfect")){
        	  rel = 5.0;
          }
          else if(grade.equals("Excellent")){
        	  rel = 4.0;
          }
          else if (grade.equals("Good")){ 
        	  rel = 3.0;
          }
          else if (grade.equals("Fair")){ 
        	  rel = 2.0;
          }
          else if (grade.equals("Bad")){ 
        	  rel = 1.0;  
          }
          if (relevance_judgments.containsKey(query) == false){
            HashMap < Integer , Double > qr = new HashMap < Integer , Double >();
            relevance_judgments.put(query,qr);
          }
          HashMap < Integer , Double > qr = relevance_judgments.get(query);
          qr.put(did,rel);
        }
      } finally {
        reader.close();
      }
    } catch (IOException ioe){
      System.err.println("Oops " + ioe.getMessage());
    }
  }
  
  
  public static void evaluateStdin(
    HashMap < String , HashMap < Integer , Double > > relevance_judgments){
    // only consider one query per call   
	//System.out.println("hello");
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      
      String line = null;
      double RR = 0.0;
      double N = 0.0;
      while ((line = reader.readLine()) != null){
        Scanner s = new Scanner(line).useDelimiter("\t");
        query = s.next();
        int did = Integer.parseInt(s.next());
      	String title = s.next();
      	double rel = Double.parseDouble(s.next());
      	if (relevance_judgments.containsKey(query) == false){
      	  throw new IOException("query not found");
      	}
      	HashMap < Integer , Double > qr = relevance_judgments.get(query);
      	if (qr.containsKey(did) != false){
      	  RR += qr.get(did);					
      	}
      	++N;
      }
      //System.out.println(Double.toString(RR/N));
    } catch (Exception e){
      System.err.println("Error:" + e.getMessage());
    }
  }
}
