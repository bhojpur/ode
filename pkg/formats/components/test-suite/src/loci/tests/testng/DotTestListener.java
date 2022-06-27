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

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * A TestNG listener that displays a "." for each passed test,
 * an "F" for each failure and an "S" for each skip. Adapted from an
 * <a href="http://testng.org/doc/documentation-main.html#logging">example</a>
 * on the TestNG web site.
 */
public class DotTestListener implements ITestListener {

  private int count = 0;

  @Override
  public void onTestFailure(ITestResult tr) { log("F"); }
  @Override
  public void onTestSkipped(ITestResult tr) { log("-"); }
  @Override
  public void onTestSuccess(ITestResult tr) { log("."); }

  @Override
  public void onFinish(ITestContext tc) { }
  @Override
  public void onStart(ITestContext tc) { }
  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult tr) { }
  @Override
  public void onTestStart(ITestResult tr) { }

  private void log(String s) {
    System.out.print(s);
    if (++count % 40 == 0) System.out.println();
  }

}
