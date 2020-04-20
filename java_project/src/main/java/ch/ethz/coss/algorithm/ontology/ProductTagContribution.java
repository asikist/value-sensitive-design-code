package ch.ethz.coss.algorithm.ontology;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.util.concurrent.AtomicDouble;

/**
 * A class that contains information about the total value contributed by a product tag to a product rating.
 * @author Thomas Asikis
 * @license Copyright (c) 2017-2020 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */
public class ProductTagContribution {
	long productTagId;
	double totalAbsOffset;
	double alpha;
	
	AtomicDouble totalContribution;
	
	Map<Preference, AtomicDouble> contributionToPreferences;
	
	
	
	/**
	 * Constructor
	 * @param productTagId the id of the product tag
	 * @param totalAbsOffset the total absolute offset of the preference
	 * @param alpha The scaling factor in the product rating calculation
	 */
	public ProductTagContribution(long productTagId, double totalAbsOffset, double alpha) {
		this.productTagId = productTagId;
		this.totalAbsOffset = totalAbsOffset;
		this.alpha = alpha;
		this.contributionToPreferences = new ConcurrentHashMap<>();
		this.totalContribution = new AtomicDouble();
	}

	/**
	 * 
	 * @param association the association score of the product tag and the preference tags
	 * @param aggregateAssociation The sum of association scores for the corresponding preference tags.
	 * @param offset the preference score offset for the user
	 * @param referenceAssociation the reference association score for normalization, it is min or max association
	 * @param preference, the preference that is relevant to the tag contribution 
	 * @param preferenceTagId the id of the preference tag that this association is calulated for
	 * @param contradiction, whether this tag contradicts a strict preference
	 */
	public void contributeAssociation(double association, 
			double aggregateAssociation, 
			double offset, 
			double referenceAssociation, 
			Preference preference, long preferenceTagId, boolean contradiction) {		
		if(association != 0 && offset != 0) {
			contributionToPreferences.putIfAbsent(preference, new AtomicDouble(0.0));
			double absSummedCorrelation = Math.abs(aggregateAssociation);
			double actualCorrelationContribution =  absSummedCorrelation > 1 ? association/absSummedCorrelation : association;
			actualCorrelationContribution = actualCorrelationContribution/referenceAssociation;
			actualCorrelationContribution = actualCorrelationContribution/preference.getTags().size();		
			actualCorrelationContribution = Math.signum(aggregateAssociation*offset)*actualCorrelationContribution;
			actualCorrelationContribution = actualCorrelationContribution*Math.abs(offset)/totalAbsOffset;
			actualCorrelationContribution = alpha*actualCorrelationContribution;
			
			actualCorrelationContribution = contradiction ? Double.NEGATIVE_INFINITY : actualCorrelationContribution;
			
			totalContribution.getAndAdd(actualCorrelationContribution);
			contributionToPreferences.get(preference).addAndGet(actualCorrelationContribution);			
		}
	}



	/*Getters and Setters*/
	public long getProductTagId() {
		return productTagId;
	}




	public double getTotalAbsOffset() {
		return totalAbsOffset;
	}




	public double getAlpha() {
		return alpha;
	}




	public AtomicDouble getTotalContribution() {
		return totalContribution;
	}




	public Map<Preference, AtomicDouble> getContributionToPreferences() {
		return contributionToPreferences;
	}
	
	
	
	
	
}
