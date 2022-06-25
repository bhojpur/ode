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

#include <ode/ClientErrors.h>
#include <ode/model/PermissionsI.h>
#include <ode/ClientErrors.h>
#include <ode/Constants.h>

::Ice::Object* IceInternal::upCast(::ode::model::PermissionsI* p) { return p; }

namespace ode {

    namespace model {

        PermissionsI::~PermissionsI() {}

        PermissionsI::PermissionsI(const std::string& perms) : Permissions() {
            __immutable = false;
            restrictions = ode::api::BoolArray();
            if (perms.empty()) {
                perm1 = -1L;
            } else {
                perm1 = 0L;
                // Due to lack of regex libraries
                // this implementation is more naive
                // than in the other SDKs
                std::string rest = perms;
                if (rest.size() == 7) {
                    // ignore the locked character
                    rest = rest.substr(1);
                }
                if (rest.size() != 6) {
                    std::string msg = "Bad permissions:";
                    msg += perms;
                    throw ode::ClientError(__FILE__, __LINE__, msg.c_str());
                }
                if (rest[0] == 'r' || rest[0] == 'R') {
                    setUserRead(true);
                }
                if (rest[1] == 'w' || rest[1] == 'W') {
                    setUserWrite(true);
                    setUserAnnotate(true);
                } else if (rest[1] == 'a' || rest[1] == 'A') {
                    setUserAnnotate(true);
                }
                if (rest[2] == 'r' || rest[2] == 'R') {
                    setGroupRead(true);
                }
                if (rest[3] == 'w' || rest[3] == 'W') {
                    setGroupWrite(true);
                    setGroupAnnotate(true);
                } else if (rest[3] == 'a' || rest[3] == 'A') {
                    setGroupAnnotate(true);
                }
                if (rest[4] == 'r' || rest[4] == 'R') {
                    setWorldRead(true);
                }
                if (rest[5] == 'w' || rest[5] == 'W') {
                    setWorldWrite(true);
                    setWorldAnnotate(true);
                } else if (rest[5] == 'a' || rest[5] == 'A') {
                    setWorldAnnotate(true);
                }
            }
        }

        void PermissionsI::ice_postUnmarshal() {
            __immutable = true;
        }

        void PermissionsI::throwIfImmutable() {
            if (__immutable) {
                throw ode::ClientError(__FILE__,__LINE__,"ImmutablePermissions");
            }
        }

        void PermissionsI::unload(const Ice::Current& /* current */) {
            // no-op
        }

        bool PermissionsI::isRestricted(const std::string& restriction, const Ice::Current& /* current */) {
            const ode::api::StringSet::const_iterator beg = extendedRestrictions.begin();
            const ode::api::StringSet::const_iterator end = extendedRestrictions.end();
            return std::find(beg, end, restriction) != end;
        }

        bool PermissionsI::isDisallow(::Ice::Int restriction, const Ice::Current& /* current */) {
            // restriction should be unsigned but that is not what's generated by Ice.
            // Instead for any negative value we return false, since there can be no
            // element in restrictions which is true.
            if (restriction < 0) {
                return false;
            }

            // and if it is positive, then it's safe to cast to unsigned.
            if (restrictions.size() > static_cast<unsigned int>(restriction)) {
                return restrictions[restriction];
            }
            return false;
        }

        bool PermissionsI::canAnnotate(const Ice::Current& /* current */) {
            return ! isDisallow(ode::constants::permissions::ANNOTATERESTRICTION);
        }

        bool PermissionsI::canDelete(const Ice::Current& /* current */) {
            return ! isDisallow(ode::constants::permissions::DELETERESTRICTION);
        }

        bool PermissionsI::canEdit(const Ice::Current& /* current */) {
            return ! isDisallow(ode::constants::permissions::EDITRESTRICTION);
        }

        bool PermissionsI::canLink(const Ice::Current& /* current */) {
            return ! isDisallow(ode::constants::permissions::LINKRESTRICTION);
        }

        bool PermissionsI::canChgrp(const Ice::Current& /* current */) {
            return ! isDisallow(ode::constants::permissions::CHGRPRESTRICTION);
        }

        bool PermissionsI::canChown(const Ice::Current& /* current */) {
            return ! isDisallow(ode::constants::permissions::CHOWNRESTRICTION);
        }

        Ice::Long PermissionsI::getPerm1(const Ice::Current& /* current */) {
            return perm1;
        }

        void PermissionsI::setPerm1(Ice::Long _perm1, const Ice::Current& /* current */) {
            throwIfImmutable();
            perm1 =  _perm1;
        }

        // shift 8; mask 4
        bool PermissionsI::isUserRead(const Ice::Current& /* current */) {
            return granted(4,8);
        }
        void PermissionsI::setUserRead(bool value, const Ice::Current& /* current */) {
            set(4,8, value);
        }

        // shift 8; mask 2
        bool PermissionsI::isUserWrite(const Ice::Current& /* current */) {
            return granted(2,8);
        }
        void PermissionsI::setUserWrite(bool value, const Ice::Current& /* current */) {
            set(2,8, value);
        }

        // shift 8; mask 1
        bool PermissionsI::isUserAnnotate(const Ice::Current& /* current */) {
            return granted(1,8);
        }
        void PermissionsI::setUserAnnotate(bool value, const Ice::Current& /* current */) {
            set(1,8, value);
        }

        // shift 4; mask 4
        bool PermissionsI::isGroupRead(const Ice::Current& /* current */) {
            return granted(4,4);
        }
        void PermissionsI::setGroupRead(bool value, const Ice::Current& /* current */) {
            set(4,4, value);
        }

        // shift 4; mask 2
        bool PermissionsI::isGroupWrite(const Ice::Current& /* current */) {
            return granted(2,4);
        }
        void PermissionsI::setGroupWrite(bool value, const Ice::Current& /* current */) {
            set(2,4, value);
        }

        // shift 4; mask 1
        bool PermissionsI::isGroupAnnotate(const Ice::Current& /* current */) {
            return granted(1,4);
        }
        void PermissionsI::setGroupAnnotate(bool value, const Ice::Current& /* current */) {
            set(1,4, value);
        }

        // shift 0; mask 4
        bool PermissionsI::isWorldRead(const Ice::Current& /* current */) {
            return granted(4,0);
        }
        void PermissionsI::setWorldRead(bool value, const Ice::Current& /* current */) {
            set(4,0, value);
        }

        // shift 0; mask 2
        bool PermissionsI::isWorldWrite(const Ice::Current& /* current */) {
            return granted(2,0);
        }
        void PermissionsI::setWorldWrite(bool value, const Ice::Current& /* current */) {
            set(2,0, value);
        }

        // shift 0; mask 1
        bool PermissionsI::isWorldAnnotate(const Ice::Current& /* current */) {
            return granted(1,0);
        }
        void PermissionsI::setWorldAnnotate(bool value, const Ice::Current& /* current */) {
            set(1,0, value);
        }

        bool PermissionsI::granted(int mask, int shift) {
            return (perm1 & (mask<<shift) ) == (mask<<shift);
        }

        void PermissionsI::set(int mask, int shift, bool on) {
            throwIfImmutable();
            if (on) {
                perm1 = perm1 | ( 0L  | (mask<<shift) );
            } else {
                perm1 = perm1 & ( -1L ^ (mask<<shift) );
            }
        }

    }
} //End ode::model