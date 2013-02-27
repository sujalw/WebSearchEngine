package edu.nyu.cs.cs2580;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class NumViewsRanker {

	private static Ranker _ranker;

	public NumViewsRanker(Ranker ranker) {
		_ranker = ranker;
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

	public Vector<ScoredDocument> createNewViewsReverseSorted(String fileName,
			boolean append) {
		// System.out.println("Inside createNewViewsReverseSorted");
		HashMap<Integer, Integer> didView = createDiDViewMap();
		Utility u = new Utility();
		HashMap<Integer, Integer> sortedDidViewMap = u
				.sortByComparator(didView);

		Vector<ScoredDocument> vsd = new Vector<ScoredDocument>();

		Iterator<Map.Entry<Integer, Integer>> it = sortedDidViewMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Integer> pairs = (Map.Entry<Integer, Integer>) it
					.next();
			int did = pairs.getKey();
			String titleStr = _ranker.getTitleString(did);
			int score = pairs.getValue();

			vsd.add(new ScoredDocument(did, titleStr, score));
		}

		return vsd;
	}

}
