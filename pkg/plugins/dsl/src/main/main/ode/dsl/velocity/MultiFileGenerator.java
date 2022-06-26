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
import java.security.InvalidParameterException;
import java.util.Collection;

public class MultiFileGenerator extends Generator {

    /**
     * Callback for formatting final filename
     */
    public interface FileNameFormatter {
        String format(SemanticType t);
    }

    /**
     * Folder to write velocity generated content
     */
    private File outputDir;

    /**
     * callback for formatting output file name
     */
    private FileNameFormatter fileNameFormatter;

    private MultiFileGenerator(Builder builder) {
        super(builder);
        if (builder.outputDir == null) {
            throw new InvalidParameterException("Where are files supposed to be written to?");
        }

        if (builder.fileNameFormatter == null) {
            throw new InvalidParameterException("File name formatter is required");
        }

        outputDir = builder.outputDir;
        fileNameFormatter = builder.fileNameFormatter;
    }

    @Override
    public Void call() throws Exception {
        // Create list of semantic types from source files
        Collection<SemanticType> types = loadSemanticTypes(odeXmlFiles);
        if (types.isEmpty()) {
            return null; // Skip when no files, otherwise we overwrite.
        }

        // Velocity process the semantic types
        for (SemanticType st : types) {
            VelocityContext vc = new VelocityContext();
            vc.put("type", st);

            // Format the final filename using callback
            String filename = fileNameFormatter.format(st);
            parseTemplate(vc, template, new File(outputDir, filename));
        }
        return null;
    }

    public static class Builder extends Generator.Builder {
        private File outputDir;
        private FileNameFormatter fileNameFormatter;

        public Builder setOutputDir(File outputDir) {
            this.outputDir = outputDir;
            return this;
        }

        public Builder setFileNameFormatter(FileNameFormatter callback) {
            this.fileNameFormatter = callback;
            return this;
        }

        public MultiFileGenerator build() {
            return new MultiFileGenerator(this);
        }
    }
}