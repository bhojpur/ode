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

#include <ode/sys/ParametersI.h>
#include <ode/RTypesI.h>

using ode::rtypes::rlist;
using ode::rtypes::rlong;
using ode::rtypes::rint;
using ode::rtypes::rbool;

::Ice::Object* IceInternal::upCast(::ode::sys::ParametersI* p) { return p; }

namespace ode {

    namespace sys {

        ParametersI::~ParametersI() {}

        ParametersI::ParametersI(const ode::sys::ParamMap& map) : Parameters() {
	    this->map = map;
        }

	// Parameters.theFilter.limit & offset
	// ===============================================================

	ParametersIPtr ParametersI::noPage() {
	    if (0 != this->theFilter) {
		this->theFilter->limit = ode::RIntPtr();
		this->theFilter->offset = ode::RIntPtr();
	    }
	    return this;
	}

	ParametersIPtr ParametersI::page(Ice::Int offset,
					 Ice::Int limit) {
	    return this->page(rint(offset), rint(limit));
	}

	ParametersIPtr ParametersI::page(const ode::RIntPtr& offset,
					 const ode::RIntPtr& limit) {
	    if (0 == this->theFilter) {
		this->theFilter = new ode::sys::Filter();
	    }
	    this->theFilter->offset = offset;
	    this->theFilter->limit = limit;
	    return this;
	}

	bool ParametersI::isPagination() {
	    if (0 != this->theFilter) {
		return this->theFilter->offset && this->theFilter->limit;
	    }
	    return false;
	}

	ode::RIntPtr ParametersI::getOffset() {
	    if (0 != this->theFilter) {
		return this->theFilter->offset;
	    }
	    return ode::RIntPtr();
	}

	ode::RIntPtr ParametersI::getLimit() {
	    if (0 != this->theFilter) {
		return this->theFilter->limit;
	    }
	    return ode::RIntPtr();
	}

	ParametersIPtr ParametersI::unique() {
	    if (0 == this->theFilter) {
		this->theFilter = new ode::sys::Filter();
	    }
	    this->theFilter->unique = rbool(true);
	    return this;
	}

	ParametersIPtr ParametersI::noUnique() {
	    if (0 == this->theFilter) {
		this->theFilter = new ode::sys::Filter();
	    }
	    this->theFilter->unique = rbool(false);
	    return this;
	}

	ode::RBoolPtr ParametersI::getUnique() {
	    if (0 != this->theFilter) {
		return this->theFilter->unique;
	    }
	    return ode::RBoolPtr();
	}

	// Parameters.theFilter.ownerId & groupId
	// ===============================================================

	ParametersIPtr ParametersI::exp(Ice::Long id) {
	    if (0 == this->theFilter) {
		this->theFilter = new ode::sys::Filter();
	    }
	    this->theFilter->ownerId = rlong(id);
	    return this;
	}

	ParametersIPtr ParametersI::allExps() {
	    if (0 != this->theFilter) {
		this->theFilter->ownerId = ode::RLongPtr();
	    }
	    return this;
	}

	bool ParametersI::isExperimenter() {
	    if (0 != this->theFilter) {
		return this->theFilter->ownerId != 0;
	    }
	    return false;
	}

	ode::RLongPtr ParametersI::getExperimenter() {
	    if (0 != this->theFilter) {
		return this->theFilter->ownerId;
	    }
	    return ode::RLongPtr();
	}

	ParametersIPtr ParametersI::grp(Ice::Long id) {
	    if (0 == this->theFilter) {
		this->theFilter = new ode::sys::Filter();
	    }
	    this->theFilter->groupId = rlong(id);
	    return this;
	}

	ParametersIPtr ParametersI::allGrps() {
	    if (0 != this->theFilter) {
		this->theFilter->groupId = ode::RLongPtr();
	    }
	    return this;
	}

	bool ParametersI::isGroup() {
	    if (0 != this->theFilter) {
		return this->theFilter->groupId != 0;
	    }
	    return false;
	}

	ode::RLongPtr ParametersI::getGroup() {
	    if (0 != this->theFilter) {
		return this->theFilter->groupId;
	    }
	    return ode::RLongPtr();
	}

	// Parameters.theFilter.starttime, endTime
	// ===============================================================

	ParametersIPtr ParametersI::startTime(const ode::RTimePtr& time) {
	    if (0 == this->theFilter) {
		this->theFilter = new ode::sys::Filter();
	    }
	    this->theFilter->startTime = time;
	    return this;
	}

	ParametersIPtr ParametersI::endTime(const ode::RTimePtr& time) {
	    if (0 == this->theFilter) {
		this->theFilter = new ode::sys::Filter();
	    }
	    this->theFilter->endTime = time;
	    return this;
	}

	ParametersIPtr ParametersI::allTimes() {
	    if (0 != this->theFilter) {
		this->theFilter->startTime = ode::RTimePtr();
		this->theFilter->endTime = ode::RTimePtr();
	    }
	    return this;
	}

	bool ParametersI::isStartTime() {
	    if (0 != this->theFilter) {
		return this->theFilter->startTime != 0;
	    }
	    return false;
	}

	bool ParametersI::isEndTime() {
	    if (0 != this->theFilter) {
		return this->theFilter->endTime != 0;
	    }
	    return false;
	}

	ode::RTimePtr ParametersI::getStartTime() {
	    if (0 != this->theFilter) {
		return this->theFilter->startTime;
	    }
	    return ode::RTimePtr();
	}

	ode::RTimePtr ParametersI::getEndTime() {
	    if (0 != this->theFilter) {
		return this->theFilter->endTime;
	    }
	    return ode::RTimePtr();
	}


	// Parameters.theOptions.leaves, orphan, acquisitionData
	// ===============================================================

	ParametersIPtr ParametersI::leaves() {
	    if (0 == this->theOptions) {
		this->theOptions = new ode::sys::Options();
	    }
	    this->theOptions->leaves = rbool(true);
	    return this;
	}

	ParametersIPtr ParametersI::noLeaves() {
	    if (0 == this->theOptions) {
		this->theOptions = new ode::sys::Options();
	    }
	    this->theOptions->leaves = rbool(false);
	    return this;
	}

	ode::RBoolPtr ParametersI::getLeaves() {
	    if (0 != this->theOptions) {
		return this->theOptions->leaves;
	    }
	    return ode::RBoolPtr();
	}

	ParametersIPtr ParametersI::orphan() {
	    if (0 == this->theOptions) {
		this->theOptions = new ode::sys::Options();
	    }
	    this->theOptions->orphan = rbool(true);
	    return this;
	}

	ParametersIPtr ParametersI::noOrphan() {
	    if (0 == this->theOptions) {
		this->theOptions = new ode::sys::Options();
	    }
	    this->theOptions->orphan = rbool(false);
	    return this;
	}

	ode::RBoolPtr ParametersI::getOrphan() {
	    if (0 != this->theOptions) {
		return this->theOptions->orphan;
	    }
	    return ode::RBoolPtr();
	}

	ParametersIPtr ParametersI::acquisitionData() {
	    if (0 == this->theOptions) {
		this->theOptions = new ode::sys::Options();
	    }
	    this->theOptions->acquisitionData = rbool(true);
	    return this;
	}

	ParametersIPtr ParametersI::noAcquisitionData() {
	    if (0 == this->theOptions) {
		this->theOptions = new ode::sys::Options();
	    }
	    this->theOptions->acquisitionData = rbool(false);
	    return this;
	}

	ode::RBoolPtr ParametersI::getAcquisitionData() {
	    if (0 != this->theOptions) {
		return this->theOptions->acquisitionData;
	    }
	    return ode::RBoolPtr();
	}


	// Parameters.map
	// ===============================================================

	ParametersIPtr ParametersI::add(const std::string& name,
					const ode::RTypePtr& r) {
	    this->map[name] = r;
	    return this;
	}


        ParametersIPtr ParametersI::addId(Ice::Long id) {
	    return add("id", rlong(id));
        }

	ParametersIPtr ParametersI::addId(const ode::RLongPtr& id) {
	    return add("id", id);
	}

	ParametersIPtr ParametersI::addIds(ode::sys::LongList ids) {
	    return addLongs("ids", ids);
	}

	ParametersIPtr ParametersI::addLong(const std::string& name,
					    Ice::Long l) {
	    return add(name, rlong(l));
	}

	ParametersIPtr ParametersI::addLong(const std::string& name,
					    const ode::RLongPtr& l) {
	    return add(name, l);
	}

	ParametersIPtr ParametersI::addLongs(const std::string& name,
					     ode::sys::LongList longs) {
	    ode::RListPtr list = rlist();
	    ode::sys::LongList::const_iterator beg = longs.begin();
	    ode::sys::LongList::const_iterator end = longs.end();
	    while (beg != end) {
		list->add(rlong(*beg));
		beg++;
	    }
	    this->map[name] = list;
	    return this;
	}


    } // sys

} // ode
