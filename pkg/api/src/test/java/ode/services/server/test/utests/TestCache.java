package ode.services.server.test.utests;

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

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.event.RegisteredEventListeners;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

public class TestCache extends Cache {

	static CacheManager ehMgr = new CacheManager();
	static volatile int count = 1;
	
//	super("test", 10 /* elts */, MemoryStoreEvictionPolicy.LFU,
//			false /* disk */, null /* path */, true /* eternal */,
//			10 /* time to live */, 10 /* time to idle */,
//			false /* disk persistent */, 10 /* disk thread interval */,
//			null /* listeners */);
	
	public TestCache() {
		this("testcache"+count++, 
				10 /* elts */, 10 /* time to live */, 
				10 /* time to idle */, null /* listeners */);
	}

	public TestCache(String name, int elements, int timeToLive, int timeToIdle, 
			RegisteredEventListeners listeners) {
		super(name, elements, MemoryStoreEvictionPolicy.LFU,
				false /* disk */, null /* path */, true /* eternal */,
				timeToLive, timeToIdle,
				false /* disk persistent */, 10 /* disk thread interval */,
				listeners);
		ehMgr.addCache(this);
	}

}