package ch.ethz.coss.algorithm.utilities;
/**
 * A class with String related utilities.
 * @author Thomas Asikis
 * @license Copyright (c) 2017 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */
public class StringHelper {
	/**
	 * A method that joins/merges the elements of a String array while inserting a delimiter String between them.
	 * @param input a String array that needs to be joined
	 * @param delimiter the delimiter String
	 * @return the String that contains the joined elements
	 */
	public static String implodeArray(String[] input, String delimiter){
		StringBuilder strb = new StringBuilder();
		int i = 0;
		for(String string : input){
			strb.append(string);
			if(i < input.length-1)
				strb.append(delimiter);
			i++;
		}
		return strb.toString();
	}
}
