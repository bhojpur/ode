package loci.tests.testng;

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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;

/**
 */
public class OrderingListener implements IMethodInterceptor {

  @Override
  public List<IMethodInstance> intercept(List<IMethodInstance> methods,
    ITestContext context)
  {
    IMethodInstance[] originalMethods =
      methods.toArray(new IMethodInstance[methods.size()]);

    Arrays.sort(originalMethods, new Comparator<IMethodInstance>() {
      @Override
      public int compare(IMethodInstance m1, IMethodInstance m2) {
        FormatReaderTest i1 = (FormatReaderTest) m1.getInstances()[0];
        FormatReaderTest i2 = (FormatReaderTest) m2.getInstances()[0];
        String instance1 = i1.getID();
        String instance2 = i2.getID();
        int instance = instance1.compareTo(instance2);
        if (instance != 0) {
          return instance;
        }

        ITestNGMethod method1 = m1.getMethod();
        ITestNGMethod method2 = m2.getMethod();

        return method1.getMethodName().compareTo(method2.getMethodName());
      }
    });

    List<IMethodInstance> orderedMethods = new ArrayList<IMethodInstance>();
    for (IMethodInstance method : originalMethods) {
      orderedMethods.add(method);
    }
    return orderedMethods;
  }

}
