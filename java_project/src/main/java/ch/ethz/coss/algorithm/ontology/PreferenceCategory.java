package ch.ethz.coss.algorithm.ontology;

import java.io.Serializable;

/**
 * A class that contains information about preference categories or types.
 * @author Thomas Asikis
 * @license Copyright (c) 2017 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class PreferenceCategory implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String name;
	
	private String translation;
	
	/**
	 * Constructor
	 * @param id the unique id of the preference category
	 * @param name the English name/label of the category
	 * @param translation the localized translation
	 */
	public PreferenceCategory(long id, String name, String translation) {
		super();
		this.id = id;
		this.name = name;
		this.translation = translation;
	}
	
	/*Getters and Setters*/
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTranslation() {
		return translation;
	}
	
	
}
