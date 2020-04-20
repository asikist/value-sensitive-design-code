package ch.ethz.coss.algorithm.utilities;

import java.io.Serializable;

/**
 * A class that can be used as a datastructure for pairs of serializable objects.
 * @author Thomas Asikis
 * @license Copyright (c) 2017-2020 Thomas Asikis
 *			Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 			The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 			THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

public class Pair<V extends Serializable, T extends Serializable> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	V first;
	T second;
	
	/**
	 * A creation of a two object tupple,or simply put a pair of elements
	 * @param first the first element
	 * @param second the second element
	 */
	public Pair(V first, T second) {
		this.first = first;
		this.second = second;
	}

	public V getFirst() {
		return first;
	}

	public void setFirst(V first) {
		this.first = first;
	}

	public T getSecond() {
		return second;
	}

	public void setSecond(T second) {
		this.second = second;
	}
	
    /**
     * <p>Returns a suitable hash code.
     * The hash code follows the definition in {@code Map.Entry}.</p>
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        // see Map.Entry API specification
    	return (getFirst() == null ? 0 : getFirst().hashCode()) ^
                (getSecond() == null ? 0 : getSecond().hashCode());
    }
    
    @Override
    public boolean equals(Object obj) {
    	  if (obj == this) {
              return true;
          }
          if (obj instanceof Pair<?, ?>) {
              final Pair<?, ?> other = (Pair<?, ?>) obj;
              return this.getFirst().equals(other.getFirst())
                      && this.getSecond().equals(other.getSecond());
          }
          return false;	
    
    
    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return first.toString() + " :: " +second.toString();
    }
	
	
}
