package ch.ethz.coss.algorithm.ontology;
/**
 * A class that contains information about a contradiction between a product tag and a strict preference.
 * @author Thomas Asikis
 * @license Copyright (c) 2017-2020 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class Contradiction {
	
	final Preference pf;
	final PreferenceTag pft;
	final ProductTag pdt;
	final Association corr;
	
	/**
	 * Constructor for a contradiction. Names are abbreviated to avoid shadowing other variables in calculation.
	 * @param pf the related preference
	 * @param pft the related preference tag
	 * @param pdt the related product tag
	 * @param corr the related correlation
	 */
	public Contradiction(Preference pf, PreferenceTag pft, ProductTag pdt, Association corr) {
		this.pf = pf;
		this.pft = pft;
		this.pdt = pdt;
		this.corr = corr;
	}

	/*Getters and Setters*/
	public Preference getPf() {
		return pf;
	}

	public PreferenceTag getPft() {
		return pft;
	}

	public ProductTag getPdt() {
		return pdt;
	}

	public Association getCorr() {
		return corr;
	}
	
	
}
