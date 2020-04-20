package ch.ethz.coss.algorithm.ontology;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.ethz.coss.algorithm.utilities.Scores;
/**
 * A class that contains information of a preference statement.
 * @author Thomas Asikis
 * @license Copyright (c) 2017-2020 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class Preference implements Serializable{
	static double median = Scores.meanUserPreference;

	private static final long serialVersionUID = 1L;	
	private String name;
	private String translation;
	private Map<Long, PreferenceTag> tags;
	
	long id;
	long versionIn;
	long versionOut;
	long catId;
	
	
	/**
	 * a parameter. to be stored efficiently in a hashmap it uses the question
	 * as unique identifier and as hashcode generator. This constructor is agnostic of version
	 * so it just add a -1 to them.
	 * 
	 * @param name
	 */	
	public Preference(String name, long id) {
		this(name, null, id, -1, -1);
	}

	
	/**
	 * a parameter. to be stored efficiently in a hashmap it uses the question
	 * as unique identifier and as hashcode generator. This constructor is agnostic of version
	 * so it just add a -1 to them.
	 * 
	 * @param name
	 */	
	public Preference(String name, long id, String translation) {
		this(name, translation, id, -1, -1);
	}
	
	/**
	 * a parameter. to be stored efficiently in a hashmap it uses the question
	 * as unique identifier and as hashcode generator
	 * 
	 * @param name
	 */	
	public Preference(String name, String translation, long id, long versionIn, long versionOut) {
		this.name = name;
		this.tags = new HashMap<>();
		this.id = id;
		this.versionIn = versionIn;
		this.versionOut = versionOut;
		this.translation = translation;
	}

	/**
	 * add a new preference tag
	 * @param tag
	 */
	public void addTag(PreferenceTag tag) {
		if (tag instanceof PreferenceTag) {
			tags.put(tag.getId(), tag);
		} else {
			throw new IllegalStateException("Adding a non-preference tag to preference: " + id);
		}
	}

	
	/**
	 * Add an array of multiple tag objects to the current preference
	 * @param tags
	 */
	public void addTags(PreferenceTag ... tags) {
		for(PreferenceTag tag : tags){
			this.addTag(tag);
		}
	}
	
	/**
	 * Add a list of multiple tag objects to the current preference
	 * @param tags
	 */
	public void addTags(List<PreferenceTag> tags) {
		addTags(tags.toArray(new PreferenceTag[tags.size()]));
	}
	
	
	/*Getters, Setters, hashCode and toString implementations*/
	

	public String getName() {
		return name;
	}

	public Set<Tag> getTags() {
		return new HashSet<>(tags.values());
	}
	
	public Set<Long> getTagIds() {
		return new HashSet<>(tags.keySet());
	}
	

	@Override
	public int hashCode() {
		int prime = 17;
		int result = name.hashCode();
		result+= prime*result + Long.valueOf(id).hashCode();
		return result;
	}
	

	public static void setMedian(double median){
		Preference.median = median;
	}
	
	/**
	 * A custom toString method that prints the preference and all its tags to a specific 
	 * format.
	 */
	@Override
	public String toString() {
		StringBuilder strb = new StringBuilder();
		strb.append(name).append("\n");
		Iterator<PreferenceTag> it = tags.values().iterator();
		strb.append("tags: ");
		while (it.hasNext()) {
			String string = (String) it.next().toString();
			strb.append(string).append(" ");
		}
		return strb.toString();
	}


	public String getTranslation() {
		return translation;
	}


	public void setTranslation(String translation) {
		this.translation = translation;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setTags(Map<Long, PreferenceTag> tags) {
		this.tags = tags;
	}


	public long getCatId() {
		return catId;
	}


	public void setCatId(long catId) {
		this.catId = catId;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public long getId() {
		return id;
	}


	public static double getMedian() {
		return median;
	}


	public long getVersionIn() {
		return versionIn;
	}


	public long getVersionOut() {
		return versionOut;
	}
	
	
	
	
}
