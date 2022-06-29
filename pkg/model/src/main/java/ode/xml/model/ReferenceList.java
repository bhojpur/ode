package ode.xml.model;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 */
public class ReferenceList<T> extends ArrayList<T> {

  private Set<T> backingSet = new HashSet<T>();

  public ReferenceList() {
    super();
  }

  public ReferenceList(Collection<? extends T> c) {
    super(c);
  }

  public boolean add(T element) {
    if (backingSet.add(element)) {
      return super.add(element);
    }
    return false;
  }

  public void add(int index, T element) {
    if (backingSet.add(element)) {
      super.add(index, element);
    }
  }

  public boolean addAll(Collection<? extends T> c) {
    if (backingSet.addAll(c)) {
      return super.addAll(c);
    }
    return false;
  }

  public boolean addAll(int index, Collection<? extends T> c) {
    if (backingSet.addAll(c)) {
      return super.addAll(index, c);
    }
    return false;
  }

  public void clear() {
    super.clear();
    backingSet.clear();
  }

  public boolean contains(Object o) {
    return backingSet.contains(o);
  }

  public boolean containsAll(Collection<?> c) {
    return backingSet.containsAll(c);
  }

  public boolean isEmpty() {
    return backingSet.isEmpty();
  }

  public T remove(int index) {
    T removed = super.remove(index);
    backingSet.remove(removed);
    return removed;
  }

  public boolean remove(Object o) {
    if (backingSet.remove(o)) {
      return super.remove(o);
    }
    return false;
  }

  public boolean removeAll(Collection<?> c) {
    if (backingSet.removeAll(c)) {
      super.removeAll(c);
    }
    return false;
  }

  public boolean retainAll(Collection<?> c) {
    if (backingSet.retainAll(c)) {
      super.retainAll(c);
    }
    return false;
  }

  public T set(int index, T element) {
    T removed = remove(index);
    add(index, element);
    return removed;
  }

}