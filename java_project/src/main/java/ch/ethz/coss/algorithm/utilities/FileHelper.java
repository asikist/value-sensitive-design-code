package ch.ethz.coss.algorithm.utilities;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
/**
 * A class that contains utilities for reading and managing data from files.
 * @author Thomas Asikis
 * @license Copyright (c) 2017-2020 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */
public class FileHelper {
	/**
	 *  Reads a file on the given path line by line
	 * @param path
	 * @param cs
	 * @return
	 * @throws IOException
	 */
	public static List<String> realLines(Path path, Charset cs) throws IOException{
		return Files.readAllLines(path, cs);
	}
	
	/**
	 * Reads a file line by line and then splits it
	 * @param path
	 * @param cs
	 * @param delimeter
	 * @return
	 * @throws IOException
	 */
	public static List<String[]> realLinesSplit(Path path, Charset cs, String delimeter) throws IOException{
		List<String[]> delimeted = new ArrayList<>();
		List<String> lines = realLines(path, cs);
		
		for(String line : lines){
			delimeted.add(line.split(delimeter, -1));
		}		
		return delimeted;		
	}
}
