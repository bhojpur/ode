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

#ifndef ODE_FIXTURE_H
#define ODE_FIXTURE_H

// domain
#include <ode/all.h>
#include <ode/client.h>
#include <ode/RTypesI.h>
#include <ode/ClientErrors.h>
#include <ode/ServerErrors.h>
#include <ode/model/ImageI.h>
#include <ode/model/PixelsI.h>
#include <ode/model/ExperimenterI.h>

// gtest
#include <safegtest.h>

#ifdef LINUX
// stackframe
#include <execinfo.h>
#endif

// std
#include <exception>
#include <cstdlib>
#include <string>
#include <vector>

#ifndef ODE_TEST
#   ifdef ODE_TEST_EXPORTS
#       define ODE_TEST ICE_DECLSPEC_EXPORT
#   else
#       define ODE_TEST ICE_DECLSPEC_IMPORT
#   endif
#endif

ODE_TEST ode::model::ImagePtr new_ImageI();

struct ODE_TEST Fixture
{
    protected:
        std::vector<ode::client*> clients;
    public:

        /**
         * Creates and fills the "root" client_ptr
         * for later use. This will be closed during
         * destruction or logout.
         */
        Fixture();

        // Fields which should always be present
        ode::client_ptr root;
        ode::client_ptr client;

        // Data graphs
        ode::model::PixelsIPtr pixels();

        // Semi-working debugging tools
        void show_stackframe();
        void printUnexpected();

        /*
         *
         */
        std::string uuid();

        /*
         * Create a new ode::client object by logging
         * in with the username and password. For most
         * users created via Fixture::newUser no password
         * is necessary. The created login will be stored
         * as "client".
         */
        void login(
                const std::string& username = std::string(),
                const std::string& password = std::string());

        /*
         * Like login(username, password) but takes an experimenter
         * object and calls ->getOdeName()->getValue() for you.
         */
        void login(
                const ode::model::ExperimenterPtr& user,
                const std::string& password = std::string());

        /**
         * Close and null the "root" and "client" fields.
         */
        void logout();

        /*
         *
         */
        ode::model::ExperimenterPtr newUser(
                const ode::model::ExperimenterGroupPtr& g = ode::model::ExperimenterGroupPtr());

        /*
         *
         */
        ode::model::ExperimenterGroupPtr newGroup(const std::string& perms = "");

        /*
         *
         */
        void addExperimenter(
                const ode::model::ExperimenterGroupPtr& group,
                const ode::model::ExperimenterPtr& user);
};

#endif // ODE_FIXTURE_H
