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

#include <typeinfo>

#include <algorithm>
#include <ode/RTypesI.h>
#include <ode/ClientErrors.h>
#include <ode/ObjectFactoryRegistrar.h>

::Ice::LocalObject* IceInternal::upCast(::ode::rtypes::ObjectFactory* p) { return p; }

namespace ode {

    namespace rtypes {

        // Omitting rtype() for the moment since it seems of less
        // value in C/C++

        // Static factory methods (primitives)
        // =========================================================================

        const ode::RBoolPtr rbool(bool val) {
            static const ode::RBoolPtr rtrue = new RBoolI(true);
            static const ode::RBoolPtr rfalse = new RBoolI(false);
            if (val) {
                return rtrue;
            } else {
                return rfalse;
            }
        }

        const ode::RDoublePtr rdouble(Ice::Double val) {
            return new RDoubleI(val);
        }

        const ode::RFloatPtr rfloat(Ice::Float val) {
            return new RFloatI(val);
        }

        const ode::RIntPtr rint(Ice::Int val) {
            static const ode::RIntPtr rint0 = new RIntI(0);
            if (val == 0) {
                return rint0;
            }
            return new RIntI(val);
        }

        const ode::RLongPtr rlong(Ice::Long val) {
            static const ode::RLongPtr rlong0 = new RLongI(0);
            if (val == 0) {
                return rlong0;
            }
            return new RLongI(val);
        }

        const ode::RTimePtr rtime(Ice::Long val){
            return new RTimeI(val);
        }

        // Static factory methods (objects)
        // =========================================================================

        const ode::RInternalPtr rinternal(const ode::InternalPtr& val) {
            static const ode::RInternalPtr rnullinternal = new RInternalI(ode::InternalPtr());
            if (! val) {
                return rnullinternal;
            }
            return new RInternalI(val);
        }

        const ode::RObjectPtr robject(const ode::model::IObjectPtr& val) {
            static const ode::RObjectPtr rnullobject = new RObjectI(ode::model::IObjectPtr());
            if (! val) {
                return rnullobject;
            }
            return new RObjectI(val);
        }

        const ode::RClassPtr rclass(const std::string& val) {
            static const ode::RClassPtr remptyclass = new RClassI("");
            if (val.empty()) {
                return remptyclass;
            }
            return new RClassI(val);
        }

        const ode::RStringPtr rstring(const std::string& val) {
            static const ode::RStringPtr remptystr = new RStringI("");
            if (val.empty()) {
                return remptystr;
            }
            return new RStringI(val);
        }

        // Static factory methods (collections)
        // =========================================================================

        const ode::RArrayPtr rarray() {
            return new RArrayI();
        }

        const ode::RListPtr rlist() {
            return new RListI();
        }

        const ode::RSetPtr rset() {
            return new RSetI();
        }

        const ode::RMapPtr rmap() {
            return new RMapI();
        }


        // Implementations (primitives)
        // =========================================================================

        template<typename T, typename P>
        Ice::Int compareRTypes(const T& lhs, const ode::RTypePtr& rhs) {

            T rhsCasted(T::dynamicCast(rhs));
            if (!rhsCasted) {
                throw std::bad_cast();
            }

            P val = lhs->getValue();
            P valR = rhsCasted->getValue();

            if (val == valR) {
                return 0;
            } else {
                return val > valR? 1 : -1;
            }
        }

        Ice::Int compareRTypes(const RTypeSeq& lhs, const ode::RTypeSeq& rhs) {

            RTypeSeq val(lhs);
            RTypeSeq valR(rhs);

            bool reversed(false);
            if (valR.size() < val.size()) {
                reversed = true;
                RTypeSeq tmp(val);
                valR = val;
                val = tmp;
            }

            std::pair<RTypeSeq::iterator, RTypeSeq::iterator> idx = std::mismatch(
                    val.begin(), val.end(), valR.begin());

            if (idx.first == val.end() &&
                    idx.second == valR.end()) {
                return 0;
            }

            bool lessThan = std::lexicographical_compare(
                    val.begin(), val.end(),
                    valR.begin(), valR.end());
            if (reversed) {
                return lessThan ? 1 : -1;
            } else {
                return lessThan ? -1 : 1;
            }
        }

        // RBOOL

        RBoolI::RBoolI(bool val) : ode::RBool() {
            this->val = val;
        }

        RBoolI::~RBoolI() { }

        bool RBoolI::getValue(const Ice::Current& /* current */) { return this->val; }

        Ice::Int RBoolI::compare(const ode::RTypePtr& rhs, const Ice::Current& /* current */) {
        return compareRTypes<RBoolPtr, bool>(this, rhs);
        }

        bool operator==(const RBoolPtr& lhs, const RBoolPtr& rhs) {
            return compareRTypes<RBoolPtr, bool>(lhs, rhs) == 0;
        }

        bool operator<(const RBoolPtr& lhs, const RBoolPtr& rhs) {
            return compareRTypes<RBoolPtr, bool>(lhs, rhs) < 0;
        }

        bool operator>(const RBoolPtr& lhs, const RBoolPtr& rhs) {
            return compareRTypes<RBoolPtr, bool>(lhs, rhs) > 0;
        }

        // RDOUBLE

        RDoubleI::RDoubleI(Ice::Double val) {
        this->val = val;
        }

        RDoubleI::~RDoubleI() {}

        Ice::Double RDoubleI::getValue(const Ice::Current& /* current */) { return this->val; }

        Ice::Int RDoubleI::compare(const ode::RTypePtr& rhs, const Ice::Current& /* current */) {
        return compareRTypes<RDoublePtr, Ice::Double>(this, rhs);
        }

        bool operator==(const RDoublePtr& lhs, const RDoublePtr& rhs) {
            return compareRTypes<RDoublePtr, Ice::Double>(lhs, rhs) == 0;
        }

        bool operator<(const RDoublePtr& lhs, const RDoublePtr& rhs) {
            return compareRTypes<RDoublePtr, Ice::Double>(lhs, rhs) < 0;
        }

        bool operator>(const RDoublePtr& lhs, const RDoublePtr& rhs) {
            return compareRTypes<RDoublePtr, Ice::Double>(lhs, rhs) > 0;
        }

        // RFLOAT

        RFloatI::~RFloatI() {}

        RFloatI::RFloatI(Ice::Float val) {
            this->val = val;
        }

        Ice::Float RFloatI::getValue(const Ice::Current& /* current */) { return this->val; }

        Ice::Int RFloatI::compare(const ode::RTypePtr& rhs, const Ice::Current& /* current */) {
            return compareRTypes<RFloatPtr, Ice::Float>(this, rhs);
        }

        bool operator==(const RFloatPtr& lhs, const RFloatPtr& rhs) {
            return compareRTypes<RFloatPtr, Ice::Float>(lhs, rhs) == 0;
        }

        bool operator<(const RFloatPtr& lhs, const RFloatPtr& rhs) {
            return compareRTypes<RFloatPtr, Ice::Float>(lhs, rhs) < 0;
        }

        bool operator>(const RFloatPtr& lhs, const RFloatPtr& rhs) {
            return compareRTypes<RFloatPtr, Ice::Float>(lhs, rhs) > 0;
        }

        // RINT

        RIntI::RIntI(Ice::Int val) {
            this->val = val;
        }

        RIntI::~RIntI() {}

        Ice::Int RIntI::getValue(const Ice::Current& /* current */) { return this->val; }

        Ice::Int RIntI::compare(const ode::RTypePtr& rhs, const Ice::Current& /* current */) {
            return compareRTypes<RIntPtr, Ice::Int>(this, rhs);
        }

        bool operator==(const RIntPtr& lhs, const RIntPtr& rhs) {
            return compareRTypes<RIntPtr, Ice::Int>(lhs, rhs) == 0;
        }

        bool operator<(const RIntPtr& lhs, const RIntPtr& rhs) {
            return compareRTypes<RIntPtr, Ice::Int>(lhs, rhs) < 0;
        }

        bool operator>(const RIntPtr& lhs, const RIntPtr& rhs) {
            return compareRTypes<RIntPtr, Ice::Int>(lhs, rhs) > 0;
        }

        // RLONG

        RLongI::RLongI(Ice::Long val) {
            this->val = val;
        }

        RLongI::~RLongI() {}

        Ice::Long RLongI::getValue(const Ice::Current& /* current */) { return this->val; }

        Ice::Int RLongI::compare(const ode::RTypePtr& rhs, const Ice::Current& /* current */) {
            return compareRTypes<RLongPtr, Ice::Long>(this, rhs);
        }

        bool operator==(const RLongPtr& lhs, const RLongPtr& rhs) {
            return compareRTypes<RLongPtr, Ice::Long>(lhs, rhs) == 0;
        }

        bool operator<(const RLongPtr& lhs, const RLongPtr& rhs) {
            return compareRTypes<RLongPtr, Ice::Long>(lhs, rhs) < 0;
        }

        bool operator>(const RLongPtr& lhs, const RLongPtr& rhs) {
            return compareRTypes<RLongPtr, Ice::Long>(lhs, rhs) > 0;
        }

        // RTIME

        RTimeI::RTimeI(Ice::Long val) {
            this->val = val;
        }

        RTimeI::~RTimeI() {}

        Ice::Long RTimeI::getValue(const Ice::Current& /* current */) { return this->val; }

        Ice::Int RTimeI::compare(const ode::RTypePtr& rhs, const Ice::Current& /* current */) {
            return compareRTypes<RTimePtr, Ice::Long>(this, rhs);
        }

        bool operator==(const RTimePtr& lhs, const RTimePtr& rhs) {
            return compareRTypes<RTimePtr, Ice::Long>(lhs, rhs) == 0;
        }

        bool operator<(const RTimePtr& lhs, const RTimePtr& rhs) {
            return compareRTypes<RTimePtr, Ice::Long>(lhs, rhs) < 0;
        }

        bool operator>(const RTimePtr& lhs, const RTimePtr& rhs) {
            return compareRTypes<RTimePtr, Ice::Long>(lhs, rhs) > 0;
        }

        // Implementations (objects)
        // =========================================================================

        // RINTERNAL

        RInternalI::RInternalI(const ode::InternalPtr& val) {
            this->val = val;
        }

        RInternalI::~RInternalI() {}

        ode::InternalPtr RInternalI::getValue(const Ice::Current& /* current */) { return this->val; }

        Ice::Int RInternalI::compare(const ode::RTypePtr& /* rhs */, const Ice::Current& /* current */) {
            throw ode::ClientError(__FILE__,__LINE__,"Not implemented");
        }

        // RObject

        RObjectI::RObjectI(const ode::model::IObjectPtr& val) {
            this->val = val;
        }

        RObjectI::~RObjectI() {}

        ode::model::IObjectPtr RObjectI::getValue(const Ice::Current& /* current */) { return this->val; }

        Ice::Int RObjectI::compare(const ode::RTypePtr& /* rhs */, const Ice::Current& /* current */) {
            throw ode::ClientError(__FILE__,__LINE__,"Not implemented");
        }

        // RSTRING

        RStringI::RStringI(const std::string& val) {
            this->val = val;
        }

        RStringI::~RStringI() {}

        std::string RStringI::getValue(const Ice::Current& /* current */) { return this->val; }

        Ice::Int RStringI::compare(const ode::RTypePtr& rhs, const Ice::Current& /* current */) {
            return compareRTypes<RStringPtr, std::string>(this, rhs);
        }

        bool operator==(const RStringPtr& lhs, const RStringPtr& rhs) {
            return compareRTypes<RStringPtr, std::string>(lhs, rhs) == 0;
        }

        bool operator<(const RStringPtr& lhs, const RStringPtr& rhs) {
            return compareRTypes<RStringPtr, std::string>(lhs, rhs) < 0;
        }

        bool operator>(const RStringPtr& lhs, const RStringPtr& rhs) {
            return compareRTypes<RStringPtr, std::string>(lhs, rhs) > 0;
        }

        // RCLASS

        RClassI::RClassI(const std::string& val) {
            this->val = val;
        }

        RClassI::~RClassI() {}

        std::string RClassI::getValue(const Ice::Current& /* current */) { return this->val; }

        Ice::Int RClassI::compare(const ode::RTypePtr& /* rhs */, const Ice::Current& /* current */) {
            throw ode::ClientError(__FILE__,__LINE__,"Not implemented");
        }


        // Implementations (collections)
        // =========================================================================

        bool operator==(const RTypeSeq& lhs, const RTypeSeq& rhs) {
            return compareRTypes(lhs, rhs) == 0;
        }

        bool operator<(const RTypeSeq& lhs, const RTypeSeq& rhs) {
            return compareRTypes(lhs, rhs) < 0;
        }

        bool operator>(const RTypeSeq& lhs, const RTypeSeq& rhs) {
            return compareRTypes(lhs, rhs) > 0;
        }

        // RARRAY

        RArrayI::RArrayI(const ode::RTypePtr& value) {
            this->val = ode::RTypeSeq();
            this->val.push_back(value);
        }

        RArrayI::RArrayI(const ode::RTypeSeq& values) {
            this->val = values;
        }

        RArrayI::~RArrayI() {}

        RTypeSeq RArrayI::getValue(const Ice::Current& /* current */) { return this->val; }

        Ice::Int RArrayI::compare(const ode::RTypePtr& rhs, const Ice::Current& /* current */) {
            return compareRTypes<RArrayPtr, RTypeSeq>(this, rhs);
        }

        bool operator==(const RArrayPtr& lhs, const RArrayPtr& rhs) {
            return compareRTypes<RArrayPtr, RTypeSeq>(lhs, rhs) == 0;
        }

        bool operator<(const RArrayPtr& lhs, const RArrayPtr& rhs) {
            return compareRTypes<RArrayPtr, RTypeSeq>(lhs, rhs) < 0;
        }

        bool operator>(const RArrayPtr& lhs, const RArrayPtr& rhs) {
            return compareRTypes<RArrayPtr, RTypeSeq>(lhs, rhs) > 0;
        }


        // Collection methods
        ode::RTypePtr RArrayI::get(Ice::Int idx, const Ice::Current& /* current */) {
            return this->val[idx];
        }

        Ice::Int RArrayI::size(const Ice::Current& /* current */) {
            return static_cast<Ice::Int>(this->val.size());
        }

        void RArrayI::add(const ode::RTypePtr& val, const Ice::Current& /* current */) {
            this->val.push_back(val);
        }

        void RArrayI::addAll(const ode::RTypeSeq& values, const Ice::Current& /* current */) {
            ode::RTypeSeq::const_iterator itr;
            for (itr = values.begin(); itr != values.end(); itr++) {
                this->val.push_back(*itr);
            }
        }

        // RLIST

        RListI::RListI(const ode::RTypePtr& value)  {
            this->val = ode::RTypeSeq();
            this->val.push_back(value);
        }

        RListI::RListI(const ode::RTypeSeq& values) {
            this->val = values;
        }

        RListI::~RListI() {}

        RTypeSeq RListI::getValue(const Ice::Current& /* current */) { return this->val; }

        Ice::Int RListI::compare(const ode::RTypePtr& rhs, const Ice::Current& /* current */) {
        return compareRTypes<RListPtr, RTypeSeq>(this, rhs);
        }

        bool operator==(const RListPtr& lhs, const RListPtr& rhs) {
            return compareRTypes<RListPtr, RTypeSeq>(lhs, rhs) == 0;
        }

        bool operator<(const RListPtr& lhs, const RListPtr& rhs) {
            return compareRTypes<RListPtr, RTypeSeq>(lhs, rhs) < 0;
        }

        bool operator>(const RListPtr& lhs, const RListPtr& rhs) {
            return compareRTypes<RListPtr, RTypeSeq>(lhs, rhs) > 0;
        }

        // Collection methods
        ode::RTypePtr RListI::get(Ice::Int idx, const Ice::Current& /* current */) {
            return this->val[idx];
        }

        Ice::Int RListI::size(const Ice::Current& /* current */) {
            return static_cast<Ice::Int>(this->val.size());
        }

        void RListI::add(const ode::RTypePtr& val, const Ice::Current& /* current */) {
            this->val.push_back(val);
        }

        void RListI::addAll(const ode::RTypeSeq& values, const Ice::Current& /* current */) {
            ode::RTypeSeq::const_iterator itr;
            for (itr = values.begin(); itr != values.end(); itr++) {
                this->val.push_back(*itr);
            }
        }

        // RSET

        RSetI::RSetI(const ode::RTypePtr& value) {
            this->val = ode::RTypeSeq();
            this->val.push_back(value);
        }

        RSetI::RSetI(const ode::RTypeSeq& values) {
            this->val = values;
        }

        RSetI::~RSetI() {}

        RTypeSeq RSetI::getValue(const Ice::Current& /* current */) { return this->val; }

        Ice::Int RSetI::compare(const ode::RTypePtr& rhs, const Ice::Current& /* current */) {
            return compareRTypes<RSetPtr, RTypeSeq>(this, rhs);
        }

        bool operator==(const RSetPtr& lhs, const RSetPtr& rhs) {
            return compareRTypes<RSetPtr, RTypeSeq>(lhs, rhs) == 0;
        }

        bool operator<(const RSetPtr& lhs, const RSetPtr& rhs) {
            return compareRTypes<RSetPtr, RTypeSeq>(lhs, rhs) < 0;
        }

        bool operator>(const RSetPtr& lhs, const RSetPtr& rhs) {
            return compareRTypes<RSetPtr, RTypeSeq>(lhs, rhs) > 0;
        }

        // Collection methods
        ode::RTypePtr RSetI::get(Ice::Int idx, const Ice::Current& /* current */) {
            return this->val[idx];
        }

        Ice::Int RSetI::size(const Ice::Current& /* current */) {
            return static_cast<Ice::Int>(this->val.size());
        }

        void RSetI::add(const ode::RTypePtr& val, const Ice::Current& /* current */) {
            this->val.push_back(val);
        }

        void RSetI::addAll(const ode::RTypeSeq& values, const Ice::Current& /* current */) {
            ode::RTypeSeq::const_iterator itr;
            for (itr = values.begin(); itr != values.end(); itr++) {
                this->val.push_back(*itr);
            }
        }

        // RMAP

        RMapI::RMapI(const std::string& key, const ode::RTypePtr& value) {
            this->val = ode::RTypeDict();
            this->val[key] = value;
        }

        RMapI::RMapI(const ode::RTypeDict& values) {
            this->val = values;
        }

        RMapI::~RMapI() {}

        RTypeDict RMapI::getValue(const Ice::Current& /* current */) { return this->val; }

        Ice::Int RMapI::compare(const ode::RTypePtr& rhs, const Ice::Current& /* current */) {
            return compareRTypes<RMapPtr, RTypeDict>(this, rhs);
        }

        // Collection methods
        ode::RTypePtr RMapI::get(const std::string& key, const Ice::Current& /* current */) {
            return this->val[key];
        }

        void RMapI::put(const std::string& key, const ode::RTypePtr& value, const Ice::Current& /* current */) {
            this->val[key] = value;
        }

        Ice::Int RMapI::size(const Ice::Current& /* current */) {
            return static_cast<Ice::Int>(this->val.size());
        }

        // Helpers
        // ========================================================================

        // Conversion classes are for ode.model <--> ome.model only (no C++)

        void ObjectFactory::register_(const Ice::CommunicatorPtr& ic) {
            ic->addObjectFactory(this, this->id);
        }

        ObjectFactory::~ObjectFactory() {}

        class RBoolIFactory : virtual public ObjectFactory {
        protected:
            ~RBoolIFactory() {}
        public:
            RBoolIFactory() {
                this->id = RBoolI::ice_staticId();
            }
            Ice::ObjectPtr create(const std::string& /* id */) {
                return new RBoolI(false);
            }
        };

        class RDoubleIFactory : virtual public ObjectFactory {
        protected:
            ~RDoubleIFactory() {}
        public:
            RDoubleIFactory() {
                this->id = RDoubleI::ice_staticId();
            }
            Ice::ObjectPtr create(const std::string& /* id */) {
                return new RDoubleI(0.0);
            }
        };

        class RFloatIFactory : virtual public ObjectFactory {
        protected:
            ~RFloatIFactory() {}
        public:
            RFloatIFactory() {
                this->id = RFloatI::ice_staticId();
            }
            Ice::ObjectPtr create(const std::string& /* id */) {
                return new RFloatI(0.0);
            }
        };

        class RLongIFactory : virtual public ObjectFactory {
        protected:
            ~RLongIFactory() {}
        public:
            RLongIFactory() {
                this->id = RLongI::ice_staticId();
            }
            Ice::ObjectPtr create(const std::string& /* id */) {
                return new RLongI(0);
            }
        };

        class RIntIFactory : virtual public ObjectFactory {
        protected:
            ~RIntIFactory() {}
        public:
            RIntIFactory() {
                this->id = RIntI::ice_staticId();
            }
            Ice::ObjectPtr create(const std::string& /* id */) {
                return new RIntI(0);
            }
        };

        class RTimeIFactory : virtual public ObjectFactory {
        protected:
            ~RTimeIFactory() {}
        public:
            RTimeIFactory() {
                this->id = RTimeI::ice_staticId();
            }
            Ice::ObjectPtr create(const std::string& /* id */) {
                return new RTimeI(0);
            }
        };

        class RStringIFactory : virtual public ObjectFactory {
        protected:
            ~RStringIFactory() {}
        public:
            RStringIFactory() {
                this->id = RStringI::ice_staticId();
            }
            Ice::ObjectPtr create(const std::string& /* id */) {
                return new RStringI("");
            }
        };

        class RClassIFactory : virtual public ObjectFactory {
        protected:
            ~RClassIFactory() {}
        public:
            RClassIFactory() {
                this->id = RClassI::ice_staticId();
            }
            Ice::ObjectPtr create(const std::string& /* id */) {
                return new RClassI("");
            }
        };

        class RInternalIFactory : virtual public ObjectFactory {
        protected:
            ~RInternalIFactory() {}
        public:
            RInternalIFactory() {
                this->id = RInternalI::ice_staticId();
            }
            Ice::ObjectPtr create(const std::string& /* id */) {
                return new RInternalI(ode::InternalPtr());
            }
        };

        class RObjectIFactory : virtual public ObjectFactory {
        protected:
            ~RObjectIFactory() {}
        public:
            RObjectIFactory() {
                this->id = RObjectI::ice_staticId();
            }
            Ice::ObjectPtr create(const std::string& /* id */) {
                return new RObjectI(ode::model::IObjectPtr());
            }
        };

        class RArrayIFactory : virtual public ObjectFactory {
        protected:
            ~RArrayIFactory() {}
        public:
            RArrayIFactory() {
                this->id = RArrayI::ice_staticId();
            }
            Ice::ObjectPtr create(const std::string& /* id */) {
                return new RArrayI();
            }
        };

        class RListIFactory : virtual public ObjectFactory {
        protected:
            ~RListIFactory() {}
        public:
            RListIFactory() {
                this->id = RListI::ice_staticId();
            }
            Ice::ObjectPtr create(const std::string& /* id */) {
                return new RListI();
            }
        };

        class RSetIFactory : virtual public ObjectFactory {
        protected:
            ~RSetIFactory() {}
        public:
            RSetIFactory() {
                this->id = RSetI::ice_staticId();
            }
            Ice::ObjectPtr create(const std::string& /* id */) {
                return new RSetI();
            }
        };

        class RMapIFactory : virtual public ObjectFactory {
        protected:
            ~RMapIFactory() {}
        public:
            RMapIFactory() {
                this->id = RMapI::ice_staticId();
            }
            Ice::ObjectPtr create(const std::string& /* id */) {
                return new RMapI();
            }
        };


        // Shared state (flyweight)
        // =========================================================================

        // Defined in header

        // Object factories
        // =========================================================================

        void registerObjectFactory(const Ice::CommunicatorPtr ic) {
            ode::conditionalAdd(RBoolI::ice_staticId(), ic, new RBoolIFactory());
            ode::conditionalAdd(RDoubleI::ice_staticId(), ic, new RDoubleIFactory());
            ode::conditionalAdd(RFloatI::ice_staticId(), ic, new RFloatIFactory());
            ode::conditionalAdd(RIntI::ice_staticId(), ic, new RIntIFactory());
            ode::conditionalAdd(RLongI::ice_staticId(), ic, new RLongIFactory());
            ode::conditionalAdd(RTimeI::ice_staticId(), ic, new RTimeIFactory());
            ode::conditionalAdd(RClassI::ice_staticId(), ic, new RClassIFactory());
            ode::conditionalAdd(RStringI::ice_staticId(), ic, new RStringIFactory());
            ode::conditionalAdd(RInternalI::ice_staticId(), ic, new RInternalIFactory());
            ode::conditionalAdd(RObjectI::ice_staticId(), ic, new RObjectIFactory());
            ode::conditionalAdd(RArrayI::ice_staticId(), ic, new RArrayIFactory());
            ode::conditionalAdd(RListI::ice_staticId(), ic, new RListIFactory());
            ode::conditionalAdd(RSetI::ice_staticId(), ic, new RSetIFactory());
            ode::conditionalAdd(RMapI::ice_staticId(), ic, new RMapIFactory());
        }
    }

}
