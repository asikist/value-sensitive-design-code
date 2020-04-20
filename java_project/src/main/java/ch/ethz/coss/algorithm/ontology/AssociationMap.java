package ch.ethz.coss.algorithm.ontology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import ch.ethz.coss.algorithm.utilities.Pair;
/**
 * A class that holds all association that map product against preference tags.
 * @author Thomas Asikis
 * @license Copyright (c) 2017-2020 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class AssociationMap implements Serializable{

	private static final long serialVersionUID = 1L;
	private ConcurrentMap<Pair<Long, Long>, Association> matches;

	/**
	 * Default constructor for an association map. Expected to be thread safe.
	 */
	public AssociationMap() {
		this.matches = new ConcurrentHashMap<>();
	}
	
	/**
	 * Add a mapping of a pair of product-preference tag.
	 * @param preferenceTag
	 * @param productTag
	 * @param score
	 */
	public void addMapping(Tag preferenceTag, Tag productTag, double score) {
		matches.put(new Pair<>(preferenceTag.getId(), productTag.getId()), new Association(productTag.getId(), preferenceTag.getId(), score));
	}

	/**
	 * Directly add a tag pair with an association score.
	 * @param tagPair
	 */
	public void addPair(Association tagPair) {
		matches.put(new Pair<>(tagPair.getPreferenceTagId(), tagPair.getProductTagId()), tagPair);
	}
	
	/**
	 * Returns the specified mapping.
	 * @param preferenceTagId
	 * @param productTagId
	 * @return
	 */
	public Association getMapping(long preferenceTagId, long productTagId) {
		return matches.get(new Pair<>(preferenceTagId, productTagId));
	}
	
	/**
	 * Whether this association map contains the specified product-preference pair.
	 * @param preferenceTagId
	 * @param productTagId
	 * @return
	 */
	public boolean contains(long preferenceTagId, long productTagId) {
		return matches.containsKey(new Pair<>(preferenceTagId, productTagId));
	}

	/**
	 * finds a list that contains all the tag pairs related to the specific
	 * preference tag.
	 * @param preferenceTag the preference tag id for the search
	 * @return a list with all matching associations
	 */
	public List<Association> find(long preferenceTagId) {
		Set<Entry<Pair<Long, Long>, Association>> entryset = matches.entrySet();
		List<Association> matchingTags = new ArrayList<>();
		for (Entry<Pair<Long, Long>, Association> entry : entryset) {
			Association tagMapping = entry.getValue();
			if (tagMapping.getPreferenceTagId() == preferenceTagId) {
				matchingTags.add(tagMapping);
			}
		}
		return matchingTags;
	}

	/**
	 * Finds all pairs for all the preference tags of the given preference
	 * matched against all the product tags of a given product.
	 * @param preference the preference id
	 * @param product the product id
	 * @return a list with all matching associations
	 */
	public List<Association> find(Preference preference) {
		Set<Entry<Pair<Long, Long>, Association>> entryset = matches.entrySet();
		List<Association> matchingTags = new ArrayList<>();
		for (Entry<Pair<Long, Long>, Association> entry : entryset) {
			Association tagMapping = entry.getValue();
			Set<Long> preferenceTagIds = preference.getTagIds();
			for (Long preferenceTagId : preferenceTagIds) {
				if (tagMapping.getPreferenceTagId() == preferenceTagId) {
					matchingTags.add(tagMapping);
				}
			}
		}
		return matchingTags;
	}

	/**
	 * Finds a list containing all the matching tags between a product and a preference.
	 * @param preference the preference id
	 * @param product the product id
	 * @return a list with all matching associations
	 */
	public List<Association> findAll(Preference preference, Product product) {		
		List<Association> matchingTags = new ArrayList<>();
		Set<Long> preferenceTagIds = preference.getTagIds();
		Set<Long> productTagIds = product.getTagIds();

			for (Long preferenceTagId : preferenceTagIds) {
				for (Long productTagId : productTagIds) {
					Association match = getMapping(preferenceTagId, productTagId);
					if(contains(preferenceTagId, productTagId)){
						matchingTags.add(match);
					}
				}		
		}
		return matchingTags;
	}
	
	/**
	 * Finds a list containing all the matching tags between a product and a preference tag.
	 * @param preferenceTagId the preference tag id
	 * @param product the product id
	 * @return a list with all matching associations
	 */
	public List<Association> matchAll(long preferenceTagId, Product product) {		
		List<Association> matchingTags = new ArrayList<>();
		Set<Long> productTagIds = product.getTagIds();

				for (Long productTagId : productTagIds) {
					Association match = getMapping(preferenceTagId, productTagId);
					if(contains(preferenceTagId, productTagId)){
						matchingTags.add(match);
					}	
		}
		return matchingTags;
	}
	
	/**
	 * Finds a list containing all the matching tags between a product tag and a preference
	 * @param preference the preference id
	 * @param productTag the product tag id
	 * @return
	 */
	public List<Association> findAll(Preference preference, ProductTag productTag) {		
		List<Association> matchingTags = new ArrayList<>();
		Set<Long> preferenceTagIds = preference.getTagIds();
		Set<Long> productTagIds = Collections.singleton(productTag.getId());

			for (Long preferenceTagId : preferenceTagIds) {
				for (Long productTagId : productTagIds) {
					Association match = getMapping(preferenceTagId, productTagId);
					if(contains(preferenceTagId, productTagId)){
						matchingTags.add(match);
					}
				}		
		}
		return matchingTags;
	}

	
	/*Getters and Setters*/
	/**
	 * Gets the association score between a product tag and a preference tag
	 * @param preferenceTag
	 * @param productTag
	 * @return
	 */
	public double getScore(long preferenceTag, long productTag) {
		return getMapping(preferenceTag, productTag).getAssociationValue();
	}
	
	/**
	 * Get all the tag-mappings (associations) in that map
	 * @return
	 */
	public Map<Pair<Long, Long>, Association> getAssociations() {
		return matches;
	}

	
	

}
