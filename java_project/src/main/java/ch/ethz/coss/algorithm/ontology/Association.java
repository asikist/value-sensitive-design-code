package ch.ethz.coss.algorithm.ontology;

import java.io.Serializable;

/**
 * A class that holds association scores and associations between a preference tag and a product tag.
 * @author Thomas Asikis
 * @license Copyright (c) 2017-2020 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class Association implements Serializable{

	private static final long serialVersionUID = 1L;
	private final long productTagId;
	private final long preferenceTagId;
	private double associationValue;
	
	/**
	 * ID Constructor
	 * @param productTag the product tag id
	 * @param preferenceTag the preference tag id
	 * @param associationScore the association score between the preference and product tag
	 */
	public Association(long productTagId, long preferenceTagId, double associationScore) {
		super();
		this.productTagId = productTagId;
		this.preferenceTagId = preferenceTagId;
		this.associationValue = associationScore;
	}
	
	/**
	 * Object constructor
	 * @param productTag the product tag object
	 * @param preferenceTag the preference tag object
	 * @param associationScore the association score between a preference tag and a product tag
	 */
	public Association(ProductTag productTag, PreferenceTag preferenceTag, double associationScore) {
		this(productTag.getId(), preferenceTag.getId(), associationScore);
	}

	/**
	 * Object constructor for utility
	 * @param preferenceTag the preference tag object
	 * @param productTag the product tag constructor
	 * @param associationScore
	 */
	public Association(PreferenceTag preferenceTag, ProductTag productTag, double associationScore) {
		this(productTag.getId(), preferenceTag.getId(), associationScore);
	}
	
	/*Getters and Setter*/
	public Long getProductTagid() {
		return productTagId;
	}

	public Long getPreferenceTagid() {
		return preferenceTagId;
	}

	public double getAssociationValue() {
		return associationValue;
	}

	public long getProductTagId() {
		return productTagId;
	}

	public long getPreferenceTagId() {
		return preferenceTagId;
	}	
	
	
	
	
}
