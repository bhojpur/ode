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

#ifndef ODE_RTYPESI_H
#define ODE_RTYPESI_H

#include <ode/IceNoWarnPush.h>
#include <ode/RTypes.h>
#include <ode/model/RTypes.h>
#include <ode/Scripts.h>
#include <Ice/Ice.h>
#include <ode/IceNoWarnPop.h>

#include <IceUtil/Config.h>
#include <Ice/Handle.h>
#include <string>
#include <map>

#ifndef ODE_CLIENT
#   ifdef ODE_CLIENT_EXPORTS
#       define ODE_CLIENT ICE_DECLSPEC_EXPORT
#   else
#       define ODE_CLIENT ICE_DECLSPEC_IMPORT
#   endif
#endif

/**
 * Header which is responsible for creating rtypes from static
 * factory methods. Where possible, factory methods return cached values
 * (the fly-weight pattern) such that <code>rbool(true) == rbool(true)</code>
 * might hold true.

 * This module is meant to be kept in sync with the abstract Java class
 * ode.rtypes as well as the ode/rtypes.py module.
 */

namespace ode {
    namespace rtypes {
        class ObjectFactory;
    }
}

namespace IceInternal {
  ODE_CLIENT ::Ice::LocalObject* upCast(::ode::rtypes::ObjectFactory*);
}

namespace ode {

    namespace rtypes {

        // Static factory methods (primitives)
        // =========================================================================

        ODE_CLIENT const ode::RBoolPtr rbool(bool val);
        ODE_CLIENT const ode::RDoublePtr rdouble(Ice::Double val);
        ODE_CLIENT const ode::RFloatPtr rfloat(Ice::Float val);
        ODE_CLIENT const ode::RIntPtr rint(Ice::Int val);
        ODE_CLIENT const ode::RLongPtr rlong(Ice::Long val);
        ODE_CLIENT const ode::RTimePtr rtime(Ice::Long val);

        // Static factory methods (objects)
        // =========================================================================

        ODE_CLIENT const ode::RInternalPtr rinternal(const ode::InternalPtr& val);
        ODE_CLIENT const ode::RObjectPtr robject(const ode::model::IObjectPtr& val);
        ODE_CLIENT const ode::RClassPtr rclass(const std::string& val);
        ODE_CLIENT const ode::RStringPtr rstring(const std::string& val);

        // Static factory methods (collections)
        // =========================================================================

        ODE_CLIENT const ode::RArrayPtr rarray();
        ODE_CLIENT const ode::RListPtr rlist();
        ODE_CLIENT const ode::RSetPtr rset();
        ODE_CLIENT const ode::RMapPtr rmap();

        // Implementations (primitives)
        // =========================================================================

        class ODE_CLIENT RBoolI : virtual public ode::RBool {
        protected:
            virtual ~RBoolI(); // protected as outlined in Ice Docs
        public:
            RBoolI(bool value);
            virtual bool getValue(const Ice::Current& current = Ice::Current());
            virtual Ice::Int compare(const RTypePtr& rhs, const Ice::Current& current = Ice::Current());
        };
        ODE_CLIENT
        bool operator<(const RBoolPtr& lhs,
                       const RBoolPtr& rhs);
        ODE_CLIENT
        bool operator>(const RBoolPtr& lhs,
                       const RBoolPtr& rhs);
        ODE_CLIENT
        bool operator==(const RBoolPtr& lhs,
                        const RBoolPtr& rhs);

        class ODE_CLIENT RDoubleI : virtual public ode::RDouble {
        protected:
            virtual ~RDoubleI(); // as above
        public:
            RDoubleI(Ice::Double value);
            virtual Ice::Double getValue(const Ice::Current& current = Ice::Current());
            virtual Ice::Int compare(const RTypePtr& rhs, const Ice::Current& current = Ice::Current());
        };
        ODE_CLIENT
        bool operator<(const RDoublePtr& lhs,
                       const RDoublePtr& rhs);
        ODE_CLIENT
        bool operator>(const RDoublePtr& lhs,
                       const RDoublePtr& rhs);
        ODE_CLIENT
        bool operator==(const RDoublePtr& lhs,
                        const RDoublePtr& rhs);

        class ODE_CLIENT RFloatI : virtual public ode::RFloat {
        protected:
            virtual ~RFloatI(); // as above
        public:
            RFloatI(Ice::Float value);
            virtual Ice::Float getValue(const Ice::Current& current = Ice::Current());
            virtual Ice::Int compare(const RTypePtr& rhs, const Ice::Current& current = Ice::Current());
        };
        ODE_CLIENT
        bool operator<(const RFloatPtr& lhs,
                       const RFloatPtr& rhs);
        ODE_CLIENT
        bool operator>(const RFloatPtr& lhs,
                       const RFloatPtr& rhs);
        ODE_CLIENT
        bool operator==(const RFloatPtr& lhs,
                        const RFloatPtr& rhs);

        class ODE_CLIENT RIntI : virtual public ode::RInt {
        protected:
            virtual ~RIntI(); // as above
        public:
            RIntI(Ice::Int value);
            virtual Ice::Int getValue(const Ice::Current& current = Ice::Current());
            virtual Ice::Int compare(const RTypePtr& rhs, const Ice::Current& current = Ice::Current());
        };
        ODE_CLIENT
        bool operator<(const RIntPtr& lhs,
                       const RIntPtr& rhs);
        ODE_CLIENT
        bool operator>(const RIntPtr& lhs,
                       const RIntPtr& rhs);
        ODE_CLIENT
        bool operator==(const RIntPtr& lhs,
                        const RIntPtr& rhs);

        class ODE_CLIENT RLongI : virtual public ode::RLong {
        protected:
            virtual ~RLongI(); // as above
        public:
            RLongI(Ice::Long value);
            virtual Ice::Long getValue(const Ice::Current& current = Ice::Current());
            virtual Ice::Int compare(const RTypePtr& rhs, const Ice::Current& current = Ice::Current());
        };
        ODE_CLIENT
        bool operator<(const RLongPtr& lhs,
                       const RLongPtr& rhs);
        ODE_CLIENT
        bool operator>(const RLongPtr& lhs,
                       const RLongPtr& rhs);
        ODE_CLIENT
        bool operator==(const RLongPtr& lhs,
                        const RLongPtr& rhs);

        class ODE_CLIENT RTimeI : virtual public ode::RTime {
        protected:
            virtual ~RTimeI(); // as above
        public:
            RTimeI(Ice::Long value);
            virtual Ice::Long getValue(const Ice::Current& current = Ice::Current());
            virtual Ice::Int compare(const RTypePtr& rhs, const Ice::Current& current = Ice::Current());
        };
        ODE_CLIENT
        bool operator<(const RTimePtr& lhs,
                       const RTimePtr& rhs);
        ODE_CLIENT
        bool operator>(const RTimePtr& lhs,
                       const RTimePtr& rhs);
        ODE_CLIENT
        bool operator==(const RTimePtr& lhs,
                        const RTimePtr& rhs);

        // Implementations (objects)
        // =========================================================================

        class ODE_CLIENT RInternalI : virtual public ode::RInternal {
        protected:
            virtual ~RInternalI(); // as above
        public:
            RInternalI(const ode::InternalPtr& value);
            virtual ode::InternalPtr getValue(const Ice::Current& current = Ice::Current());
            virtual Ice::Int compare(const RTypePtr& rhs, const Ice::Current& current = Ice::Current());
        };

        class ODE_CLIENT RObjectI : virtual public ode::RObject {
        protected:
            virtual ~RObjectI(); // as above
        public:
            RObjectI(const ode::model::IObjectPtr& value);
            virtual ode::model::IObjectPtr getValue(const Ice::Current& current = Ice::Current());
            virtual Ice::Int compare(const RTypePtr& rhs, const Ice::Current& current = Ice::Current());
        };

        class ODE_CLIENT RStringI : virtual public ode::RString {
        protected:
            virtual ~RStringI(); // as above
        public:
            RStringI(const std::string& value);
            virtual std::string getValue(const Ice::Current& current = Ice::Current());
            virtual Ice::Int compare(const RTypePtr& rhs, const Ice::Current& current = Ice::Current());
        };
        ODE_CLIENT
        bool operator<(const RStringPtr& lhs,
                       const RStringPtr& rhs);
        ODE_CLIENT
        bool operator>(const RStringPtr& lhs,
                       const RStringPtr& rhs);
        ODE_CLIENT
        bool operator==(const RStringPtr& lhs,
                        const RStringPtr& rhs);

        class ODE_CLIENT RClassI : virtual public ode::RClass {
        protected:
            virtual ~RClassI(); // as above
        public:
            RClassI(const std::string& value);
            virtual std::string getValue(const Ice::Current& current = Ice::Current());
            virtual Ice::Int compare(const RTypePtr& rhs, const Ice::Current& current = Ice::Current());
        };

        // Implementations (collections)
        // =========================================================================

        ODE_CLIENT
        bool operator<(const RTypeSeq& lhs,
                       const RTypeSeq& rhs);
        ODE_CLIENT
        bool operator>(const RTypeSeq& lhs,
                       const RTypeSeq& rhs);
        ODE_CLIENT
        bool operator==(const RTypeSeq& lhs,
                        const RTypeSeq& rhs);

        /**
         * Guaranteed to never contain an empty list.
         */
        class ODE_CLIENT RArrayI : virtual public ode::RArray {
        protected:
            virtual ~RArrayI(); // as above
        public:
            RArrayI(const ode::RTypePtr& value);
            RArrayI(const ode::RTypeSeq& values = ode::RTypeSeq());
            virtual ode::RTypeSeq getValue(const Ice::Current& current = Ice::Current());
            virtual Ice::Int compare(const RTypePtr& rhs, const Ice::Current& current = Ice::Current());
            // Collection methods
            virtual ode::RTypePtr get(Ice::Int idx, const Ice::Current& current = Ice::Current());
            virtual Ice::Int size(const Ice::Current& current = Ice::Current());
            virtual void add(const ode::RTypePtr& val, const Ice::Current& current = Ice::Current());
            virtual void addAll(const ode::RTypeSeq& values, const Ice::Current& current = Ice::Current());
        };
        ODE_CLIENT
        bool operator<(const RArrayPtr& lhs,
                       const RArrayPtr& rhs);
        ODE_CLIENT
        bool operator>(const RArrayPtr& lhs,
                       const RArrayPtr& rhs);
        ODE_CLIENT
        bool operator==(const RArrayPtr& lhs,
                        const RArrayPtr& rhs);

        /**
         * Guaranteed to never contain an empty list.
         */
        class ODE_CLIENT RListI : virtual public ode::RList {
        protected:
            virtual ~RListI(); // as above
        public:
            RListI(const ode::RTypePtr& value);
            RListI(const ode::RTypeSeq& values = ode::RTypeSeq());
            virtual ode::RTypeSeq getValue(const Ice::Current& current = Ice::Current());
            virtual Ice::Int compare(const RTypePtr& rhs, const Ice::Current& current = Ice::Current());
            // Collection methods
            virtual ode::RTypePtr get(Ice::Int idx, const Ice::Current& current = Ice::Current());
            virtual Ice::Int size(const Ice::Current& current = Ice::Current());
            virtual void add(const ode::RTypePtr& val, const Ice::Current& current = Ice::Current());
            virtual void addAll(const ode::RTypeSeq& values, const Ice::Current& current = Ice::Current());
        };
        ODE_CLIENT
        bool operator<(const RListPtr& lhs,
                       const RListPtr& rhs);
        ODE_CLIENT
        bool operator>(const RListPtr& lhs,
                       const RListPtr& rhs);
        ODE_CLIENT
        bool operator==(const RListPtr& lhs,
                        const RListPtr& rhs);

        /**
         * Guaranteed to never contain an empty list.
         */
        class ODE_CLIENT RSetI : virtual public ode::RSet {
        protected:
            virtual ~RSetI(); // as above
        public:
            RSetI(const ode::RTypePtr& value);
            RSetI(const ode::RTypeSeq& values = ode::RTypeSeq());
            virtual ode::RTypeSeq getValue(const Ice::Current& current = Ice::Current());
            virtual Ice::Int compare(const RTypePtr& rhs, const Ice::Current& current = Ice::Current());
            // Collection methods
            virtual ode::RTypePtr get(Ice::Int idx, const Ice::Current& current = Ice::Current());
            virtual Ice::Int size(const Ice::Current& current = Ice::Current());
            virtual void add(const ode::RTypePtr& val, const Ice::Current& current = Ice::Current());
            virtual void addAll(const ode::RTypeSeq& values, const Ice::Current& current = Ice::Current());
        };
        ODE_CLIENT
        bool operator<(const RSetPtr& lhs,
                       const RSetPtr& rhs);
        ODE_CLIENT
        bool operator>(const RSetPtr& lhs,
                       const RSetPtr& rhs);
        ODE_CLIENT
        bool operator==(const RSetPtr& lhs,
                        const RSetPtr& rhs);

        class ODE_CLIENT RMapI : virtual public ode::RMap {
        protected:
            virtual ~RMapI(); // as above
        public:
            RMapI(const std::string& key, const ode::RTypePtr& value);
            RMapI(const ode::RTypeDict& values = ode::RTypeDict());
            virtual ode::RTypeDict getValue(const Ice::Current& current = Ice::Current());
            virtual Ice::Int compare(const RTypePtr& rhs, const Ice::Current& current = Ice::Current());
            // Collection methods
            virtual ode::RTypePtr get(const std::string& key, const Ice::Current& current = Ice::Current());
            virtual void put(const std::string& key, const ode::RTypePtr& value, const Ice::Current& current = Ice::Current());
            virtual Ice::Int size(const Ice::Current& current = Ice::Current());
        };

        // Helpers
        // ========================================================================

        // Conversion classes are for ode.model <--> ome.model only (no python)
        typedef IceInternal::Handle<ObjectFactory> ObjectFactoryPtr;

        class ObjectFactory : virtual public Ice::ObjectFactory {
        protected:
            std::string id;
            virtual ~ObjectFactory(); // as above
        public:
            virtual void register_(const Ice::CommunicatorPtr& ic);
            virtual Ice::ObjectPtr create(const std::string& id) = 0;
            virtual void destroy() { } // No-op
        };

        Ice::Int compareRTypes(const RTypeSeq& lhs, const ode::RTypeSeq& rhs);

        // Object factories
        // =========================================================================

        ODE_CLIENT void registerObjectFactory(const Ice::CommunicatorPtr ic);

    }

}

#endif // ODE_RTYPESI_H
