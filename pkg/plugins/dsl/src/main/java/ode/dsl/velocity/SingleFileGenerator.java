package ode.dsl.velocity;

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

import ode.dsl.SemanticType;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.util.Comparator;
import java.util.List;

public class SingleFileGenerator extends Generator {

    /**
     * Folder to write velocity generated content
     */
    private File outFile;


    private SingleFileGenerator(Builder builder) {
        super(builder);
        this.outFile = builder.outFile;
    }

    @Override
    public Void call() throws Exception {
        // Create list of semantic types from source files
        List<SemanticType> types = loadSemanticTypes(odeXmlFiles);
        if (types.isEmpty()) {
            return null; // Skip when no files, otherwise we overwrite.
        }

        // Sort types by short name
        types.sort(Comparator.comparing(SemanticType::getShortname));

        /// Put all types in velocity context
        VelocityContext vc = new VelocityContext();
        vc.put("types", types);

        // Do the work
        parseTemplate(vc, template, outFile);
        return null;
    }

    public static class Builder extends Generator.Builder {
        private File outFile;

        public Builder setOutFile(File outFile) {
            this.outFile = outFile;
            return this;
        }

        @Override
        public SingleFileGenerator build() {
            return new SingleFileGenerator(this);
        }
    }
}