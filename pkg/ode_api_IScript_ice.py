#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

from sys import version_info as _version_info_
import Ice, IcePy
import ode_ServicesF_ice
import ode_Scripts_ice

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'IScript' not in _M_ode.api.__dict__:
    _M_ode.api.IScript = Ice.createTempClass()
    class IScript(_M_ode.api.ServiceInterface):
        """
        Utility service for managing and launching scripts for execution by the Processor API.
        Typical usage might include (PYTHON):
        {@code
        sf = client.createSession()
        svc = sf.getScriptService()
        scripts = svc.getScripts()
        if len(scripts) >= 1:
        script_id = svc.keys()\[-1]
        else:
        script_id = svc.uploadScript('/test/my_script.py', SCRIPT_TEXT)
        params = svc.getParams(script_id)
        # You will need to parse the params to create the proper input
        inputs = {}
        # The last parameter is how long to wait as an RInt
        proc = svc.runScript(script_id, inputs, None)
        try:
        cb = ode.scripts.ProcessCallbackI(client, proc)
        while not cb.block(1000): # ms.
        pass
        cb.close()
        rv = proc.getResults(0)
        finally:
        proc.close(False)
        }
        See ODE.scripts for more information.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.IScript:
                raise RuntimeError('ode.api.IScript is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::IScript', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::IScript'

        def ice_staticId():
            return '::ode::api::IScript'
        ice_staticId = staticmethod(ice_staticId)

        def getScripts_async(self, _cb, current=None):
            """
            This method returns official server scripts as a list of
            ode.model.OriginalFile objects.
            These scripts will be executed by the server if submitted
            via {@code runScript}. The input parameters
            necessary for proper functioning can be retrieved via
            {@code getParams}.
            The ode.model.OriginalFile#path value can be used
            in other official scripts via the
            language specific import command, since the script
            directory will be placed on the appropriate
            environment path variable.
            {@code
            scripts = scriptService.getScripts()
            for script in scripts:
            text = scriptService.getScriptText(script.id.val)
            # First character is a "/" symbol
            path = script.path.val\[1:\]
            path = path.replace("/",".")
            print "Possible import: %s" % path
            }
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- 
            SecurityViolation -- 
            """
            pass

        def getScriptsByMimetype_async(self, _cb, mimetype, current=None):
            """
            This method returns official server scripts identified
            with the specified extension as a list of
            ode.model.OriginalFile objects.
            These scripts will be executed by the server if submitted
            via {@code runScript}. The input parameters
            necessary for proper functioning can be retrieved via
            {@code getParams}.
            The ode.model.OriginalFile#path value can be used
            in other official scripts via the
            language specific import command, since the script
            directory will be placed on the appropriate
            environment path variable.
            {@code
            scripts = scriptService.getScripts("py")
            for script in scripts:
            text = scriptService.getScriptText(script.id.val)
            path = script.path.val\[1:\] # First symbol is a "/"
            path = path.replace("/",".")
            print "Possible import: %s" % path
            }
            Arguments:
            _cb -- The asynchronous callback object.
            mimetype -- the mimetype identifying the scripts.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- 
            SecurityViolation -- 
            """
            pass

        def getUserScripts_async(self, _cb, acceptsList, current=None):
            """
            Returns non-official scripts which have been uploaded by individual users.
            These scripts will not be run by the server, though a user can
            start a personal usermode processor which will allow the scripts to be
            executed. This is particularly useful for testing new scripts.
            Arguments:
            _cb -- The asynchronous callback object.
            acceptsList -- 
            current -- The Current object for the invocation.
            """
            pass

        def getScriptID_async(self, _cb, path, current=None):
            """
            Get the id of an official script by the script path.
            The script service ensures that all script paths are unique.
            Note: there is no similar method for user scripts (e.g. {@code getUserScriptID})
            since the path is not guaranteed to be unique.
            Arguments:
            _cb -- The asynchronous callback object.
            path -- The name of the script.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- 
            SecurityViolation -- 
            """
            pass

        def getScriptText_async(self, _cb, scriptID, current=None):
            """
            Get the text from the server for the script with given id.
            Arguments:
            _cb -- The asynchronous callback object.
            scriptID -- see above.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- 
            """
            pass

        def uploadScript_async(self, _cb, path, scriptText, current=None):
            """
            Upload a user script to the server and return the id. This method checks that
            a script with that names does not exist and that the script has parameters
            if possible, i.e. a usermode processor is running which for the
            current user.
            Arguments:
            _cb -- The asynchronous callback object.
            path -- see above.
            scriptText -- see above.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- 
            SecurityViolation -- 
            """
            pass

        def uploadOfficialScript_async(self, _cb, path, scriptText, current=None):
            """
            Like {@code uploadScript} but is only callable by
            administrators. The parameters for the script are also
            checked.
            Arguments:
            _cb -- The asynchronous callback object.
            path -- 
            scriptText -- 
            current -- The Current object for the invocation.
            """
            pass

        def editScript_async(self, _cb, fileObject, scriptText, current=None):
            """
            Modify the text for the given script object.
            Arguments:
            _cb -- The asynchronous callback object.
            fileObject -- see above.
            scriptText -- see above.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- 
            SecurityViolation -- 
            """
            pass

        def getScriptWithDetails_async(self, _cb, scriptID, current=None):
            """
            Get the script from the server with details from OriginalFile
            Arguments:
            _cb -- The asynchronous callback object.
            scriptID -- see above
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- 
            """
            pass

        def getParams_async(self, _cb, scriptID, current=None):
            """
            Get the parameters that the script takes and returns, along with
            other metadata available from the script.
            Arguments:
            _cb -- The asynchronous callback object.
            scriptID -- see above.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- 
            """
            pass

        def deleteScript_async(self, _cb, scriptID, current=None):
            """
            Delete the script on the server with id. The file will also be removed from disk.
            Arguments:
            _cb -- The asynchronous callback object.
            scriptID -- Id of the script to delete.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- 
            SecurityViolation -- 
            """
            pass

        def runScript_async(self, _cb, scriptID, inputs, waitSecs, current=None):
            """
            If ode.ResourceError is thrown, then no
            {@code Processor} is available. Use {@code scheduleJob}
            to create a ode.model.ScriptJob in the
            Waiting state. A {@code Processor} may become
            available.
            {@code
            try:
            proc = scriptService.runScript(1, {}, None)
            except ResourceError:
            job = scriptService.scheduleScript(1, {}, None)
            }
            The {@code ScriptProcess} proxy MUST be closed before
            exiting. If you would like the script execution to continue
            in the background, pass True as the argument.
            {@code
            try:
            proc.poll()         # See if process is finished
            finally:
            proc.close(True)    # Detach and execution can continue
            # proc.close(False) # OR script is immediately stopped.
            }
            Arguments:
            _cb -- The asynchronous callback object.
            scriptID -- 
            inputs -- 
            waitSecs -- 
            current -- The Current object for the invocation.
            """
            pass

        def canRunScript_async(self, _cb, scriptID, current=None):
            """
            Returns true if there is a processor which will run the
            given script.
            Either the script is an official script and this method
            will return true (though an individual invocation may fail
            with an ode.ResourceError for some reason)
            or this is a user script, and a usermode processor
            must be active which takes the scripts user or group.
            Arguments:
            _cb -- The asynchronous callback object.
            scriptID -- 
            current -- The Current object for the invocation.
            """
            pass

        def validateScript_async(self, _cb, j, acceptsList, current=None):
            """
            Used internally by processor.py to check if the script
            attached to the ode.model.Job has a valid script
            attached, based on the {@code acceptsList} and the current
            security context.
            An example of an acceptsList might be Experimenter(myUserId, False), meaning that
            only scripts belonging to me should be trusted. An empty list implies that the server should
            return what it would by default trust.
            A valid script will be returned if it exists; otherwise null.
            Arguments:
            _cb -- The asynchronous callback object.
            j -- 
            acceptsList -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_IScript)

        __repr__ = __str__

    _M_ode.api.IScriptPrx = Ice.createTempClass()
    class IScriptPrx(_M_ode.api.ServiceInterfacePrx):

        """
        This method returns official server scripts as a list of
        ode.model.OriginalFile objects.
        These scripts will be executed by the server if submitted
        via {@code runScript}. The input parameters
        necessary for proper functioning can be retrieved via
        {@code getParams}.
        The ode.model.OriginalFile#path value can be used
        in other official scripts via the
        language specific import command, since the script
        directory will be placed on the appropriate
        environment path variable.
        {@code
        scripts = scriptService.getScripts()
        for script in scripts:
        text = scriptService.getScriptText(script.id.val)
        # First character is a "/" symbol
        path = script.path.val\[1:\]
        path = path.replace("/",".")
        print "Possible import: %s" % path
        }
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: see above.
        Throws:
        ApiUsageException -- 
        SecurityViolation -- 
        """
        def getScripts(self, _ctx=None):
            return _M_ode.api.IScript._op_getScripts.invoke(self, ((), _ctx))

        """
        This method returns official server scripts as a list of
        ode.model.OriginalFile objects.
        These scripts will be executed by the server if submitted
        via {@code runScript}. The input parameters
        necessary for proper functioning can be retrieved via
        {@code getParams}.
        The ode.model.OriginalFile#path value can be used
        in other official scripts via the
        language specific import command, since the script
        directory will be placed on the appropriate
        environment path variable.
        {@code
        scripts = scriptService.getScripts()
        for script in scripts:
        text = scriptService.getScriptText(script.id.val)
        # First character is a "/" symbol
        path = script.path.val\[1:\]
        path = path.replace("/",".")
        print "Possible import: %s" % path
        }
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getScripts(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IScript._op_getScripts.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        This method returns official server scripts as a list of
        ode.model.OriginalFile objects.
        These scripts will be executed by the server if submitted
        via {@code runScript}. The input parameters
        necessary for proper functioning can be retrieved via
        {@code getParams}.
        The ode.model.OriginalFile#path value can be used
        in other official scripts via the
        language specific import command, since the script
        directory will be placed on the appropriate
        environment path variable.
        {@code
        scripts = scriptService.getScripts()
        for script in scripts:
        text = scriptService.getScriptText(script.id.val)
        # First character is a "/" symbol
        path = script.path.val\[1:\]
        path = path.replace("/",".")
        print "Possible import: %s" % path
        }
        Arguments:
        Returns: see above.
        Throws:
        ApiUsageException -- 
        SecurityViolation -- 
        """
        def end_getScripts(self, _r):
            return _M_ode.api.IScript._op_getScripts.end(self, _r)

        """
        This method returns official server scripts identified
        with the specified extension as a list of
        ode.model.OriginalFile objects.
        These scripts will be executed by the server if submitted
        via {@code runScript}. The input parameters
        necessary for proper functioning can be retrieved via
        {@code getParams}.
        The ode.model.OriginalFile#path value can be used
        in other official scripts via the
        language specific import command, since the script
        directory will be placed on the appropriate
        environment path variable.
        {@code
        scripts = scriptService.getScripts("py")
        for script in scripts:
        text = scriptService.getScriptText(script.id.val)
        path = script.path.val\[1:\] # First symbol is a "/"
        path = path.replace("/",".")
        print "Possible import: %s" % path
        }
        Arguments:
        mimetype -- the mimetype identifying the scripts.
        _ctx -- The request context for the invocation.
        Returns: see above.
        Throws:
        ApiUsageException -- 
        SecurityViolation -- 
        """
        def getScriptsByMimetype(self, mimetype, _ctx=None):
            return _M_ode.api.IScript._op_getScriptsByMimetype.invoke(self, ((mimetype, ), _ctx))

        """
        This method returns official server scripts identified
        with the specified extension as a list of
        ode.model.OriginalFile objects.
        These scripts will be executed by the server if submitted
        via {@code runScript}. The input parameters
        necessary for proper functioning can be retrieved via
        {@code getParams}.
        The ode.model.OriginalFile#path value can be used
        in other official scripts via the
        language specific import command, since the script
        directory will be placed on the appropriate
        environment path variable.
        {@code
        scripts = scriptService.getScripts("py")
        for script in scripts:
        text = scriptService.getScriptText(script.id.val)
        path = script.path.val\[1:\] # First symbol is a "/"
        path = path.replace("/",".")
        print "Possible import: %s" % path
        }
        Arguments:
        mimetype -- the mimetype identifying the scripts.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getScriptsByMimetype(self, mimetype, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IScript._op_getScriptsByMimetype.begin(self, ((mimetype, ), _response, _ex, _sent, _ctx))

        """
        This method returns official server scripts identified
        with the specified extension as a list of
        ode.model.OriginalFile objects.
        These scripts will be executed by the server if submitted
        via {@code runScript}. The input parameters
        necessary for proper functioning can be retrieved via
        {@code getParams}.
        The ode.model.OriginalFile#path value can be used
        in other official scripts via the
        language specific import command, since the script
        directory will be placed on the appropriate
        environment path variable.
        {@code
        scripts = scriptService.getScripts("py")
        for script in scripts:
        text = scriptService.getScriptText(script.id.val)
        path = script.path.val\[1:\] # First symbol is a "/"
        path = path.replace("/",".")
        print "Possible import: %s" % path
        }
        Arguments:
        mimetype -- the mimetype identifying the scripts.
        Returns: see above.
        Throws:
        ApiUsageException -- 
        SecurityViolation -- 
        """
        def end_getScriptsByMimetype(self, _r):
            return _M_ode.api.IScript._op_getScriptsByMimetype.end(self, _r)

        """
        Returns non-official scripts which have been uploaded by individual users.
        These scripts will not be run by the server, though a user can
        start a personal usermode processor which will allow the scripts to be
        executed. This is particularly useful for testing new scripts.
        Arguments:
        acceptsList -- 
        _ctx -- The request context for the invocation.
        """
        def getUserScripts(self, acceptsList, _ctx=None):
            return _M_ode.api.IScript._op_getUserScripts.invoke(self, ((acceptsList, ), _ctx))

        """
        Returns non-official scripts which have been uploaded by individual users.
        These scripts will not be run by the server, though a user can
        start a personal usermode processor which will allow the scripts to be
        executed. This is particularly useful for testing new scripts.
        Arguments:
        acceptsList -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getUserScripts(self, acceptsList, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IScript._op_getUserScripts.begin(self, ((acceptsList, ), _response, _ex, _sent, _ctx))

        """
        Returns non-official scripts which have been uploaded by individual users.
        These scripts will not be run by the server, though a user can
        start a personal usermode processor which will allow the scripts to be
        executed. This is particularly useful for testing new scripts.
        Arguments:
        acceptsList -- 
        """
        def end_getUserScripts(self, _r):
            return _M_ode.api.IScript._op_getUserScripts.end(self, _r)

        """
        Get the id of an official script by the script path.
        The script service ensures that all script paths are unique.
        Note: there is no similar method for user scripts (e.g. {@code getUserScriptID})
        since the path is not guaranteed to be unique.
        Arguments:
        path -- The name of the script.
        _ctx -- The request context for the invocation.
        Returns: see above.
        Throws:
        ApiUsageException -- 
        SecurityViolation -- 
        """
        def getScriptID(self, path, _ctx=None):
            return _M_ode.api.IScript._op_getScriptID.invoke(self, ((path, ), _ctx))

        """
        Get the id of an official script by the script path.
        The script service ensures that all script paths are unique.
        Note: there is no similar method for user scripts (e.g. {@code getUserScriptID})
        since the path is not guaranteed to be unique.
        Arguments:
        path -- The name of the script.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getScriptID(self, path, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IScript._op_getScriptID.begin(self, ((path, ), _response, _ex, _sent, _ctx))

        """
        Get the id of an official script by the script path.
        The script service ensures that all script paths are unique.
        Note: there is no similar method for user scripts (e.g. {@code getUserScriptID})
        since the path is not guaranteed to be unique.
        Arguments:
        path -- The name of the script.
        Returns: see above.
        Throws:
        ApiUsageException -- 
        SecurityViolation -- 
        """
        def end_getScriptID(self, _r):
            return _M_ode.api.IScript._op_getScriptID.end(self, _r)

        """
        Get the text from the server for the script with given id.
        Arguments:
        scriptID -- see above.
        _ctx -- The request context for the invocation.
        Returns: see above.
        Throws:
        ApiUsageException -- 
        """
        def getScriptText(self, scriptID, _ctx=None):
            return _M_ode.api.IScript._op_getScriptText.invoke(self, ((scriptID, ), _ctx))

        """
        Get the text from the server for the script with given id.
        Arguments:
        scriptID -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getScriptText(self, scriptID, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IScript._op_getScriptText.begin(self, ((scriptID, ), _response, _ex, _sent, _ctx))

        """
        Get the text from the server for the script with given id.
        Arguments:
        scriptID -- see above.
        Returns: see above.
        Throws:
        ApiUsageException -- 
        """
        def end_getScriptText(self, _r):
            return _M_ode.api.IScript._op_getScriptText.end(self, _r)

        """
        Upload a user script to the server and return the id. This method checks that
        a script with that names does not exist and that the script has parameters
        if possible, i.e. a usermode processor is running which for the
        current user.
        Arguments:
        path -- see above.
        scriptText -- see above.
        _ctx -- The request context for the invocation.
        Returns: The new id of the script.
        Throws:
        ApiUsageException -- 
        SecurityViolation -- 
        """
        def uploadScript(self, path, scriptText, _ctx=None):
            return _M_ode.api.IScript._op_uploadScript.invoke(self, ((path, scriptText), _ctx))

        """
        Upload a user script to the server and return the id. This method checks that
        a script with that names does not exist and that the script has parameters
        if possible, i.e. a usermode processor is running which for the
        current user.
        Arguments:
        path -- see above.
        scriptText -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_uploadScript(self, path, scriptText, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IScript._op_uploadScript.begin(self, ((path, scriptText), _response, _ex, _sent, _ctx))

        """
        Upload a user script to the server and return the id. This method checks that
        a script with that names does not exist and that the script has parameters
        if possible, i.e. a usermode processor is running which for the
        current user.
        Arguments:
        path -- see above.
        scriptText -- see above.
        Returns: The new id of the script.
        Throws:
        ApiUsageException -- 
        SecurityViolation -- 
        """
        def end_uploadScript(self, _r):
            return _M_ode.api.IScript._op_uploadScript.end(self, _r)

        """
        Like {@code uploadScript} but is only callable by
        administrators. The parameters for the script are also
        checked.
        Arguments:
        path -- 
        scriptText -- 
        _ctx -- The request context for the invocation.
        """
        def uploadOfficialScript(self, path, scriptText, _ctx=None):
            return _M_ode.api.IScript._op_uploadOfficialScript.invoke(self, ((path, scriptText), _ctx))

        """
        Like {@code uploadScript} but is only callable by
        administrators. The parameters for the script are also
        checked.
        Arguments:
        path -- 
        scriptText -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_uploadOfficialScript(self, path, scriptText, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IScript._op_uploadOfficialScript.begin(self, ((path, scriptText), _response, _ex, _sent, _ctx))

        """
        Like {@code uploadScript} but is only callable by
        administrators. The parameters for the script are also
        checked.
        Arguments:
        path -- 
        scriptText -- 
        """
        def end_uploadOfficialScript(self, _r):
            return _M_ode.api.IScript._op_uploadOfficialScript.end(self, _r)

        """
        Modify the text for the given script object.
        Arguments:
        fileObject -- see above.
        scriptText -- see above.
        _ctx -- The request context for the invocation.
        Throws:
        ApiUsageException -- 
        SecurityViolation -- 
        """
        def editScript(self, fileObject, scriptText, _ctx=None):
            return _M_ode.api.IScript._op_editScript.invoke(self, ((fileObject, scriptText), _ctx))

        """
        Modify the text for the given script object.
        Arguments:
        fileObject -- see above.
        scriptText -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_editScript(self, fileObject, scriptText, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IScript._op_editScript.begin(self, ((fileObject, scriptText), _response, _ex, _sent, _ctx))

        """
        Modify the text for the given script object.
        Arguments:
        fileObject -- see above.
        scriptText -- see above.
        Throws:
        ApiUsageException -- 
        SecurityViolation -- 
        """
        def end_editScript(self, _r):
            return _M_ode.api.IScript._op_editScript.end(self, _r)

        """
        Get the script from the server with details from OriginalFile
        Arguments:
        scriptID -- see above
        _ctx -- The request context for the invocation.
        Returns: see above
        Throws:
        ApiUsageException -- 
        """
        def getScriptWithDetails(self, scriptID, _ctx=None):
            return _M_ode.api.IScript._op_getScriptWithDetails.invoke(self, ((scriptID, ), _ctx))

        """
        Get the script from the server with details from OriginalFile
        Arguments:
        scriptID -- see above
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getScriptWithDetails(self, scriptID, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IScript._op_getScriptWithDetails.begin(self, ((scriptID, ), _response, _ex, _sent, _ctx))

        """
        Get the script from the server with details from OriginalFile
        Arguments:
        scriptID -- see above
        Returns: see above
        Throws:
        ApiUsageException -- 
        """
        def end_getScriptWithDetails(self, _r):
            return _M_ode.api.IScript._op_getScriptWithDetails.end(self, _r)

        """
        Get the parameters that the script takes and returns, along with
        other metadata available from the script.
        Arguments:
        scriptID -- see above.
        _ctx -- The request context for the invocation.
        Returns: see above.
        Throws:
        ApiUsageException -- 
        """
        def getParams(self, scriptID, _ctx=None):
            return _M_ode.api.IScript._op_getParams.invoke(self, ((scriptID, ), _ctx))

        """
        Get the parameters that the script takes and returns, along with
        other metadata available from the script.
        Arguments:
        scriptID -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getParams(self, scriptID, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IScript._op_getParams.begin(self, ((scriptID, ), _response, _ex, _sent, _ctx))

        """
        Get the parameters that the script takes and returns, along with
        other metadata available from the script.
        Arguments:
        scriptID -- see above.
        Returns: see above.
        Throws:
        ApiUsageException -- 
        """
        def end_getParams(self, _r):
            return _M_ode.api.IScript._op_getParams.end(self, _r)

        """
        Delete the script on the server with id. The file will also be removed from disk.
        Arguments:
        scriptID -- Id of the script to delete.
        _ctx -- The request context for the invocation.
        Throws:
        ApiUsageException -- 
        SecurityViolation -- 
        """
        def deleteScript(self, scriptID, _ctx=None):
            return _M_ode.api.IScript._op_deleteScript.invoke(self, ((scriptID, ), _ctx))

        """
        Delete the script on the server with id. The file will also be removed from disk.
        Arguments:
        scriptID -- Id of the script to delete.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_deleteScript(self, scriptID, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IScript._op_deleteScript.begin(self, ((scriptID, ), _response, _ex, _sent, _ctx))

        """
        Delete the script on the server with id. The file will also be removed from disk.
        Arguments:
        scriptID -- Id of the script to delete.
        Throws:
        ApiUsageException -- 
        SecurityViolation -- 
        """
        def end_deleteScript(self, _r):
            return _M_ode.api.IScript._op_deleteScript.end(self, _r)

        """
        If ode.ResourceError is thrown, then no
        {@code Processor} is available. Use {@code scheduleJob}
        to create a ode.model.ScriptJob in the
        Waiting state. A {@code Processor} may become
        available.
        {@code
        try:
        proc = scriptService.runScript(1, {}, None)
        except ResourceError:
        job = scriptService.scheduleScript(1, {}, None)
        }
        The {@code ScriptProcess} proxy MUST be closed before
        exiting. If you would like the script execution to continue
        in the background, pass True as the argument.
        {@code
        try:
        proc.poll()         # See if process is finished
        finally:
        proc.close(True)    # Detach and execution can continue
        # proc.close(False) # OR script is immediately stopped.
        }
        Arguments:
        scriptID -- 
        inputs -- 
        waitSecs -- 
        _ctx -- The request context for the invocation.
        """
        def runScript(self, scriptID, inputs, waitSecs, _ctx=None):
            return _M_ode.api.IScript._op_runScript.invoke(self, ((scriptID, inputs, waitSecs), _ctx))

        """
        If ode.ResourceError is thrown, then no
        {@code Processor} is available. Use {@code scheduleJob}
        to create a ode.model.ScriptJob in the
        Waiting state. A {@code Processor} may become
        available.
        {@code
        try:
        proc = scriptService.runScript(1, {}, None)
        except ResourceError:
        job = scriptService.scheduleScript(1, {}, None)
        }
        The {@code ScriptProcess} proxy MUST be closed before
        exiting. If you would like the script execution to continue
        in the background, pass True as the argument.
        {@code
        try:
        proc.poll()         # See if process is finished
        finally:
        proc.close(True)    # Detach and execution can continue
        # proc.close(False) # OR script is immediately stopped.
        }
        Arguments:
        scriptID -- 
        inputs -- 
        waitSecs -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_runScript(self, scriptID, inputs, waitSecs, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IScript._op_runScript.begin(self, ((scriptID, inputs, waitSecs), _response, _ex, _sent, _ctx))

        """
        If ode.ResourceError is thrown, then no
        {@code Processor} is available. Use {@code scheduleJob}
        to create a ode.model.ScriptJob in the
        Waiting state. A {@code Processor} may become
        available.
        {@code
        try:
        proc = scriptService.runScript(1, {}, None)
        except ResourceError:
        job = scriptService.scheduleScript(1, {}, None)
        }
        The {@code ScriptProcess} proxy MUST be closed before
        exiting. If you would like the script execution to continue
        in the background, pass True as the argument.
        {@code
        try:
        proc.poll()         # See if process is finished
        finally:
        proc.close(True)    # Detach and execution can continue
        # proc.close(False) # OR script is immediately stopped.
        }
        Arguments:
        scriptID -- 
        inputs -- 
        waitSecs -- 
        """
        def end_runScript(self, _r):
            return _M_ode.api.IScript._op_runScript.end(self, _r)

        """
        Returns true if there is a processor which will run the
        given script.
        Either the script is an official script and this method
        will return true (though an individual invocation may fail
        with an ode.ResourceError for some reason)
        or this is a user script, and a usermode processor
        must be active which takes the scripts user or group.
        Arguments:
        scriptID -- 
        _ctx -- The request context for the invocation.
        """
        def canRunScript(self, scriptID, _ctx=None):
            return _M_ode.api.IScript._op_canRunScript.invoke(self, ((scriptID, ), _ctx))

        """
        Returns true if there is a processor which will run the
        given script.
        Either the script is an official script and this method
        will return true (though an individual invocation may fail
        with an ode.ResourceError for some reason)
        or this is a user script, and a usermode processor
        must be active which takes the scripts user or group.
        Arguments:
        scriptID -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_canRunScript(self, scriptID, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IScript._op_canRunScript.begin(self, ((scriptID, ), _response, _ex, _sent, _ctx))

        """
        Returns true if there is a processor which will run the
        given script.
        Either the script is an official script and this method
        will return true (though an individual invocation may fail
        with an ode.ResourceError for some reason)
        or this is a user script, and a usermode processor
        must be active which takes the scripts user or group.
        Arguments:
        scriptID -- 
        """
        def end_canRunScript(self, _r):
            return _M_ode.api.IScript._op_canRunScript.end(self, _r)

        """
        Used internally by processor.py to check if the script
        attached to the ode.model.Job has a valid script
        attached, based on the {@code acceptsList} and the current
        security context.
        An example of an acceptsList might be Experimenter(myUserId, False), meaning that
        only scripts belonging to me should be trusted. An empty list implies that the server should
        return what it would by default trust.
        A valid script will be returned if it exists; otherwise null.
        Arguments:
        j -- 
        acceptsList -- 
        _ctx -- The request context for the invocation.
        """
        def validateScript(self, j, acceptsList, _ctx=None):
            return _M_ode.api.IScript._op_validateScript.invoke(self, ((j, acceptsList), _ctx))

        """
        Used internally by processor.py to check if the script
        attached to the ode.model.Job has a valid script
        attached, based on the {@code acceptsList} and the current
        security context.
        An example of an acceptsList might be Experimenter(myUserId, False), meaning that
        only scripts belonging to me should be trusted. An empty list implies that the server should
        return what it would by default trust.
        A valid script will be returned if it exists; otherwise null.
        Arguments:
        j -- 
        acceptsList -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_validateScript(self, j, acceptsList, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IScript._op_validateScript.begin(self, ((j, acceptsList), _response, _ex, _sent, _ctx))

        """
        Used internally by processor.py to check if the script
        attached to the ode.model.Job has a valid script
        attached, based on the {@code acceptsList} and the current
        security context.
        An example of an acceptsList might be Experimenter(myUserId, False), meaning that
        only scripts belonging to me should be trusted. An empty list implies that the server should
        return what it would by default trust.
        A valid script will be returned if it exists; otherwise null.
        Arguments:
        j -- 
        acceptsList -- 
        """
        def end_validateScript(self, _r):
            return _M_ode.api.IScript._op_validateScript.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.IScriptPrx.ice_checkedCast(proxy, '::ode::api::IScript', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.IScriptPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::IScript'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_IScriptPrx = IcePy.defineProxy('::ode::api::IScript', IScriptPrx)

    _M_ode.api._t_IScript = IcePy.defineClass('::ode::api::IScript', IScript, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    IScript._ice_type = _M_ode.api._t_IScript

    IScript._op_getScripts = IcePy.Operation('getScripts', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_OriginalFileList, False, 0), (_M_ode._t_ServerError,))
    IScript._op_getScriptsByMimetype = IcePy.Operation('getScriptsByMimetype', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.api._t_OriginalFileList, False, 0), (_M_ode._t_ServerError,))
    IScript._op_getUserScripts = IcePy.Operation('getUserScripts', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.api._t_IObjectList, False, 0),), (), ((), _M_ode.api._t_OriginalFileList, False, 0), (_M_ode._t_ServerError,))
    IScript._op_getScriptID = IcePy.Operation('getScriptID', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    IScript._op_getScriptText = IcePy.Operation('getScriptText', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))
    IScript._op_uploadScript = IcePy.Operation('uploadScript', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    IScript._op_uploadOfficialScript = IcePy.Operation('uploadOfficialScript', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    IScript._op_editScript = IcePy.Operation('editScript', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_OriginalFile, False, 0), ((), IcePy._t_string, False, 0)), (), None, (_M_ode._t_ServerError,))
    IScript._op_getScriptWithDetails = IcePy.Operation('getScriptWithDetails', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode._t_RTypeDict, False, 0), (_M_ode._t_ServerError,))
    IScript._op_getParams = IcePy.Operation('getParams', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.grid._t_JobParams, False, 0), (_M_ode._t_ServerError,))
    IScript._op_deleteScript = IcePy.Operation('deleteScript', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0),), (), None, (_M_ode._t_ServerError,))
    IScript._op_runScript = IcePy.Operation('runScript', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode._t_RTypeDict, False, 0), ((), _M_ode._t_RInt, False, 0)), (), ((), _M_ode.grid._t_ScriptProcessPrx, False, 0), (_M_ode._t_ServerError,))
    IScript._op_canRunScript = IcePy.Operation('canRunScript', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    IScript._op_validateScript = IcePy.Operation('validateScript', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_Job, False, 0), ((), _M_ode.api._t_IObjectList, False, 0)), (), ((), _M_ode.model._t_OriginalFile, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.IScript = IScript
    del IScript

    _M_ode.api.IScriptPrx = IScriptPrx
    del IScriptPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
