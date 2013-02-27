package edu.nyu.cs.cs2580;

// @CS2580: this class should not be changed.
class ScoredDocument {
	public int _did;
	public String _title;
	public double _score;

	ScoredDocument(int did, String title, double score) {
		_did = did;
		_title = title;
		_score = score;
	}

	public int get_did() {
		return _did;
	}

	public void set_did(int _did) {
		this._did = _did;
	}

	public String get_title() {
		return _title;
	}

	public void set_title(String _title) {
		this._title = _title;
	}

	public double get_score() {
		return _score;
	}

	public void set_score(double _score) {
		this._score = _score;
	}

	String asString() {
		return new String(Integer.toString(_did) + "\t" + _title + "\t"
				+ Double.toString(_score));
	}
}
