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
import ode.dsl.SemanticTypeProcessor;
import ode.dsl.sax.MappingReader;
import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

public abstract class Generator implements Callable<Void> {

    private final Logger Log = LoggerFactory.getLogger(Generator.class);

    /**
     * The database types corresponding to the profiles
     */
    private Properties databaseTypes;

    /**
     * Profile thing
     */
    protected String profile;

    /**
     * Collection of .ode.xml files to process
     */
    protected Collection<File> odeXmlFiles;

    /**
     * Velocity template file name
     */
    protected File template;

    /**
     * VelocityEngine instance to use for performing work
     */
    protected VelocityEngine velocity;

    protected Generator(Builder builder) {
        if (builder.profile == null || builder.profile.isEmpty()) {
            throw new InvalidParameterException("Generator.profile cannot be null or empty");
        }

        if (builder.databaseTypes == null || builder.databaseTypes.isEmpty()) {
            throw new InvalidParameterException("Generator.databaseTypes cannot be null or empty");
        }

        if (builder.template == null) {
            throw new InvalidParameterException("Velocity '.vm' files missing or not set!");
        }

        if (builder.odeXmlFiles == null || builder.odeXmlFiles.isEmpty()) {
            throw new InvalidParameterException("No '.ode.xml' files supplied!");
        }

        this.profile = builder.profile;
        this.databaseTypes = builder.databaseTypes;
        this.odeXmlFiles = builder.odeXmlFiles;
        this.template = builder.template;
        this.velocity = builder.velocity;
    }

    List<SemanticType> loadSemanticTypes(Collection<File> files) {
        Map<String, SemanticType> typeMap = new HashMap<>();
        MappingReader sr = new MappingReader(profile, databaseTypes);
        for (File file : files) {
            if (file.exists()) {
                typeMap.putAll(sr.parse(file));
            }
        }

        if (typeMap.isEmpty()) {
            return Collections.emptyList(); // Skip when no files, otherwise we overwrite.
        }

        return new SemanticTypeProcessor(profile, typeMap, databaseTypes).call();
    }

    void parseTemplate(VelocityContext vc, File template, File output) throws IOException {
        try (InputStream in = FileUtils.openInputStream(template);
             OutputStream os = FileUtils.openOutputStream(output);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in));
             BufferedWriter result = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {
            velocity.evaluate(vc, result, "Processing template: " + this.template.getName(), reader);
        } catch (ParseErrorException e) {
            Log.error("Error parsing template", e);
        } catch (MethodInvocationException e) {
            Log.error("", e);
        }
    }

    public static abstract class Builder {
        private String profile;
        private Properties databaseTypes;
        private File template;
        private VelocityEngine velocity;
        private Collection<File> odeXmlFiles;

        public Builder setProfile(String profile) {
            this.profile = profile;
            return this;
        }

        public Builder setDatabaseTypes(Properties databaseTypes) {
            this.databaseTypes = databaseTypes;
            return this;
        }

        public Builder setOdeXmlFiles(Collection<File> source) {
            this.odeXmlFiles = source;
            return this;
        }

        public Builder setTemplate(File template) {
            this.template = template;
            return this;
        }

        public Builder setVelocityEngine(VelocityEngine velocity) {
            this.velocity = velocity;
            return this;
        }

        public abstract Generator build();
    }

}