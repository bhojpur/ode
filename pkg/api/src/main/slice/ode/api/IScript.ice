/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_API_ISCRIPT_ICE
#define ODE_API_ISCRIPT_ICE

#include <ode/ServicesF.ice>
#include <ode/Scripts.ice>

module ode {

    module api {

        /**
         * Utility service for managing and launching scripts for execution by the Processor API.
         *
         * Typical usage might include (PYTHON):
         * <pre>
         * {@code
         * sf = client.createSession()
         * svc = sf.getScriptService()
         * scripts = svc.getScripts()
         *
         * if len(scripts) >= 1:
         *   script_id = svc.keys()\[-1]
         * else:
         *   script_id = svc.uploadScript('/test/my_script.py', SCRIPT_TEXT)
         *
         * params = svc.getParams(script_id)
         *
         * # You will need to parse the params to create the proper input
         * inputs = {}
         *
         * # The last parameter is how long to wait as an RInt
         * proc = svc.runScript(script_id, inputs, None)
         * try:
         *     cb = ode.scripts.ProcessCallbackI(client, proc)
         *     while not cb.block(1000): # ms.
         *         pass
         *     cb.close()
         *     rv = proc.getResults(0)
         * finally:
         *     proc.close(False)
         * }
         * </pre>
         * See <a href="https://docs.bhojpur.net/latest/ode/developers/scripts/">ODE.scripts</a> for more information.
         **/
        ["ami","amd"] interface IScript extends ServiceInterface
            {
                //
                // Script management
                //

                /**
                 * This method returns official server scripts as a list of
                 * {@link ode.model.OriginalFile} objects.
                 * These scripts will be executed by the server if submitted
                 * via {@code runScript}. The input parameters
                 * necessary for proper functioning can be retrieved via
                 * {@code getParams}.
                 *
                 * The {@link ode.model.OriginalFile#path} value can be used
                 * in other official scripts via the
                 * language specific import command, since the script
                 * directory will be placed on the appropriate
                 * environment path variable.
                 * <pre>
                 * {@code
                 * scripts = scriptService.getScripts()
                 * for script in scripts:
                 *     text = scriptService.getScriptText(script.id.val)
                 *     # First character is a "/" symbol
                 *     path = script.path.val\[1:\]
                 *     path = path.replace("/",".")
                 *     print "Possible import: %s" % path
                 * }
                 * </pre>
                 *
                 * @return see above.
                 * @throws ApiUsageException
                 * @throws SecurityViolation
                 */
                idempotent OriginalFileList getScripts() throws ServerError;

                /**
                 * This method returns official server scripts identified
                 * with the specified extension as a list of
                 * {@link ode.model.OriginalFile} objects.
                 * These scripts will be executed by the server if submitted
                 * via {@code runScript}. The input parameters
                 * necessary for proper functioning can be retrieved via
                 * {@code getParams}.
                 *
                 * The {@link ode.model.OriginalFile#path} value can be used
                 * in other official scripts via the
                 * language specific import command, since the script
                 * directory will be placed on the appropriate
                 * environment path variable.
                 * <pre>
                 * {@code
                 * scripts = scriptService.getScripts("py")
                 * for script in scripts:
                 *     text = scriptService.getScriptText(script.id.val)
                 *     path = script.path.val\[1:\] # First symbol is a "/"
                 *     path = path.replace("/",".")
                 *     print "Possible import: %s" % path
                 * }
                 * </pre>
                 *
                 * @param mimetype the mimetype identifying the scripts.
                 * @return see above.
                 * @throws ApiUsageException
                 * @throws SecurityViolation
                 */
                idempotent OriginalFileList getScriptsByMimetype(string mimetype) throws ServerError;

                /**
                 * Returns non-official scripts which have been uploaded by individual users.
                 * These scripts will <em>not</me> be run by the server, though a user can
                 * start a personal <i>usermode processor</i> which will allow the scripts to be
                 * executed. This is particularly useful for testing new scripts.
                 */
                idempotent OriginalFileList getUserScripts(IObjectList acceptsList) throws ServerError;

                /**
                 * Get the id of an official script by the script path.
                 * The script service ensures that all script paths are unique.
                 *
                 * Note: there is no similar method for user scripts (e.g. {@code getUserScriptID})
                 * since the path is not guaranteed to be unique.
                 *
                 * @param path The name of the script.
                 * @return see above.
                 * @throws ApiUsageException
                 * @throws SecurityViolation
                 */
                idempotent long getScriptID(string path) throws  ServerError;

                /**
                 * Get the text from the server for the script with given id.
                 *
                 * @param scriptID see above.
                 * @return see above.
                 * @throws ApiUsageException
                 */
                idempotent string getScriptText(long scriptID) throws ServerError;

                /**
                 * Upload a user script to the server and return the id. This method checks that
                 * a script with that names does not exist and that the script has parameters
                 * <em>if possible</em>, i.e. a usermode processor is running which for the
                 * current user.
                 *
                 * @param path see above.
                 * @param scriptText see above.
                 * @return The new id of the script.
                 * @throws ApiUsageException
                 * @throws SecurityViolation
                 */
                long uploadScript(string path, string scriptText) throws ServerError;

                /**
                 * Like {@code uploadScript} but is only callable by
                 * administrators. The parameters for the script are also
                 * checked.
                 **/
                long uploadOfficialScript(string path, string scriptText) throws ServerError;

                /**
                 * Modify the text for the given script object.
                 *
                 * @param fileObject see above.
                 * @param scriptText see above.
                 * @throws ApiUsageException
                 * @throws SecurityViolation
                 */
                void editScript(ode::model::OriginalFile fileObject, string scriptText) throws ServerError;

                /**
                 * Get the script from the server with details from OriginalFile
                 * @param scriptID see above
                 * @return see above
                 * @throws ApiUsageException
                 */
                idempotent RTypeDict getScriptWithDetails(long scriptID) throws ServerError;

                /**
                 * Get the parameters that the script takes and returns, along with
                 * other metadata available from the script.
                 *
                 * @param scriptID see above.
                 * @return see above.
                 * @throws ApiUsageException
                 **/
                idempotent ode::grid::JobParams getParams(long scriptID) throws ServerError;

                /**
                 * Delete the script on the server with id. The file will also be removed from disk.
                 *
                 * @param scriptID Id of the script to delete.
                 * @throws ApiUsageException
                 * @throws SecurityViolation
                 */
                void deleteScript(long scriptID) throws ServerError;

                // Planned methods
                // ===============
                // idempotent OriginalFileList getImageScripts(Parameters params) throws ServerError;
                // idempotent OriginalFileList getScriptsByName(string name, Parameters params) throws ServerError;
                // idempotent OriginalFileList getScriptsByKeyword(string keyword, Parameters params) throws ServerError;

                //
                // Process/Job Management
                //

                /**
                 * If {@link ode.ResourceError} is thrown, then no
                 * {@code Processor} is available. Use {@code scheduleJob}
                 * to create a {@link ode.model.ScriptJob} in the
                 * <i>Waiting</i> state. A {@code Processor} may become
                 * available.
                 *
                 * <pre>
                 * {@code
                 * try:
                 *     proc = scriptService.runScript(1, {}, None)
                 * except ResourceError:
                 *     job = scriptService.scheduleScript(1, {}, None)
                 * }
                 * </pre>
                 *
                 * The {@code ScriptProcess} proxy MUST be closed before
                 * exiting. If you would like the script execution to continue
                 * in the background, pass <code>True</code> as the argument.
                 *
                 * <pre>
                 * {@code
                 * try:
                 *     proc.poll()         # See if process is finished
                 * finally:
                 *     proc.close(True)    # Detach and execution can continue
                 *     # proc.close(False) # OR script is immediately stopped.
                 * }
                 * </pre>
                 */
                ode::grid::ScriptProcess* runScript(long scriptID, ode::RTypeDict inputs, ode::RInt waitSecs) throws ServerError;

                /**
                 * Returns true if there is a processor which will run the
                 * given script.
                 *
                 * <p>
                 * Either the script is an official script and this method
                 * will return true (though an individual invocation may fail
                 * with an {@link ode.ResourceError} for some reason)
                 * <em>or</em> this is a user script, and a usermode processor
                 * must be active which takes the scripts user or group.
                 * </p>
                 *
                 */
                idempotent
                bool canRunScript(long scriptID) throws ServerError;

                /**
                 * Used internally by processor.py to check if the script
                 * attached to the {@link ode.model.Job} has a valid script
                 * attached, based on the {@code acceptsList} and the current
                 * security context.
                 *
                 * An example of an acceptsList might be <pre>Experimenter(myUserId, False)</pre>, meaning that
                 * only scripts belonging to me should be trusted. An empty list implies that the server should
                 * return what it would by default trust.
                 *
                 * A valid script will be returned if it exists; otherwise null.
                 */
                idempotent
                ode::model::OriginalFile validateScript(ode::model::Job j, ode::api::IObjectList acceptsList) throws ServerError;

                // Planned methods
                // ===============
                // ode::grid::ScriptProcess* findProcess(long jobID) throws ServerError;
                // ode::model::ScriptJob scheduleScript(long scriptID, ode::RTypeDict inputs, ode::RTime scheduledFor) throws ServerError;
                // ode::api::ScriptJobList getActiveJobs(ode::sys::Parameters params) throws ServerError;
                // ode::api::ScriptJobList getWaitingJobs(ode::sys::Parameters params) throws ServerError;
                // ode::api::ScriptJobList getRunningJobs(ode::sys::Parameters params) throws ServerError;
                // long cancelActiveJobs() throws ServerError;
                // long cancelRunningJobs() throws ServerError;
                // long cancelWaitingJobs() throws ServerError;

            };

    };

};

#endif