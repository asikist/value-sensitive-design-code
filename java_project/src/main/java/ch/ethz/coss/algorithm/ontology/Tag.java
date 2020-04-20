package ch.ethz.coss.algorithm.ontology;

import java.io.Serializable;
/**
 * A class that contains information about tags, e.g their label/name or id. 
 * @author Thomas Asikis
 * @license Copyright (c) 2017-2020 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */
public abstract class Tag implements Serializable{

	private static final long serialVersionUID = 1L;
	final  long id;
	final  String name;
	final  long versionIn; //for analysis
	final  long versionOut; //for analysis
	
	/**
	 * Create a Tag object. if a version field is unknown, set the corresponding fields as -1. Don't use this
	 * constructor in production, because it will not probably effect anything to set versions.
	 * @param id
	 * @param name
	 * @param versionIn
	 * @param versionOut
	 */
	public Tag(long id, String name, long versionIn, long versionOut) {
		this.id = id;
		this.name = name;
		this.versionIn = versionIn;
		this.versionOut = versionOut;
	}
	
	/**
	 * constructor if both versions are unknow. Sets both to -1
	 * @param id
	 * @param name
	 */
	public Tag(long id, String name) {
		this(id, name, -1, -1);
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public long getVersionIn() {
		return versionIn;
	}

	public long getVersionOut() {
		return versionOut;
	}

	@Override
	public String toString() {
		return "Tag [id=" + id + ", name=" + name + ", versionIn=" + versionIn + ", versionOut=" + versionOut + "]";
	}
	
	
	
	
	
}
