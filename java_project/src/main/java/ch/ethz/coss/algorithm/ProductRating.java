package ch.ethz.coss.algorithm;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.ethz.coss.algorithm.ontology.AssociationMap;
import ch.ethz.coss.algorithm.ontology.Preference;
import ch.ethz.coss.algorithm.ontology.Product;
import ch.ethz.coss.algorithm.ontology.User;
import ch.ethz.coss.algorithm.utilities.Pair;
import ch.ethz.coss.algorithm.utilities.Scores;


/**
 * The default product rating abstract class. This class is extended to implement different product rating calculation logics.
 * @author Thomas Asikis
 * @license Copyright (c) 2017-2020 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public abstract class ProductRating {

	final protected Map<Long, Product> products;
	final protected AssociationMap tagAssociations;
	final protected User user;
	final protected Product product;

	protected static Comparator<Pair<?, Double>> absDescComparator = new Comparator<Pair<?, Double>>() {
		@Override
		public int compare(Pair<?, Double> o1, Pair<?, Double> o2) {
			return Double.compare(Math.abs(o2.getSecond()), Math.abs(o1.getSecond()));
		}
	};



	protected double productRating;
	
	public ProductRating(Map<Long, Product> products, AssociationMap tagAssociations, User user, Product product) {
		this.products = products;
		this.tagAssociations = tagAssociations;
		this.user = user;
		this.product = product;		

	}

	/* Product Rating Calculation */

	/**
	 * Calculate the score of a product for the given user
	 * 
	 * @param user
	 * @param product
	 * @return
	 */
	public Recommendation recommend() {
		double score = productRating();
		return new Recommendation(user.getUid(), product.getId(), score);
	}
	
	/**
	 * Product rating calculation, the result of which is shown to the user.
	 * @return the product rating value
	 */
	public double productRating() {
		return this.productRating;
	}
	
	/**
	 * Intermediate product rating calculation. Useful when someone needs to store rating values
	 * before rescaling for analysis purposes.
	 * @return
	 */
	protected abstract double rawProductRating();


	/**
	 * This function normalizes aggregate association scores, when they exceed the proposed theoretical values.
	 * This is used when the underlying ontology has expert bias that allows for overlaps between tag concepts.
	 * @param associationScore the aggregate association score or any value that requiresa a clipping score.
	 * @return the clipped value.
	 */
	protected static double clip(double associationScore) {
		return associationScore < -Scores.maxAllowedAssociation ? -Scores.maxAllowedAssociation
				: (associationScore > Scores.maxAllowedAssociation ? Scores.maxAllowedAssociation : associationScore);
	}


	/* Getters */

	public Map<Long, Product> getProducts() {
		return products;
	}

	public User getUser() {
		return user;
	}

	public Product getProduct(){
		return this.product;
	}
	
	public AssociationMap getTagAssociations() {
		return tagAssociations;
	}
	
	/* Product tag contribution */

	/**
	 * Product tag contributions sorted in descending order based on the highest
	 * absolute value.
	 * 
	 * @return A list of product tag ids and rating contributions. If the list is
	 *         empty or all values are zero's, this means that there is not enough
	 *         information to determine a product rating. In these cases the product
	 *         rating should be zero.
	 */
	public abstract List<Pair<Long, Double>> getProductTagContribution();
	
	public abstract List<Pair<Long, Double>> getProductTagContributionPercentage();


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
	public abstract Map<Preference, List<Pair<Long, Double>>> getProductTagContributionPerPreference();
	
	public abstract Map<Preference, List<Pair<Long, Double>>> getProductTagContributionPerPreferencePercentages();
	/* Preference Contribution to Rating */

	/**
	 * Non thread-safe first call. Operates on List, and probably will throw a
	 * ConcurrentModification exception or give illogical resutls. Ensure the call
	 * to this method is always synced. Bigger contributions will be first no matter
	 * if positive or negative.
	 * 
	 * @return The sorted by absolute contribution list list of preference
	 *         contributions to the product rating. The sum of the contributions for
	 *         all the preferences should be equal to the value of the product
	 *         rating acquired from the {@link #productRating(Product)} method. If
	 *         the list is empty or all values are zero's, this means that there is
	 *         not enough information to determine a product rating. In these cases
	 *         the product rating should be zero.
	 */
	public abstract List<Pair<Preference, Double>> getPreferenceContributions();
	
	public abstract List<Pair<Preference, Double>> getPreferenceContributionsPercentages();

	/**
	 * The score contribution of a preference to this rating. The score for all the
	 * preferences should add to the total score. all preferences include even
	 * preferences that the user has not shifted
	 * 
	 * @param preference
	 * @return
	 */
	protected abstract double getPreferenceContribution(Preference preference);

	protected abstract double getRawPreferenceContribution(Preference preference);
}
