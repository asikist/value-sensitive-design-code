package algorithm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ch.ethz.coss.algorithm.ProductRatingHypNorm;
import ch.ethz.coss.algorithm.ontology.Association;
import ch.ethz.coss.algorithm.ontology.AssociationMap;
import ch.ethz.coss.algorithm.ontology.Preference;
import ch.ethz.coss.algorithm.ontology.PreferenceTag;
import ch.ethz.coss.algorithm.ontology.Product;
import ch.ethz.coss.algorithm.ontology.ProductTag;
import ch.ethz.coss.algorithm.ontology.User;
import ch.ethz.coss.algorithm.utilities.Scores;
import junit.framework.TestCase;

/**Tests on pen & paper confirmed programmatically **/
public class TestProductRatingManual {
	AssociationMap map;
	Map<String, User> users;
	Map<Long, Product> products;
	
	//rounding tolerance
	static int testRoundingTolerance = 5;

	
	/**
	 * A simple setting. Contains 2 possible contradictions and tests correlation sums over 1
	 */
	public void prepareSetting1() {
		Scores.meanProductRating = 5.0;
		Scores.ratingScale = 5.0;
		//load the user
				User user = new User("Thomas");
				long idGenerator = 1;
				
				//Let's predefine some preferencetags here
				PreferenceTag w1 = new PreferenceTag(idGenerator++, "w1");
				PreferenceTag w2 = new PreferenceTag(idGenerator++, "w2");
				PreferenceTag w3 = new PreferenceTag(idGenerator++, "w3");
				PreferenceTag w4 = new PreferenceTag(idGenerator++, "w4");
				

				//load the preference questions from the questionnaires
				idGenerator = 1;
				Preference c1 = new Preference("c1.", idGenerator++);
				Preference c2 = new Preference("c2.", idGenerator++);
				Preference c3 = new Preference("c3.", idGenerator++);
				
				c1.addTags(w1, w2);
				c2.addTags(w3, w4);
				c3.addTags(w2, w4);
				
				//assign the user answer scores to each questionnaire
				user.addPreference(c1, 10.0); 
				user.addPreference(c2, 3.0); 
				user.addPreference(c3, 7.0);
				
				String preff = user.preferencesToString();
				System.out.println("my preferences " + preff);
				
				
				idGenerator = 1;
				ProductTag z1 = new ProductTag(idGenerator, "z"+idGenerator++);
				ProductTag z2 = new ProductTag(idGenerator, "z"+idGenerator++);
				ProductTag z3 = new ProductTag(idGenerator, "z"+idGenerator++);
				ProductTag z4 = new ProductTag(idGenerator, "z"+idGenerator++);
				ProductTag z5 = new ProductTag(idGenerator, "z"+idGenerator++);
				ProductTag z6 = new ProductTag(idGenerator, "z"+idGenerator++);
				ProductTag z7 = new ProductTag(idGenerator, "z"+idGenerator++);
				ProductTag z8 = new ProductTag(idGenerator, "z"+idGenerator++);
				
				ProductTag z10 = new ProductTag(10, "z10");
				ProductTag z11 = new ProductTag(11, "z11");

				ProductTag z65 = new ProductTag(65, "z65");

				
				//load the products to recommend from the database
				//Green Pistachios
				Product p1 = new Product("p1", 1);
				p1.addTags(z1, z2, z3); 
				//Eco Lamb
				Product p2 = new Product("p2", 2);
				p2.addTags(z3, z4, z5, z6);
				//Fair Yoghurt
				Product p3 = new Product("p3", 3);
				p3.addTags(z7, z8); 
				//Product with no info
				Product p4 = new Product("p4", 4); 
				p4.addTags(z2, z10, z11);
				
				Product p5 = new Product("p5", 5);
				p5.addTag(z65);
				
				//load the collection of all the products
				products = new HashMap<>();
				products.put(p1.getId(), p1);
				products.put(p2.getId(), p2);
				products.put(p3.getId(), p3);
				products.put(p4.getId(), p4);
				products.put(p5.getId(), p5);				

				
				//load and add the tag to tag correlations
				users = Collections.singletonMap("Thomas", user);
				
				//w1
				Association r1 = new Association(z7, w1, -1.0);
				Association r2 = new Association(z8, w1, 0.3);
				Association r3 = new Association(z1, w1, 0.4);
				Association r4 = new Association(z4, w1, 0.2);
				Association r5 = new Association(z5, w1, 0.3);
				Association r6 = new Association(z10, w1, -0.2);
				
				//w2
				Association r7 = new Association(z2, w2, -0.3);
				Association r8 = new Association(z3, w2, 0.3);
				Association r9 = new Association(z6, w2, -0.2);
				Association r10 = new Association(z11, w2, 0.3);				
				
				
				//w3
				Association r11 = new Association(z3, w3, -0.2);
				Association r12 = new Association(z5, w3, 0.3);
				Association r13 = new Association(z7, w3, 0.5);
				Association r14 = new Association(z10, w3, -0.6);
				
				//w4
				Association r15 = new Association(z1, w4, 1.0);
				Association r16 = new Association(z5, w4, 0.3);
				Association r17 = new Association(z6, w4, -0.2);
			


				map = new AssociationMap();
				map.addPair(r1);
				map.addPair(r2);
				map.addPair(r3);
				map.addPair(r4);
				map.addPair(r5);
				map.addPair(r6);
				map.addPair(r7);
				map.addPair(r8);
				map.addPair(r9);
				map.addPair(r10);				
				map.addPair(r11);
				map.addPair(r12);
				map.addPair(r13);
				map.addPair(r14);
				map.addPair(r15);
				map.addPair(r16);
				map.addPair(r17);
	
				
				System.out.println("User offset abs sum : " + user.getTotalAbsoluteOffset());
	}
	

	@Test
	public void rankingTest() {
		
		prepareSetting1();
		
		ProductRatingHypNorm prf = new ProductRatingHypNorm(products, map, users.get("Thomas"), products.get(1L));
		System.out.println(prf.getProductRating()); //5.69
		TestCase.assertTrue(almostEquals(prf.getProductRating(), 5.694444, testRoundingTolerance));
	
	
	
	ProductRatingHypNorm prf1 = new ProductRatingHypNorm(products, map, users.get("Thomas"), products.get(2L));
	System.out.println(prf1.getProductRating()); //5.94	
	
	TestCase.assertTrue(almostEquals(prf1.getProductRating(), 5.9490740741, testRoundingTolerance));
	System.out.println(prf1.getProductRating());
	
	ProductRatingHypNorm prf2 = new ProductRatingHypNorm(products, map, users.get("Thomas"), products.get(3L));
	System.out.println(prf2.getProductRating());//0
	
	TestCase.assertTrue(almostEquals(prf2.getProductRating(), 0, testRoundingTolerance));

	
	ProductRatingHypNorm prf3 = new ProductRatingHypNorm(products, map, users.get("Thomas"), products.get(4L));
	System.out.println(prf3.getProductRating()); //5.13
	
	TestCase.assertTrue(almostEquals(prf3.getProductRating(), 5.1388888, testRoundingTolerance));

	ProductRatingHypNorm prf5 = new ProductRatingHypNorm(products, map, users.get("Thomas"), products.get(5L));
	System.out.println(prf5.getProductRating()); //NaN
	TestCase.assertTrue(almostEquals(prf5.getProductRating(), Double.NaN, testRoundingTolerance));

	}
	
	public static boolean almostEquals(double first, double second, int toleranceDecimal) {
		if(Double.isNaN(first) & Double.isNaN(second))
			return true;
		if(Double.isInfinite(first) & Double.isFinite(second) && first*second > 0)
			return true;
		double diff = Math.abs(first - second);
		return diff < Math.pow(10.0, -toleranceDecimal);
		
	}

}
