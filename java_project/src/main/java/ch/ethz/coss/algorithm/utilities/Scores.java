package ch.ethz.coss.algorithm.utilities;
/**
 * Basic configuration for Score constants. E.g. values to control rating range etc.
 * @author Thomas Asikis
 * @license Copyright (c) 2017-2020 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */
public class Scores {
	/**
	 * The maximum positive similarity score between two tags.
	 * Examples:
	 *  vegan, meat, score -1
	 *  vegan, vegetables, 1 
	 *  where 1 is the maxTagSimilarity
	 */
	public static double maxAllowedAssociation = 1.0;
	public static double maxAbsTagSimilarity = Math.abs(maxAllowedAssociation);

	/**
	 * The mean (or median) score that a user provides to a preference statement
	 * e.g. in a scale of 0-10, the mean should be 5.
	 */
	public static double meanUserPreference = 5.0; //this should remain positive
	public static double meanAbsUserPreference = Math.abs(meanUserPreference); //this should remain positive
	public static double maxUserPreference = 2*meanUserPreference; 

	
	public static double contradictionAssociationOffset = -Scores.meanAbsUserPreference*Scores.maxAbsTagSimilarity;
	/**
	 * The mean (or median) rating that a user expects from a product.
	 * If a user expects a rating between 0 and 10, then 5 is the expected value.
	 */
	public static double meanProductRating = 5.0; //What is the "middle" value of the rating scale
	
	public static double ratingScale = 5.0; //how lower of higher the rating can be from the mean rating
	//e.g. [meanProductRating-rating scale, meanProductRating + rating scale]
	

}
