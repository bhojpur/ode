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

#ifndef ODE_SYS_PARAMETERSI_H
#define ODE_SYS_PARAMETERSI_H

#include <ode/IceNoWarnPush.h>
#include <ode/System.h>
#include <ode/IceNoWarnPop.h>
#include <IceUtil/Config.h>
#include <Ice/Handle.h>
#include <Ice/Config.h>
#include <iostream>
#include <string>
#include <vector>

#ifndef ODE_CLIENT
#   ifdef ODE_CLIENT_EXPORTS
#       define ODE_CLIENT ICE_DECLSPEC_EXPORT
#   else
#       define ODE_CLIENT ICE_DECLSPEC_IMPORT
#   endif
#endif

namespace ode {
    namespace sys {
        class ODE_CLIENT ParametersI; // Forward
    }
}

namespace IceInternal {
  ODE_CLIENT ::Ice::Object* upCast(::ode::sys::ParametersI*);
}

namespace ode {

    namespace sys {

	/**
	 * Ice versions 3.4 and 3.5 (3.6 needs evaluating) have a bug
	 * preventing the use of IceUtil::Handle (generated classes
	 * such as ode::sys::Parameters are derived from both
	 * IceUtil::Shared and IceInternal::GCShared which both
	 * provide __incRef() and __decRef() and so the Handle class
	 * can't call them without causing a compilation failure.
	 * Until this is fixed, using the internal handle type is a
	 * workaround.
	 */
        typedef IceInternal::Handle<ParametersI> ParametersIPtr;

        /*
         * Helper subclass of ode::sys::Parameters for simplifying method
	 * parameter creation.
         */
        class ParametersI : virtual public Parameters {

	protected:
	    ~ParametersI(); // protected as outlined in Ice docs.
	public:
	    /*
	     * If no argument is provided, creates an empty ParamMap for use
	     * by this instance.
	     *
	     * Uses (and does not copy) the given map as the named parameter
	     * store. Be careful if either null is passed in or if this instance
	     * is being used in a multi-threaded environment. No synchrnization
	     * takes place.
	     */
	    ParametersI(const ode::sys::ParamMap& map = ode::sys::ParamMap());

	    // Parameters.theFilter.limit & offset
	    // ===============================================================

	    /*
	     * Nulls both the Filter.limit and Filter.offset values.
	     */
	    ParametersIPtr noPage();

	    /*
	     * Sets both the Filter.limit and Filter.offset values by
	     * wrapping the arguments in ode::RInts and calling
	     * page(RIntPtr&, RIntPtr&)
	     */
	    ParametersIPtr page(Ice::Int offset, Ice::Int limit);

	    /*
	     * Creates a Filter if necessary and sets both Filter.limit
	     * and Filter.offset
	     */
	    ParametersIPtr page(const ode::RIntPtr& offset,
				const ode::RIntPtr& limit);

	    /*
	     * Returns true if the filter contains a limit OR an offset.
	     * false otherwise.
	     */
	    bool isPagination();

	    ode::RIntPtr getOffset();
	    ode::RIntPtr getLimit();
	    ParametersIPtr unique();
	    ParametersIPtr noUnique();
	    ode::RBoolPtr getUnique();

	    // Parameters.theFilter.ownerId & groupId
	    // ===============================================================

	    ParametersIPtr exp(Ice::Long id);
	    ParametersIPtr allExps();
	    bool isExperimenter();
	    ode::RLongPtr getExperimenter();

	    ParametersIPtr grp(Ice::Long id);
	    ParametersIPtr allGrps();
	    bool isGroup();
	    ode::RLongPtr getGroup();

	    // Parameters.theFilter.starttime, endTime
	    // ===============================================================

	    ParametersIPtr startTime(const ode::RTimePtr& time);
	    ParametersIPtr endTime(const ode::RTimePtr& time);
	    ParametersIPtr allTimes();
	    bool isStartTime();
	    bool isEndTime();
	    ode::RTimePtr getStartTime();
	    ode::RTimePtr getEndTime();

	    // Parameters.theOptions.leaves, orphan, acquisitionData
	    // ===============================================================

	    ParametersIPtr leaves();
	    ParametersIPtr noLeaves();
	    ode::RBoolPtr getLeaves();

	    ParametersIPtr orphan();
	    ParametersIPtr noOrphan();
	    ode::RBoolPtr getOrphan();

	    ParametersIPtr acquisitionData();
	    ParametersIPtr noAcquisitionData();
	    ode::RBoolPtr getAcquisitionData();

	    // Parameters.map
	    // ===============================================================

	    ParametersIPtr add(const std::string& name,
			       const ode::RTypePtr& r);
	    ParametersIPtr addId(Ice::Long id);
	    ParametersIPtr addId(const ode::RLongPtr& id);
	    ParametersIPtr addIds(ode::sys::LongList ids);
	    ParametersIPtr addLong(const std::string& name, Ice::Long l);
	    ParametersIPtr addLong(const std::string& name,
				   const ode::RLongPtr& l);
	    ParametersIPtr addLongs(const std::string& name,
				    ode::sys::LongList longs);

	};

    } // sys

} // ode
#endif // ODE_SYS_PARAMETERSI_H
