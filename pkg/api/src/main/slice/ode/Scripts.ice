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

#ifndef ODE_SCRIPTS_ICE
#define ODE_SCRIPTS_ICE

#include <ode/RTypes.ice>
#include <ode/System.ice>
#include <ode/ServerErrors.ice>
#include <ode/Collections.ice>

/*
 * The Processor API is intended to provide an script runner
 * implementation, for use by the server and via the
 * InteractiveProcessor wrapper by clients.
 */

module ode {

    /**
     * Base class similar to {@link ode.model.IObject} but for non-model-objects.
     **/
    class Internal{};

    /**
     * Base type for {@link RType}s whose contents will not be parsed by
     * the server. This allows Server-specific types to be safely
     * passed in as the inputs/outputs of scripts.
     **/
    ["protected"] class RInternal extends ode::RType {
        Internal val;
        Internal getValue();
    };

    /**
     * Simple 2D array of bytes.
     **/
    sequence<Ice::ByteSeq> Bytes2D;

    /**
     * Sequences cannot subclass other types, so the Plane
     * class extends {@code Internal} and wraps a {@code Bytes2D} instance.
     **/
    class Plane extends Internal {
        Bytes2D data;
    };

    /**
     * XY-point in space.
     **/
    class Point extends Internal {
        int x;
        int y;
    };

    /**
     * RGBA-color packed into a single long.
     **/
    class Color extends Internal {
        long packedColor;
    };

    module grid {

        /**
         * A single parameter to a Job. For example, used by
         * ScriptJobs to define what the input and output
         * environment variables should be. Helper classes are available
         * in the Python ode.scripts module, so that the following are
         * equivalent:
         *
         * <pre># 1
         * a = ode.grid.Params()
         * a.optional = True
         * a.prototype = ode.rtypes.rstring("")
         * a.description = "An optional string which will be ignored by the script"
         * ode.scripts.client(inputs = {"a":a})
         * </pre>
         *
         * <pre># 2
         * a = ode.scripts.String("a", optional=True, description=\
         * "An optional string which will be ignored by the script")
         * ode.scripts.client(a)
         * </pre>
         *
         * For advanced setters not available on the Type classes (like ode.script.String)
         * use the getter type.param() and then set values directly.
         *
         * <pre>
         * a = ode.scripts.String("a")
         * a.param().values = \["hi", "bye"]
         * </pre>
         **/
        class Param {

            /**
             * Usage documentation of this param for script users.
             *
             * Example of a bad description: ""a long value""
             *
             * Example of a good description: ""long representing
             * the number of bins to be used by <some algorithm>. A sensible
             * value would be between 16 and 32""
             *
             **/
            string description;

            /**
             * Whether or not a script will require this value to be present
             * in the input or output. If an input is missing or None when
             * non-optional, then a {@link ode.ValidationException} will be
             * thrown on {@code processJob}. A missing output param will be
             * marked after execution.
             **/
            bool \optional;

            /**
             * Whether or not the prototype should be used as a default.
             * If true, then if the value is missing from the input OR
             * output values, the prototype will be substituted.
             *
             * <pre>
             * param = ...;
             * inputs = ...;
             * if name in inputs:
             *     value = inputs\[name]
             * elif param.inputs\[name].useDefault:
             *     value = param.inputs\[name].prototype
             * </pre>
             **/
            bool useDefault;

            /**
             * {@link ode.RType} which represents what the input or output
             * value should look like. If this is a collection type (i.e.
             * {@link ode.RCollection} or {@link ode.RMap} or their
             * subclasses), then the first contents of the collection will be
             * used (recursively).
             *
             * <pre>
             * param.prototype = rlist(rlist(rstring)))
             * </pre>
             * requires that a list of list of strings be passed.
             **/
            ode::RType prototype;

            /**
             * Minimum value which an input may contain. If the prototype
             * is a collection type, then the min type must match the type
             * of the innermost non-collection instance.
             *
             * For example,
             * <pre>
             * param.prototype = rlong(0)
             * param.min = rlong(-5)
             * </pre>
             * but
             * <pre>
             * param.prototype = rlist(rlong(0))
             * param.min = rlong(-5)
             * </pre>
             **/
            ode::RType min;

            /**
             * Maximum value which an input may contain. If the prototype
             * is a collection type, then the max type must match the type
             * of the innermost non-collection instance.
             *
             * For example,
             * <pre>
             * param.prototype = rlong(0)
             * param.max = rlong(5)
             * </pre>
             * but
             * <pre>
             * param.prototype = rlist(rlong(0))
             * param.max = rlong(5)
             * </pre>
             **/
            ode::RType max;

            /**
             * An enumeration of acceptable values which can be used
             * for this parameter. If {@code min} and {@code max} are set,
             * this value will be ignored. If {@code prototype} is an
             * {@link ode.RCollection} or {@link ode.RMap} instance, then
             * the values in this {@link ode.RList} will be of the member
             * types of the collection or map, and not a collection or map
             * instance.
             **/
            ode::RList values;

            /**
             * Defines the grouping strategy for this {@link Param}.
             *
             * <p>
             * A set of {@code Param} objects in a single {@code JobParams} can
             * use dot notation to specify that they belong together,
             * and in which order they should be presented to the user.
             * </p>
             *
             * <pre>
             * inputs = {"a" : Param(..., grouping = "1.1"),
             *           "b" : Param(..., grouping = "1.2"),
             *           "c" : Param(..., grouping = "2.2"),
             *           "d" : Param(..., grouping = "2.1")}
             * </pre>
             * defines two groups of parameters which might be
             * display to the user so:
             *
             * <pre>
             *  Group 1:                  Group 2:
             * +-----------------------+ +-----------------------+
             * | a:                    | | d:                    |
             * +-----------------------+ +-----------------------+
             * | b:                    | | c:                    |
             * +-----------------------+ +-----------------------+
             * </pre>
             *
             * <p>
             * Further dots (e.g. "1.2.3.5") can be used to specify
             * deeper trees of parameters.
             * </p>
             *
             * <p>
             * By most clients, Params missing grouping values (e.g. "") will
             * be ordered <em>after</em> params with grouping values.
             * </p>
             *
             * <p>
             * A group which has a boolean as the top-level object
             * can be thought of as a checkbox which turns on or off
             * all of the other group members. For example,
             * </p>
             *
             * <pre>
             * inputs = {"Image_Ids" : Param(prototype=rlist(), grouping = "1"),
             *           "Scale_Bar" : Param(prototype=rbool(), grouping = "2"),
             *           "Color"     : Param(prototype=rinternal(Color()), grouping = "2.1"),
             *           "Size"      : Param(prototype=rlong(), grouping = "2.2")}
             * </pre>
             *
             * <p>
             * might be displayed as:
             * </p>
             *
             * <pre>
             *
             *  Scale Bar: \[ on/off ]
             *  ======================
             *    Color:  \[rgb]
             *    Size:   \[ 10]
             *
             * </pre>
             *
             **/
            string grouping;

            /**
             * Defines machine readable interpretations for this parameter.
             *
             * <p>
             * Where the description field should provide information for
             * users, the assigned namespaces can define how clients may
             * interpret the param.
             * </p>
             *
             * <p>
             * {@code ode.constants.namespaces.NSDOWNLOAD}, for example,
             * indicates that users may want to download the resulting
             * file. The {@code prototype} of the {@code Param} should be one
             * of: {@link ode.model.OriginalFile},
             * {@link ode.model.FileAnnotation},
             * or an annotation link (like
             * {@link ode.model.ImageAnnotationLink})
             * which points to a file annotation.
             * </p>
             **/
            ode::api::StringSet namespaces;
        };

        dictionary<string, Param> ParamMap;

        /**
         * Complete job description with all input and output Params.
         *
         * JobParams contain information about who wrote a script, what its
         * purpose is, and how it should be used, and are defined via the
         * "ode.scripts.client" method.
         *
         * <pre>
         * c = ode.scripts.client(name="my algorithm", version="0.0.1")
         * </pre>
         *
         * Alternatively, a JobParams instance can be passed into the constructor:
         *
         * <pre>
         * params = ode.grid.JobParams()
         * params.authors = \["Pramila", "Kumari"]
         * params.version = "0.0.1"
         * params.description = """
         *     Clever way to count to 5
         * """
         * c = ode.scripts.client(params)
         * </pre>
         *
         * A single JobParam instance is parsed from a script and stored by the server.
         * Later invocations re-use this instance until the script changes.
         **/
        class JobParams extends Internal {

            /**
             * Descriptive name for this script. This value should be unique where
             * possible, but no assurance is provided by the server that multiple
             * scripts with the same name are not present.
             **/
            string name;

            /**
             * Author-given version number for this script. Please see the script
             * authors' guide for information about choosing version numbers.
             **/
             string version;

            /**
             * A general description of a script, including documentation on how
             * it should be used, what data it will access, and other metrics
             * like how long it takes to execute, etc.
             **/
            string description;

            /**
             * Single, human-readable string for how to contact the script author.
             **/
            string contact;

            /**
             * Information about the authors who took part in creating this script.
             * No particular format is required.
             **/
            ode::api::StringArray authors;

            /**
             * Information about the institutions which took part in creating this script.
             * No particular format is required.
             **/
            ode::api::StringArray institutions;

            /**
             * For authors\[i], authorInstitutions\[i] should be
             * and array of indexes j such that author i is a member
             * of authorsInstitutions\[i]\[j].
             *
             * Example:
             *   authors = \["Jane", "Mike"]
             *   institutions = \["Acme U.", "Private Corp."]
             *   authorsInstitutions = \[\[1, 2], \[1]]
             *
             * which means that Jane is a member of both "Acme U."
             * and "Private Corp." while Mike is only a member of
             * "Acme U."
             *
             * An empty authorsInstitutions array implies that all
             * authors are from all institutions.
             **/
            ode::api::IntegerArrayArray authorsInstitutions;

            /**
             * Definitive list of the inputs which MAY or MUST be provided
             * to the script, based on the "optional" flag.
             **/
            ParamMap inputs;

            /**
             * Definitive list of the outputs which MAY or MUST be provided
             * to the script, based on the "optional" flag.
             **/
            ParamMap outputs;

            /**
             * {@code ode.model.Format.value} of the stdout stream produced
             * by the script. If this value is not otherwise set (i.e. is
             * None), the default of "text/plain" will be set. This is
             * typically a good idea if the script uses "print" or the logging
             * module.
             *
             * If you would like to disable stdout upload, set the value to ""
             * (the empty string).
             *
             * "text/html" or "application/octet-stream" might also be values of interest.
             **/
            string stdoutFormat;

            /**
             * {@code ode.model.Format.value} of the stderr stream produced by
             * the script. If this value is not otherwise set (i.e. is None),
             * the default of "text/plain" will be set. This is typically a
             * good idea if the script uses "print" or the logging module.
             *
             * If you would like to disable stderr upload, set the value to ""
             * (the empty string).
             *
             * "text/html" or "application/octet-stream" might also be values of interest.
             **/
            string stderrFormat;

            /**
             * Defines machine readable interpretations for this
             * {@code JobParams}.
             *
             * <p>
             * Where the description field should provide information for
             * users, the assigned namespaces can define how clients may
             * interpret the script, including which categories or algorithm
             * types the script belongs to.
             * </p>
             *
             **/
            ode::api::StringSet namespaces;
        };

        /**
         * Callback which can be attached to a Process
         * with notification of any of the possible
         * ends-of-life that a Process might experience
         **/
        ["ami"] interface ProcessCallback {

            /**
             * Process terminated normally. Return code provided.
             * In the case that a non-Server process sent a signal
             * (KILL, TERM, ... ), that will represented in the
             * return code.
             **/
            void processFinished(int returncode);

            /**
             * cancel() was called on this Process. If the Process
             * failed to terminate, argument is false, in which calling
             * kill() is the last resort.
             **/
            void processCancelled(bool success);

            /**
             * kill() was called on this Process. If this does not
             * succeed, there is nothing else that Server can do to
             * stop its execution.
             **/
            void processKilled(bool success);
        };

        /**
         * Thin wrapper around a system-level process. Most closely
         * resembles Python's subprocess.Popen class.
         **/
        ["ami"] interface Process {

            /**
             * Returns the return code of the process, or null
             * if unfinished.
             **/
            idempotent
            ode::RInt poll() throws ode::ServerError;

            /**
             * Blocks until poll() would return a non-null return code.
             **/
            idempotent
            int wait() throws ode::ServerError;

            /**
             * Signal to the Process that it should terminate. This may
             * be done "softly" for a given time period.
             **/
            idempotent
            bool cancel() throws ode::ServerError;

            /**
             * Terminate the Process immediately.
             **/
            bool kill();

            /**
             * First attempts cancel() several times and finally
             * resorts to kill to force the process to shutdown
             * cleanly. This method doesn't return any value or
             * throw an exception so that it can be called oneway.
             **/
             void shutdown();

            /**
             * Add a callback for end-of-life events
             **/
            void registerCallback(ProcessCallback* cb) throws ode::ServerError;

            /**
             * Remove a callback for end-of-life events
             **/
            void unregisterCallback(ProcessCallback* cb) throws ode::ServerError;
        };

        /**
         * Extension of the {@code Process} interface which is returned by
         * {@code IScript} when an {@link ode.model.ScriptJob} is launched.
         * It is critical that instances of (@code ScriptProcess} are closed
         * on completion. See the {@link #close} method for more information.
         **/
        interface ScriptProcess extends Process {

            /**
             * Returns the job which started this process. Several
             * scheduling fields (submitted, scheduledFor, started, finished)
             * may be of interest.
             **/
            idempotent
            ode::model::ScriptJob getJob() throws ServerError;

            /**
             * Returns the results immediately if present. If the process
             * is not yet finished, waits "waitSecs" before throwing an
             * {@link ode.ApiUsageException}. If poll has returned a non-null
             * value, then this method will always return a non-null value.
             **/
            idempotent
            ode::RTypeDict getResults(int waitSecs) throws ServerError;

            /**
             * Sets the message on the {@link ode.model.ScriptJob} object.
             * This value MAY be overwritten by the server if the script
             * fails.
             **/
            idempotent
            string setMessage(string message) throws ServerError;

            /**
             * Closes this process and frees server resources attached to it.
             * If the detach argument is True, then the background process
             * will continue executing. The user can reconnect to the process
             * via the {@code IScript} service.
             *
             * If the detach argument is False, then the background process
             * will be shutdown immediately, and all intermediate results
             * (stdout, stderr, ...) will be uploaded.
             **/
            void close(bool detach) throws ServerError;
        };

        //
        // INTERNAL DEFINITIONS:
        // The following classes and types will not be needed by the casual user.
        //

        /*
         * Forward definition of the Processor interface.
         */
        interface Processor;

        /**
         * Internal callback interface which is passed to the
         * {@code Processor.accepts} method
         * to query whether or not a processor will accept a certain operation.
         **/
        interface ProcessorCallback {
            idempotent void isAccepted(bool accepted, string sessionUuid, string procConn);
            idempotent void isProxyAccepted(bool accepted, string sessionUuid, Processor* procProxy);
            idempotent void responseRunning(ode::api::LongList jobIds);
        };

        /**
         * Simple controller for Processes. Uses the session
         * id given to create an Ice.Config file which is used
         * as the sole argument to an execution of the given job.
         *
         * Jobs are responsible for loading arguments from the
         * environment via the session id.
         **/
        ["ami"] interface Processor {

            /**
             * Called by {@link ode.grid.SharedResources} to find a suitable
             * target for {@code ode.grid.SharedResources.acquireProcessor}.
             * New processor instances are added to the checklist by using
             * {@code ode.grid.SharedResources.addProcessor}. All processors
             * must respond with their session uuid in order to authorize
             * the action.
             **/
            idempotent
            void willAccept(ode::model::Experimenter userContext,
                         ode::model::ExperimenterGroup groupContext,
                         ode::model::Job scriptContext,
                         ProcessorCallback* cb);

            /**
             * Used by servers to find out what jobs are still active.
             * Response will be sent to
             * {@code ProcessorCallback.responseRunning}
             **/
            idempotent
            void requestRunning(ProcessorCallback* cb);

            /**
             * Parses a job and returns metadata definition required
             * for properly submitting the job. This object will be
             * cached by the server, and passed back into {@code processJob}
             **/
            idempotent
            JobParams parseJob(string session, ode::model::Job jobObject) throws ServerError;

            /**
             * Starts a process based on the given job
             * If this processor cannot handle the given job, a
             * null process will be returned. The {@code params} argument
             * was created by a previously call to {@code parseJob}.
             **/
            Process* processJob(string session, JobParams params, ode::model::Job jobObject) throws ServerError;

        };

        /**
         * Client facing interface to the background processing
         * framework. If a user needs interactivity, one of these
         * processors should be acquired from the ServiceFactory.
         * Otherwise, a Job can be submitted via JobHandle.
         **/
        ["ami"] interface InteractiveProcessor {

            /**
             * Returns the system clock time in milliseconds since the epoch
             * at which this processor will be reaped.
             **/
            idempotent
            long expires();

            /**
             * Returns the job which defines this processor. This may be
             * only the last job associated with the processor if execute
             * is called multiple times.
             **/
            idempotent
            ode::model::Job getJob();

            /**
             * Retrieves the parameters needed to be passed in an execution
             * and the results which will be passed back out.
             *
             * This method is guaranteed to return a non-null value or throw an exception.
             **/
            idempotent
            JobParams params() throws ServerError;

            /**
             * Executes an instance of the job returned by getJob() using
             * the given map as inputs.
             **/
            Process* execute(ode::RMap inputs) throws ServerError;

            /**
             * Retrieve the results for the given process. This will throw
             * an ApiUsageException if called before the process has returned.
             * Use either process.poll() or process.wait() or a ProcessCallback
             * to wait for completion before calling.
             *
             * If the user has not overridden or disabled the output values
             * "stdout" and "stderr", these will be filled with the OriginalFile
             * instances uploaded after completion under the key values of the
             * same name.
             **/
            idempotent
            ode::RMap getResults(Process* proc) throws ServerError;

            /**
             * Sets whether or not cancel will be called on the current
             * {@link Process} on stop. If detach is true, then the
             * {@link Process} will continue running. Otherwise,
             * {@link Process#cancel} will be called, before preparing for
             * another run.
             *
             * false by default
             *
             **/
            idempotent
            bool setDetach(bool detach) throws ServerError;

            /**
             * Clears the current execution of {@link ode.model.Job} from
             * the processor to prepare for another execution.
             *
             * cancel() will be called on the current {@link Process}
             * if detach is set to false.
             **/
            idempotent
            void stop() throws ServerError;

        };

        ["java:type:java.util.ArrayList<ode.grid.InteractiveProcessorPrx>:java.util.List<ode.grid.InteractiveProcessorPrx>"]
            sequence<InteractiveProcessor*> InteractiveProcessorList;

    };
};

#endif