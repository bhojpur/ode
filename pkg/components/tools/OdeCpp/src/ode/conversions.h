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

#ifndef ODE_CONVERSIONS_H
#define ODE_CONVERSIONS_H

#include <ode/IceNoWarnPush.h>
#include <Ice/Ice.h>
#include <ode/IceNoWarnPop.h>

#include <IceUtil/Config.h>
#include <Ice/Handle.h>
#include <vector>

#ifndef ODE_CLIENT
#   ifdef ODE_CLIENT_EXPORTS
#       define ODE_CLIENT ICE_DECLSPEC_EXPORT
#   else
#       define ODE_CLIENT ICE_DECLSPEC_IMPORT
#   endif
#endif

namespace ode {
  namespace conversions {
    class Conversion;
    typedef IceUtil::Handle<Conversion> ConversionPtr;
  }
}

namespace IceInternal {
  ODE_CLIENT ::Ice::LocalObject* upCast(::ode::conversions::Conversion*);
}

namespace ode {

  namespace conversions {

    /**
     * Base-functor like object which can be used for preparing complex
     * equations for converting from one unit to another. Primarily these
     * classes and static methods are used via code-generation. Sympy-generated
     * strings are placed directly into code. If the proper imports are in place,
     * then a top-level {@link Conversion} (usually of type {@link Add} or
     * {@link Mul} is returned from the evaluation.
     */
    class ODE_CLIENT Conversion : public IceUtil::Shared {

    private:
        Conversion& operator=(const Conversion& rv);
        Conversion(Conversion&);

    protected:

        /**
         * Conversions, if any, which are passed into the constructor
         * of this instance. If none are passed, then the implementation
         * has a short-cut form, e.g. taking an {@link Integer} rather than
         * an {@link Int}.
         */
        std::vector<ConversionPtr> conversions;

    public:

        /**
         * Constructor for two {@link Conversion} objects. No processing
         * happens during constructor. Instead, the {@link #convert(double)}
         * method will handle descending through the recursive structure.
         */
        Conversion(const ConversionPtr& c1, const ConversionPtr& c2);

        /**
         * Constructor for one {@link Conversion} object. No processing
         * happens during constructor. Instead, the {@link #convert(double)}
         * method will handle descending through the recursive structure.
         */
        Conversion(const ConversionPtr& c1);

        /**
         * Constructor which can be fallen back on when no
         * Conversion instances are passed.
         **/
        Conversion();

        virtual ~Conversion();

        /**
         * Primary operator for {@link Conversion} instances.
         * @param original A unit value which is to be processed through the
         *    tree-like representation of this equation. Only {@link Sym} objects
         *    will actually use the "original" value.
         * @return a {@link BigDecimal} result from the calculation. If this value
         *    maps to {@link Double#NEGATIVE_INFINITY} or
         *    {@link Double#POSITIVE_INFINITY}, then a {@link BigResult} exception
         *    should be thrown before returning to clients.
         */
        virtual double convert(double original) = 0;

    };

    /**
     * Sums all {@link Conversion} instances via {@link BigDecimal#add(BigDecimal)}.
     */
    class ODE_CLIENT _Add : virtual public Conversion {

    private:
        _Add& operator=(const _Add& rv);
        _Add(_Add&);

    public:
        _Add(const ConversionPtr& c1, const ConversionPtr& c2);
        double convert(double original);
    };

    /**
     * Simply is a representation of a possibly large integer.
     */
    class ODE_CLIENT _Int : virtual public Conversion {

    private:
        _Int& operator=(const _Int& rv);
        _Int(_Int&);

    private:
        long i;
        std::string s;

    public:
        _Int(long i);
        _Int(std::string s);
        double convert(double original);
    };

    /**
     * Multiplies all {@link Conversion} instances via
     * {@link BigDecimal#multiply(BigDecimal)}.
     */
    class ODE_CLIENT _Mul : virtual public Conversion {

    private:
        _Mul& operator=(const _Mul& rv);
        _Mul(_Mul&);

    public:
        _Mul(const ConversionPtr& c1, const ConversionPtr& c2);
        double convert(double original);
    };

    /**
     * Exponentiates two {@link Conversion} instances via
     * {@link BigDecimal#pow(BigDecimal)}.
     */
    class ODE_CLIENT _Pow : virtual public Conversion {

    private:
        _Pow& operator=(const _Pow& rv);
        _Pow(_Pow&);

    private:
        long base;
        int exp;

    public:
        _Pow(long base, int exp);
        double convert(double original);

    };

    /**
     * Divides two {@link Conversion} instances via
     * {@link BigDecimal#divide(BigDecimal, MathContext)}.
     */
    class ODE_CLIENT _Rat : virtual public Conversion {

    private:
        _Rat& operator=(const _Rat& rv);
        _Rat(_Rat&);

    private:
        long num;
        long denom;
        bool delay;

    public:
        _Rat(long num, long denom);
        _Rat(const ConversionPtr& c1, const ConversionPtr& c2);
        double convert(double original);

    };

    /**
     * Simply represents the variable of the source unit so that
     * {@link Sym#convert(double)} just returns the value passed in.
     */
    class ODE_CLIENT _Sym : virtual public Conversion {

    private:
        _Sym& operator=(const _Sym& rv);
        _Sym(_Sym&);

    public:
        _Sym(char sym);
        _Sym(std::string sym);
        double convert(double original);
    };

    // =======================================================
    // Helper static methods which prevent the need for "new"
    // in the generated code.
    // =======================================================

    /**
     * Static helper for creating {@link Add} instances.
     */
    static inline ConversionPtr Add(
            const ConversionPtr& c1,
            const ConversionPtr& c2) {
        return new _Add(c1, c2);
    }

    /**
     * Static helper for creating {@link Int} instances.
     */
    static inline ConversionPtr Int(long i) {
        return new _Int(i);
    }

    /**
     * Static helper for creating {@link Int} instances.
     */
    static inline ConversionPtr Int(std::string i) {
        return new _Int(i);
    }

    /**
     * Static helper for creating {@link Mul} instances.
     */
    static inline ConversionPtr Mul(
            const ConversionPtr& c1,
            const ConversionPtr& c2) {
        return new _Mul(c1, c2);
    }

    /**
     * Static helper for creating {@link Pow} instances.
     */
    static inline ConversionPtr Pow(long num, int den) {
        return new _Pow(num, den);
    }

   /**
    * Static helper for creating {@link Rat} instances.
    */
    static inline ConversionPtr Rat(long num, long den) {
        return new _Rat(num, den);
    }

    /**
     * Static helper for creating {@link Rat} instances.
     */
    static inline ConversionPtr Rat(
            const ConversionPtr& c1,
            const ConversionPtr& c2) {
        return new _Rat(c1, c2);
    }

   /**
    * Static helper for creating {@link Sym} instances.
    */
    static inline ConversionPtr Sym(std::string sym) {
        return new _Sym(sym);
    }

  }

}
#endif
