package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.ethz.coss.algorithm.ProductRating;
import ch.ethz.coss.algorithm.ProductRatingHypNorm;
import ch.ethz.coss.algorithm.Recommendation;
import ch.ethz.coss.algorithm.ontology.Association;
import ch.ethz.coss.algorithm.ontology.AssociationMap;
import ch.ethz.coss.algorithm.ontology.Preference;
import ch.ethz.coss.algorithm.ontology.PreferenceTag;
import ch.ethz.coss.algorithm.ontology.Product;
import ch.ethz.coss.algorithm.ontology.ProductTag;
import ch.ethz.coss.algorithm.ontology.User;
import ch.ethz.coss.algorithm.utilities.Scores;
import junit.framework.TestCase;

public class TestProductRatingRanking {
	AssociationMap map;
	Map<String, User> users;
	Map<Long, Product> products;
	
	@Before
	public void prepareSetting() {
		Scores.meanProductRating = 5.0;
		Scores.ratingScale = 5.0;
		//load the user
				User user = new User("Thomas");
				long idGenerator = 0;
				
				//Let's predefine some preference tags here
				PreferenceTag pfvegan = new PreferenceTag(idGenerator++, "vegan");
				PreferenceTag pffairtrade = new PreferenceTag(idGenerator++, "fair trade");
				PreferenceTag pffairtrade2 = new PreferenceTag(idGenerator++, "workerRights");
				PreferenceTag pfflowfat = new PreferenceTag(idGenerator++, "low fat");

				//load the preference questions from the questionnaires
				Preference preference1 = new Preference("I prefer to buy vegan products.", idGenerator++);
				preference1.addTags(pfvegan);
				Preference preference2 = new Preference("I would pay a premium for a fair trade labeled product.", idGenerator++);
				preference2.addTags(pffairtrade);
				preference2.addTags(pffairtrade2);
				Preference preference3 = new Preference("I follow a low fat diet", idGenerator++);
				preference3.addTags(pfflowfat);
				
				//assign the user answer scores to each questionnaire
				user.addPreference(preference1, 0); //non-vegan
				user.addPreference(preference2, 8.0); //pro fair trade
				user.addPreference(preference3, 2.0); //medium fat diet
				
				String preff = user.preferencesToString();
				System.out.println("my preferences " + preff);
				
				
				ProductTag pdvegan = new ProductTag(idGenerator++, "vegan");
				ProductTag pdfairtrade = new ProductTag(idGenerator++, "fair trade");
				ProductTag pdEco = new ProductTag(idGenerator++, "eco");
				ProductTag pdMeat = new ProductTag(idGenerator++, "meat");
				ProductTag pdHighFat = new ProductTag(idGenerator++, "high fat");
				//load the products to recommend from the database
				//Green Pistachios
				long pidGenerator = 0;
				Product product1 = new Product(pidGenerator+"", pidGenerator);
				product1.addTags(pdvegan, pdfairtrade, pdEco); //0, should be last from the ranked products
				//Eco Lamb
				Product product2 = new Product(pidGenerator+++"", pidGenerator);
				product2.addTags(pdMeat, pdfairtrade, pdHighFat); //1, most fitting, should be first
				//Fair Yoghurt
				Product product3 = new Product(pidGenerator+++"", pidGenerator);
				product3.addTags(pdfairtrade, pdHighFat); //2, more fitting, should be second
				//Product with no info
				Product product4 = new Product(pidGenerator+++"", pidGenerator); //unknown, this guy gets a NaN
				
				//load the collection of all the products
				products = new HashMap<>();
				products.put(product1.getId(), product1);
				products.put(product2.getId(), product2);
				products.put(product3.getId(), product3);
				products.put(product4.getId(), product4);
				
				//System.out.println("Existing products: " + products);

				
				//load and add the tag to tag associations
				users = Collections.singletonMap("Thomas", user);

				Association mapping1 = new Association(pdvegan, pfvegan, 1.0);
				Association mapping2 = new Association(pdMeat, pfvegan, -1.0);
				Association mapping3 = new Association(pdfairtrade, pffairtrade, 0.8);
				Association mapping4 = new Association(pdHighFat, pfflowfat, -0.8);

				map = new AssociationMap();
				map.addPair(mapping1);
				map.addPair(mapping2);
				map.addPair(mapping3);
				map.addPair(mapping4);		
				
				System.out.println("User offset abs sum : " + user.getTotalAbsoluteOffset());
	}
	
	//deterministic testing. changed to product rating
	@Test
	public void rankingTest() {
		
		
		//calculate a score for all the products
		List<Recommendation> recs = new ArrayList<>();
		System.out.println(products);
		
		for(Product product: products.values()) {
			for(User user :  users.values()) {
				ProductRating pr = new ProductRatingHypNorm(products, map, user, product);
				recs.add(pr.recommend());				
			}			
		}
		
		
		Collections.sort(recs, new Comparator<Recommendation>() {
			@Override
			public int compare(Recommendation o1, Recommendation o2) {
				return Double.compare(o2.getProductRating(), o1.getProductRating());
			}			
		});
		
		long[] expectedOrder = {3L, 1L, 2L, 0L};//Nans go first, then the order should be descending
		
		int i = 0;
		
		long[] resultingOrder = new long[expectedOrder.length];
		
		//artifact before junit tests
		System.out.println(recs);
		
		for(Recommendation rec :  recs){
			System.out.println(rec.getPid() + "  " + rec.getProductRating());
			
			resultingOrder[i] = rec.getPid();
		
			i++;
		}	
		
		System.out.println(Arrays.toString(resultingOrder));
		TestCase.assertTrue(Arrays.equals(resultingOrder, expectedOrder));
	}
	
}