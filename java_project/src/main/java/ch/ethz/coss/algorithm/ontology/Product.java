package ch.ethz.coss.algorithm.ontology;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/**
 * A class that contains information about a product.
 * @author Thomas Asikis
 * @license Copyright (c) 2017 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class Product implements Serializable{

	private static final long serialVersionUID = 1L;
	private String ean;
	private long id;
	private Set<Long> tags;
	private Map<Long, Integer> categories;
	private String name;
	private String brand;
	private String ingredients;
	private String description;
	
	/**
	 * Create a new product with a unique id and assign an EAN number
	 * @param ean EAN identifier of a product
	 * @param id database or unique id of a product
	 */
	public Product(String ean, long id) {
		this(ean, id, null, null, null);
	}
	
	/**
	 * 
	 * @param ean EAN identifier. May not always be unique or it may be missing...
	 * @param id, unique id for a product
	 * @param name, a product name, usually provided by the retailer
	 * @param brand, the brand of a product
	 * @param ingredients, any ingredients or package description go here.
	 */
	public Product(String ean, long id, String name, String brand, String ingredients) {
		super();
		this.ean = ean;
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.ingredients = ingredients;
		this.tags = new HashSet<>();
		this.categories = new HashMap<>();
	}
	
	
	/**
	 * assign a tag id to the product. Probably the assignment is done by the product properties.
	 * @param tag
	 */
	public void addTagId(long tag){
		tags.add(tag);
	}
	
	
	/**
	 * assign a tag id to the product, based on the id of the provided object. Probably the assignment is done by the product properties.
	 * @param tag
	 */
	public void addTag(ProductTag tag){
		this.addTagId(tag.getId());
	}
	
	/**
	 * add a retailer category label that contains the product. Not used in the algorithm. Was mainly
	 * used for evaluation.
	 * @param category, the category id
	 * @param level, the level of the category
	 */
	public void addCategory(Long category, int level){
		categories.put(category, level);
	}
	
	/**
	 * Add a collection of tags, overloads the varargs addition
	 * @param tags
	 */
	public void addTagIds(Collection<Long> tagIds){
		this.tags.addAll(tagIds);
	}
	
	/**
	 * Add an array of tag ids
	 * @param tagIds
	 */
	public void addTagIds(Long ... tagIds){
		Set<Long> uniqueTags = new HashSet<>();
		for(Long tag : tagIds){
			uniqueTags.add(tag);
		}
		addTagIds(uniqueTags);
	}
	
	/**
	 * Add an array of tag ids
	 * @param tagIds
	 */
	public void addTags(ProductTag ... tagIds){
		Set<Long> uniqueTags = new HashSet<>();
		for(ProductTag tag : tagIds){
			uniqueTags.add(tag.getId());
		}
		addTagIds(uniqueTags);
	}

	
	/*Getters, Setters and String Representation*/

	public String getEan() {
		return ean;
	}
	
	public long getId() {
		return id;
	}


	public Set<Long> getTagIds() {
		return tags;
	}
	
	public Map<Long, Integer> getCategories() {
		return categories;
	}
	
	
	@Override
	public String toString() {
		StringBuilder strb = new StringBuilder("product:{ id: ").append(this.getId());
		strb.append(", name:" + this.getName());
		strb.append(", ean:" + this.getEan());
		strb.append(", brand: " + brand + ", ingredients: " + ingredients);
		strb.append("\n").append("tags: ");
		Iterator<Long> it = tags.iterator();
		while (it.hasNext()) {
			String string = (String) it.next().toString();
			strb.append(string).append(", "); //coma overflow
		}
		strb.append("}\n");
		return strb.toString();
	}

	public Set<Long> getTags() {
		return tags;
	}

	public String getName() {
		return name;
	}

	public String getBrand() {
		return brand;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setTags(Set<Long> tags) {
		this.tags = tags;
	}

	public void setCategories(Map<Long, Integer> categories) {
		this.categories = categories;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
