package ch.ethz.coss.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.util.concurrent.AtomicDouble;

import ch.ethz.coss.algorithm.ontology.Association;
import ch.ethz.coss.algorithm.ontology.AssociationMap;
import ch.ethz.coss.algorithm.ontology.Preference;
import ch.ethz.coss.algorithm.ontology.PreferenceTag;
import ch.ethz.coss.algorithm.ontology.Product;
import ch.ethz.coss.algorithm.ontology.ProductTagContribution;
import ch.ethz.coss.algorithm.ontology.Tag;
import ch.ethz.coss.algorithm.ontology.User;
import ch.ethz.coss.algorithm.utilities.Pair;
import ch.ethz.coss.algorithm.utilities.Scores;


/**
 * The product rating calculation, based on the class used in the ASSET field test.
 * @author Thomas Asikis
 * @license Copyright (c) 2017-2020 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class ProductRatingHypNorm extends ProductRating{

	protected Map<Preference, AtomicDouble> preferenceContributions;
	protected Map<Long, ProductTagContribution> productTagContributions;

	protected double productRating;
	boolean noProductTagInformation;
	boolean contradiction;
	final double summedOffset;
	
	/**
	 * Constructor for the rating calculation. Calculation takes place in the constructor.
	 * @param products the products map that contains product ids and objects. Used for reference association calcuations.
	 * @param tagAssociations the tag associations map, that contains all association scores and tag pairs related to the calcuation.
	 * @param user the user object
	 * @param product the product object
	 */
	public ProductRatingHypNorm(Map<Long, Product> products, AssociationMap tagAssociations, User user, Product product) {

		super(products, tagAssociations, user, product);
		this.summedOffset = user.getTotalAbsoluteOffset();

		// initializations
		this.preferenceContributions = new ConcurrentHashMap<>();
		this.productTagContributions = new ConcurrentHashMap<>();
		this.noProductTagInformation = true;
		this.contradiction = false;
		
		double rawProductRating = rawProductRating();
		// scale the product rating as shown in equation 23 of the Appendix
		this.productRating = Scores.meanProductRating + (Scores.ratingScale * rawProductRating);
		//handles strict preference case
		this.productRating = Double.isInfinite(this.productRating) && productRating < 0 ? 0 : this.productRating;
		

	}

	/* Product Rating Calculation */

	/**
	 * Retrieve the product rating for the given user and create a recommendation object.
	 * @param user
	 * @param product
	 * @return the product rating value.
	 */
	public Recommendation recommend() {
		double score = productRating();
		return new Recommendation(user.getUid(), product.getId(), score);
	}

	/**
	 * A getter for the product rating calculated.
	 * @return the product rating
	 */
	public double productRating() {
		return this.productRating;
	}

	/**
	 * Calculate the product rating between a product and a user before applying the
	 * scale transformation. The paper equations based on the version of 20 April 2020
	 * are referenced via comments throghout the code. Please look in the appendix. 
	 * @return the non-scaled rating.
	 */
	protected double rawProductRating() {
		double rawProductRating = 0.0;
		double summedAverageAssociation = 0.0; //
		
		if(summedOffset == 0.0) {
			System.out.println("summedOffset");
			return Double.NaN;
		}

		
		for (Entry<Preference, Double> preferenceAndScore : user.getPreferences().entrySet()) {
			Preference preference = preferenceAndScore.getKey();
			double preferenceScore = preferenceAndScore.getValue();
			double preferenceOffset = preferenceScore - Scores.meanUserPreference;
			double summedNormalizedAssociation = 0.0;
			double totalPreferenceTags = preference.getTags().size();
			
			for(Tag tag : preference.getTags()) {
				PreferenceTag preferenceTag = (PreferenceTag) tag;
				//the aggregate association, showcased in equation 15 of Appendix.
				double summedAssociation = 0.0; 
				List<Association> preferenceTagAssociations = tagAssociations.find(preferenceTag.getId());
				List<Association> productAssociations = filterOnProduct(this.product, preferenceTagAssociations);
				
				
				noProductTagInformation = noProductTagInformation && (productAssociations.size() == 0);
				
				for(Association association : productAssociations) {
					//the aggregate association, showcased in equation 15 of Appendix.
					summedAssociation += association.getAssociationValue();
					//contradiction activates only in the case offset and association are on extreme values and have opposite signs.
					//used for strict preferences
					boolean isContradiction = preferenceOffset*association.getAssociationValue() == Scores.contradictionAssociationOffset;
		
					contradiction |= isContradiction;;
				}
				
				//clipping for aggregate association needed as shown in equation 26 of the Appendix
				double aggregatedAssociation = normalize(summedAssociation);
				
				//calculation of reference association as shown in equation 19 of the Appendix
				double maxAssociation = 0.0;
				double minAssociation = 0.0;
				
				for(Association association : preferenceTagAssociations) {
					double associationValue = association.getAssociationValue();
					if (associationValue > 0) {
						maxAssociation += associationValue;
					} else {
						minAssociation += associationValue;
					}			
				}
				
				//clipping for reference association as shown in equation 17,18 of the Appendix
				//absolute is used in accordance to equation 19 in Appendix
				maxAssociation = Math.abs(normalize(maxAssociation));
				minAssociation = Math.abs(normalize(minAssociation));
				
				
				//clipping for reference association as shown in equation 19 of the Appendix
				double normalizedAssociation = 0;
				
				if(aggregatedAssociation != 0) {
					normalizedAssociation = aggregatedAssociation > 0 ? aggregatedAssociation/maxAssociation : aggregatedAssociation/minAssociation;
				}
				
				//preparing nominator for mean association over preference tags in accordance to
				//equation 20 in Appendix.
				summedNormalizedAssociation += normalizedAssociation;		
				
				
				//for contributions
				for(Association association : productAssociations) {
					productTagContributions.putIfAbsent(association.getProductTagid(), new ProductTagContribution(product.getId(), summedOffset, Scores.ratingScale));
					//contradiction activates only in the case offset and association are on extreme values and have opposite signs.
					boolean isContradiction = preferenceOffset*association.getAssociationValue() == Scores.contradictionAssociationOffset;
					productTagContributions.get(association.getProductTagId()).contributeAssociation(association.getAssociationValue(), summedAssociation, preferenceOffset, 
							summedAssociation > 0 ? maxAssociation : minAssociation, preference, association.getPreferenceTagId(), isContradiction);
				}
				
				
			}
			//calculation of equation 20 in the Appendix, also referred to as sustainability index of 
			//product for preference
			double averageAssociation = summedNormalizedAssociation/totalPreferenceTags;
			//This is done because preferences without preference tags or contraditions are assigned non finite values.
			double preferenceContribution =  Double.isFinite(averageAssociation) ? averageAssociation*preferenceOffset : 0;

			
			preferenceContributions.putIfAbsent(preference, new AtomicDouble());
			preferenceContributions.get(preference).addAndGet(Scores.ratingScale*preferenceContribution/summedOffset);
			summedAverageAssociation +=  preferenceContribution;
		}
		
		if(noProductTagInformation) {
			rawProductRating = Double.NaN;
		} else if(contradiction) {
			rawProductRating = Double.NEGATIVE_INFINITY;			
		} else {
			
			//calculation of non-scaled product rating, in the logic of equations 21 and 22 in the appendix.  
			rawProductRating = summedAverageAssociation/summedOffset;
	
		
		}
		return rawProductRating;
	}
	
	
	
	/**
	 * For all the associations of a preference tag, return the ones that are mapped to the product tags of the 
	 * given product.
	 * @param product the product object
	 * @param preferenceTagAssociations the list of relevant associations to the preference.
	 * @return
	 */
	public static List<Association> filterOnProduct(Product product, List<Association> preferenceTagAssociations){
		List<Association> filtered = new ArrayList<>();

		
		for (Association preferenceTagAssociation : preferenceTagAssociations) {
			if(product.getTagIds().contains(preferenceTagAssociation.getProductTagid())) {
				filtered.add(preferenceTagAssociation);				
			}
		}
		return filtered;
	}
	
	/**
	 * The clipping function for aggregate association scores.
	 * @param associationScore the association score or the value to be clipped.
	 * @return
	 */
	protected static double normalize(double associationScore) {
		return associationScore < -Scores.maxAllowedAssociation ? -Scores.maxAllowedAssociation
				: (associationScore > Scores.maxAllowedAssociation ? Scores.maxAllowedAssociation : associationScore);
	}

	/* Product tag contributions */

	/**
	 * Product tag contributions sorted in descending order based on the highest
	 * absolute value.
	 * 
	 * @return A list of product tag ids and rating contributions. If the list is
	 *         empty or all values are zero's, this means that there is not enough
	 *         information to determine a product rating. In these cases the product
	 *         rating should be zero.
	 */
	@Override
	public List<Pair<Long, Double>> getProductTagContribution() {
		List<Pair<Long, Double>> result = new ArrayList<>();
		for(Entry<Long, ProductTagContribution> e : productTagContributions.entrySet()) {
			result.add(new Pair<Long, Double>(e.getKey(), e.getValue().getTotalContribution().doubleValue()));
		}
		Collections.sort(result, absDescComparator);
		return result;
	}
	
	/**
	 *@return the list containing the percentage of contributions for each product tag to the final
	 *rating value
	 */
	@Override
	public List<Pair<Long, Double>> getProductTagContributionPercentage() {
		List<Pair<Long, Double>> result = getProductTagContribution();
		double absSum = 0;
		for(Pair<Long, Double> pair : result) {
			absSum += Math.abs(pair.getSecond());
		}
		for(Pair<Long, Double> pair : result) {
			pair.setSecond(pair.getSecond()/absSum);
		}
		Collections.sort(result, absDescComparator);
		return result;
	}

	/**
	 * Product tag contributions sorted in descending order based on the highest
	 * absolute value. Preferences are unsorted but a sorting can be created based
	 * on the preference contribution which occurs from the usage of
	 * {@link #preferenceContributions}.
	 * 
	 * @return A map of Preferences to product tag ids and rating contribution for
	 *         each product tag to each preference If each list is empty or all
	 *         values are zero's, this means that there is not enough information to
	 *         determine a product rating. In these cases the product rating should
	 *         be zero.
	 */
	public Map<Preference, List<Pair<Long, Double>>> getProductTagContributionPerPreference() {
	Map<Preference, List<Pair<Long, Double>>> sortedProductTagContributions = new ConcurrentHashMap<>();		
		
		for(Entry<Long, ProductTagContribution> e : productTagContributions.entrySet()) {
			Map<Preference, AtomicDouble> toPreferenceContributions = e.getValue().getContributionToPreferences();
			for(Entry<Preference, AtomicDouble> e2 : toPreferenceContributions.entrySet()) {
				sortedProductTagContributions.putIfAbsent(e2.getKey(), new ArrayList<>());
				sortedProductTagContributions.get(e2.getKey()).add(new Pair<Long, Double>(e.getKey(), e2.getValue().doubleValue()));
			}
		}

		for(Entry<Preference, List<Pair<Long, Double>>> e : sortedProductTagContributions.entrySet()) {
			Collections.sort(e.getValue(), absDescComparator);
		}

		return sortedProductTagContributions;
	}

	/** The product contribution per preference on the corresponding sustainability index
	 * @return the List of the contributions per product for each sustainability index.
	 */
	public Map<Preference, List<Pair<Long, Double>>> getProductTagContributionPerPreferencePercentages() {
		Map<Preference, List<Pair<Long, Double>>> sortedProductTagContributions = new ConcurrentHashMap<>();
		
		
		for(Entry<Long, ProductTagContribution> e : productTagContributions.entrySet()) {
			Map<Preference, AtomicDouble> toPreferenceContributions = e.getValue().getContributionToPreferences();
			for(Entry<Preference, AtomicDouble> e2 : toPreferenceContributions.entrySet()) {
				double percentage = e2.getValue().doubleValue()/Math.abs(this.preferenceContributions.get(e2.getKey()).doubleValue());
				sortedProductTagContributions.putIfAbsent(e2.getKey(), new ArrayList<>());
				sortedProductTagContributions.get(e2.getKey()).add(new Pair<Long, Double>(e.getKey(), percentage));
			}
		}

		for(Entry<Preference, List<Pair<Long, Double>>> e : sortedProductTagContributions.entrySet()) {
			Collections.sort(e.getValue(), absDescComparator);
		}

		return sortedProductTagContributions;
	}
	/* Preference Contribution to Rating */

	/** Calculates the preference contributions to the product rating in absolute value.
	 * Non thread-safe first call. Operates on List, and probably will throw a
	 * ConcurrentModification exception or give illogical results. Ensure the call
	 * to this method is always in sync and don't perform non-thread sage operations when using it.
	 * Higher absolute value contributions will be first no matter if positive or negative.
	 * 
	 * @return The sorted by absolute contribution list list of preference
	 *         contributions to the product rating. The sum of the contributions for
	 *         all the preferences should be equal to the value of the product
	 *         rating acquired from the {@link #productRating(Product)} method. If
	 *         the list is empty or all values are zero's, this means that there is
	 *         not enough information to determine a product rating. In these cases
	 *         the product rating should be zero.
	 */
	public List<Pair<Preference, Double>> getPreferenceContributions() {
		List<Pair<Preference, Double>> result = new ArrayList<>();
		for( Entry<Preference, AtomicDouble> e : preferenceContributions.entrySet()) {
			result.add(new Pair<Preference, Double>(e.getKey(), e.getValue().doubleValue()));
		}
		Collections.sort(result, absDescComparator);
		return result;
	}
	
	/** Calculates the preference contribution percentages.
	 * @return the preference contributions in percentages in a lust of preference, score pairs.
	 */
	public List<Pair<Preference, Double>> getPreferenceContributionsPercentages() {
		List<Pair<Preference, Double>> result = getPreferenceContributions();
		double sum = 0.0;
		for( Pair<Preference, Double> e : result) {
			sum+=Math.abs(e.getSecond());
		}
		
		for( Pair<Preference, Double> e : result) {
			e.setSecond(e.getSecond()/sum);
		}
		Collections.sort(result, absDescComparator);
		return result;
		
	}
	

	/**
	 * The score contribution of a preference to this rating. The score for all the
	 * preferences should add to the total score. all preferences include even
	 * preferences that the user has not shifted
	 * 
	 * @param preference the preference object
	 * @return
	 */
	protected double getPreferenceContribution(Preference preference) {
		double rawContribution = getRawPreferenceContribution(preference);
		return Scores.ratingScale * rawContribution;
	}

	/*Getters and Setters*/
	protected double getRawPreferenceContribution(Preference preference) {
		// this becomes NaN if the the user has set no preferences
		return this.preferenceContributions.get(preference).doubleValue();
	}

	public Map<Long, ProductTagContribution> getProductTagContributions() {
		return productTagContributions;
	}

	public double getProductRating() {
		return productRating;
	}

	public boolean isNoProductTagInformation() {
		return noProductTagInformation;
	}

	public double getSummedOffset() {
		return summedOffset;
	}

	public boolean isContradiction() {
		return contradiction;
	}
	
	


}
