package ch.ethz.coss.algorithm.ontology;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ch.ethz.coss.algorithm.utilities.Pair;
import ch.ethz.coss.algorithm.utilities.Scores;

/**
 * A class that contains information about users, e.g their preference scores.
 * @author Thomas Asikis
 * @license Copyright (c) 2017-2020 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String uid;
	Map<Preference, Double> preferences;
	Map<Preference, Double> offsets;
	Map<Preference, Double> absoluteOffsets;
	Map<Long, Double> history;

	/**
	 * create a new user with a unique uid
	 * @param uid
	 */
	public User(String uid) {
		this.uid = uid;
		this.preferences = new LinkedHashMap<>();
		this.offsets = new LinkedHashMap<>();
		this.absoluteOffsets = new LinkedHashMap<>();
		this.history = new HashMap<>();
	}

	/**
	 * add a single preference to the user with a corresponding score. Collision of preferences
	 * is solved via replacing
	 * @param preference
	 * @param score
	 */
	public void addPreference(Preference preference, double score) {
		preferences.put(preference, score);
		double offset =  score-Scores.meanUserPreference;
		offsets.put(preference, offset);
		double absOffset = Math.abs(offset);
		absoluteOffsets.put(preference, absOffset);
	}	

	/**
	 * add a purchased product to user history. If product already existis, the new quantity is added to the
	 * old one. a get and add operation.
	 * @param product
	 * @param quantity
	 */
	public void addToHistory(Product product, double quantity) {
		this.history.putIfAbsent(product.getId(), 0.0);
		this.history.put(product.getId(), history.get(product.getId()) + quantity);
	}
	

	/** 
	 * add a mew list of preferences tot the user. preference collision is solved by replace
	 * @param preferences
	 */
	public void addPreferences(List<Pair<Preference, Double>> preferences) {
		for(Pair<Preference, Double> preferenceAndScore : preferences){
			addPreference(preferenceAndScore.getFirst(), preferenceAndScore.getSecond());
		}
	}

	public String getUid() {
		return uid;
	}

	/**
	 * get all user preferences and theis respective scores
	 * @return
	 */
	public Map<Preference, Double> getPreferences() {
		return preferences;
	}
	
	

	public Map<Preference, Double> getOffsets() {
		return offsets;
	}

	public Map<Preference, Double> getAbsoluteOffsets() {
		return absoluteOffsets;
	}

	public Double getTotalAbsoluteOffset() {
		double sum = 0.0;
		for(Entry<Preference, Double> e : this.offsets.entrySet()) {
			sum+=Math.abs(e.getValue());
			//System.err.println(e.getValue());
		}
		//System.err.println(sum);
		return sum;
	}

	/**
	 * String representation of a preference and the correspofing user score on that preference
	 * @return
	 */
	public String preferencesToString() {
		StringBuilder strb = new StringBuilder("User: " + uid).append("\n");
		
		for(Map.Entry<Preference, Double> preferenceAndScore : preferences.entrySet()){
			String description = preferenceAndScore.getValue() > Scores.meanUserPreference ? "pro" : preferenceAndScore.getValue() < Scores.meanUserPreference ? "against" : "neutral";
			strb.append(preferenceAndScore.getKey().getName() + "\t::\t" + preferenceAndScore.getValue() +"\t" + description).append("\n");
		} 
			
		return strb.append("-----\n").toString();
	}
	
}
