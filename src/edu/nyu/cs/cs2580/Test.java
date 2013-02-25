package edu.nyu.cs.cs2580;

import java.util.Date;
import java.util.Vector;

public class Test {
	public static void main(String[] args) {
		
		Vector<Double> recallVec = new Vector<Double>();
		recallVec.add(0.2d);
		recallVec.add(0.2d);
		recallVec.add(0.4d);
		recallVec.add(0.4d);
		recallVec.add(0.4d);
		recallVec.add(0.6d);
		recallVec.add(0.6d);
		recallVec.add(0.6d);
		recallVec.add(0.8d);
		recallVec.add(1.0d);
		
		Vector<Double> precisionVec = new Vector<Double>();
		precisionVec.add(1d);
		precisionVec.add(0.5d);
		precisionVec.add(0.67d);
		precisionVec.add(0.5d);
		precisionVec.add(0.4d);
		precisionVec.add(0.5d);
		precisionVec.add(0.43d);
		precisionVec.add(0.38d);
		precisionVec.add(0.44d);
		precisionVec.add(0.5d);
		
		/*Vector<Double> recallVec = new Vector<Double>();
		recallVec.add(0.0d);
		recallVec.add(0.33d);
		recallVec.add(0.33d);
		recallVec.add(0.33d);
		recallVec.add(0.67d);
		recallVec.add(0.67d);
		recallVec.add(1.0d);
		recallVec.add(1.0d);
		recallVec.add(1.0d);
		recallVec.add(1.0d);
		
		Vector<Double> precisionVec = new Vector<Double>();
		precisionVec.add(0d);
		precisionVec.add(0.5d);
		precisionVec.add(0.33d);
		precisionVec.add(0.25d);
		precisionVec.add(0.4d);
		precisionVec.add(0.33d);
		precisionVec.add(0.43d);
		precisionVec.add(0.38d);
		precisionVec.add(0.33d);
		precisionVec.add(0.3d);*/
		
		double patr = Utilities.getPrecisionAtRecall(recallVec, precisionVec, 0.2);
		System.out.println("\nPrecision at recall = " + patr);
		
		System.out.println("Date = " + new Date());
	}
}
