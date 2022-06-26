package net.bhojpur.gradle.api

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

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logging
import net.bhojpur.gradle.api.extensions.ApiExtension
import net.bhojpur.gradle.api.extensions.SplitExtension
import net.bhojpur.gradle.api.factories.SplitFactory
import net.bhojpur.gradle.api.tasks.SplitTask

@CompileStatic
class ApiPluginBase implements Plugin<Project> {

    /**
     * Sets the group name for the DSLPlugin tasks to reside in.
     * i.e. In a terminal, call `./gradlew tasks` to list tasks in their groups in a terminal
     */
    public static final String GROUP = "ode-api"

    public static final String EXTENSION_NAME_API = "api"

    public static final String TASK_PREFIX_GENERATE = "generate"

    private static final def Log = Logging.getLogger(ApiPluginBase)

    @Override
    void apply(Project project) {
        ApiExtension api = createBaseExtension(project)

        api.language.whenObjectAdded { SplitExtension split ->
            registerSplitTask(project, api, split)
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    ApiExtension createBaseExtension(Project project) {
        def language = project.container(SplitExtension, new SplitFactory(project))
        return project.extensions.create(EXTENSION_NAME_API, ApiExtension, project, language)
    }

    static void registerSplitTask(Project project, ApiExtension api, SplitExtension split) {
        String taskName = TASK_PREFIX_GENERATE + split.name.capitalize()
        project.tasks.register(taskName, SplitTask, new Action<SplitTask>() {
            @Override
            void execute(SplitTask t) {
                t.with {
                    group = GROUP
                    setDescription("Splits ${split.language} from .combined files")
                    setOutputDir(split.outputDir.flatMap { File f -> api.outputDir.dir(f.toString()) })
                    setLanguage(split.language)
                    setNamer(split.renamer)
                    source api.combinedFiles + split.combinedFiles
                    include "**/*.combined"
                }
            }
        })
    }
}