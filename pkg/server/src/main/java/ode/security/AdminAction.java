package ode.security;

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

/**
 * action for passing to {@link SecuritySystem#runAsAdmin(AdminAction)}. All
 * external input should be <em>carefully</em> checked or even better copied
 * before being passed to this method. A common idiom would be: <code>
 *   public void someApiMethod(IObject target, String someValue)
 *   {
 *         	AdminAction action = new AdminAction(){
 *   		public void runAsAdmin() {
 *   	    	IObject copy = iQuery.get( iObject.getClass(), iObject.getId() );
 *   	    	copy.setValue( someValue );
 *   	    	iUpdate.saveObject(copy);    
 *   		}
 *   	};
 *   }
 * </code>
 */
public interface AdminAction {
    /**
     * executes with special privileges within the {@link SecuritySystem}.
     * 
     * @see SecuritySystem#runAsAdmin(AdminAction)
     */
    void runAsAdmin();

}