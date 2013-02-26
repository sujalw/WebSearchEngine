package edu.nyu.cs.cs2580;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NumViewsRanker {

	private static Ranker _ranker;

	public NumViewsRanker(Ranker ranker) {
		_ranker = ranker;
		// createDiDViewMap();
	}

	public static HashMap<Integer, Integer> createDiDViewMap() {
		System.out.println("Inside createDiDViewMap");
		int numberofDocs = _ranker.documentCount();
		HashMap<Integer, Integer> didView = new HashMap<Integer, Integer>();
		for (int i = 0; i < numberofDocs; i++) {
			didView.put(i, _ranker.getNumViews(i));
		}
		return didView;
	}

	public String createNewViewsReverseSorted(String fileName, boolean append) {
		// System.out.println("Inside createNewViewsReverseSorted");
		HashMap<Integer, Integer> didView = createDiDViewMap();
		Utility u = new Utility();
		HashMap<Integer, Integer> sortedDidViewMap = u
				.sortByComparator(didView);

		String contents = "";
		// FileWriter fstream = new FileWriter(fileName, append);
		// BufferedWriter out = new BufferedWriter(fstream);

		Iterator<Map.Entry<Integer, Integer>> it = sortedDidViewMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Integer> pairs = (Map.Entry<Integer, Integer>) it
					.next();
			int did = pairs.getKey();
			String titleStr = _ranker.getTitleString(did);
			int score = pairs.getValue();

			contents += did + "\t" + titleStr + "\t" + score;
			contents += "\n";

		}

		// for(int i = sortedDidViewMap.size() - 1; i >= 0; i--) {
		// String bodyStr = _ranker.getBodyString(i);

		// out.newLine();
		// }
		// out.write(contents);
		// out.close();

		return contents;
	}

}
