package ode.formats.importer.exclusions;

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

import ode.formats.importer.ImportContainer;
import ode.ServerError;
import ode.api.ServiceFactoryPrx;

/**
 * Voter given the opportunity by the {@link ode.formats.importer.ImportLibrary}
 * to cancel the importing of a given file.
 */
public interface FileExclusion {

   /**
    * Given the current {@link ServiceFactoryPrx session} attempt to discover
    * if the given {@link ImportContainer container} should be skipped, i.e.
    * not imported.
    * @return
    *   null if no suggestion is being made. false if this implementation
    *   suggests skipping the given container, otherwise true.
    */
    Boolean
    suggestExclusion(ServiceFactoryPrx factory, ImportContainer container)
            throws ServerError;

}