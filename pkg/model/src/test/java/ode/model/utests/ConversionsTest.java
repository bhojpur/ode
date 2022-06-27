package ode.model.utests;

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

import java.math.BigDecimal;

import ode.model.units.Conversion;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ConversionsTest {

    @Test
    public void testSimpleAdd() throws Exception {
        Conversion add = Conversion.Add(Conversion.Rat(1, 2), Conversion.Rat(1,2));
        double whole = add.convert(-1).doubleValue(); // -1 is ignored
        Assert.assertEquals(whole, 1.0, 0.0001);
    }

    @Test
    public void testSimpleMul() throws Exception {
        Conversion mul = Conversion.Mul(Conversion.Int(1000000), Conversion.Sym("megas"));
        double seconds = mul.convert(5.0).doubleValue();
        Assert.assertEquals(seconds, 5000000.0, 0.0001);
    }

    @Test
    public void testSimpleInt() throws Exception {
        Conversion i = Conversion.Int(123);
        double x = i.convert(-1).doubleValue(); // -1 is ignored
        Assert.assertEquals(x, 123.0, 0.0001);
    }

    @Test
    public void testBigInt() throws Exception {
        String big = "123456789012345678901234567891234567890";
        big = big + big + big + big + big;
        Conversion i = Conversion.Mul(Conversion.Int(big), Conversion.Int(big));
        BigDecimal rv = i.convert(-1);
        Assert.assertEquals(rv.doubleValue(), Double.POSITIVE_INFINITY);
    }

    @Test
    public void testSimplePow() throws Exception {
        Conversion p = Conversion.Pow(3, 2);
        double x = p.convert(-1).doubleValue(); // -1 is ignored
        Assert.assertEquals(x, 9.0, 0.0001);
    }

    @Test
    public void testSimpleRat() throws Exception {
        Conversion r = Conversion.Rat(1, 3);
        double x = r.convert(-1).doubleValue(); // -1 is ignored
        Assert.assertEquals(x, 0.33333333, 0.0001);
    }

    @Test
    public void testDelayedRat() throws Exception {
        Conversion r = Conversion.Rat(Conversion.Int(1), Conversion.Int(3));
        double x = r.convert(-1).doubleValue(); // -1 is ignored
        Assert.assertEquals(x, 0.33333333, 0.0001);
    }

    @Test
    public void testSimpleSym() throws Exception {
        Conversion sym = Conversion.Sym("x");
        double x = sym.convert(5.0).doubleValue();
        Assert.assertEquals(x, 5.0, 0.0001);
    }
    
    @Test
    public void testFahrenheit() throws Exception {
        Conversion ftoc = Conversion.Add(Conversion.Mul(Conversion.Rat(5, 9),
                Conversion.Sym("f")), Conversion.Rat(-160, 9));
        Assert.assertEquals(ftoc.convert(32.0).doubleValue(), 0.0, 0.0001);
        Assert.assertEquals(ftoc.convert(212.0).doubleValue(), 100.0, 0.0001);
        Assert.assertEquals(ftoc.convert(-40.0).doubleValue(), -40.0, 0.0001);
    }

}