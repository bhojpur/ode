package ode.specification;

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

// The Bhojpur ODE Data Model specification

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.XMLConstants;

import org.w3c.dom.Document;

/**
 * Provides methods to validate Bhojpur ODE instance documents against
 */
public class OdeValidator
{

   /** Path the schema language. */
    public static final String JAXP_SCHEMA_LANGUAGE =
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /** W3C. */
    public static final String W3C_XML_SCHEMA =
        "http://www.w3.org/2001/XMLSchema";

    /** The source. */
    public static final String JAXP_SCHEMA_SOURCE =
        "http://java.sun.com/xml/jaxp/properties/schemaSource";

    public OdeValidator()
    {
    }

    /**
     * Validate the specified file
     * @param file The file to parse.
     * @param schema The schema used to validate the specified file.
     * @return true if no errors
     */
    public boolean isValidFile(File file, File schema)
    {
        try {
            // try parsing the file and return true if no errors
            parseFile(file, schema);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validate the specified file
     * @param file The file to parse.
     * @param schemaStreamArray The schema as array of stream sources used to validate the specified file.
     * @return true if no errors
     */
    public boolean isValidFile(File file, StreamSource[] schemaStreamArray)
    {
        try {
            // try parsing the file and return true if no errors
            parseFileWithStreamArray(file, schemaStreamArray);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validate the specified file
     * Any validation errors thrown as an exception.
     * @param file The file to parse.
     * @param schemaStreamArray The schema as array of stream sources used to validate the specified file.
     * @throws Exception Thrown if an error occurred.
     */
    public void validateFile(File file, StreamSource[] schemaStreamArray)
        throws Exception
    {
        // try parsing the file
        parseFileWithStreamArray(file, schemaStreamArray);
    }

    /*
     * Validate the specified file
     * Any validation errors thrown as an exception.
     * @param file The file to parse.
     * @param schema The schema used to validate the specified file.
     * @throws Exception Thrown if an error occurred.
     */
    public void validateFile(File file, File schema)
        throws Exception
    {
        // try parsing the file
        parseFile(file, schema);
    }

    /**
     * Validate the specified file
     * Any validation errors are sent to StdErr, not thrown as an exception.
     * @param file The file to parse.
     * @param schemaStreamArray The schema as array of stream sources used to validate the specified file.
     * @throws Exception Thrown if other (NON-VALIDATION) errors occurred.
     */
    public void validateFileToStdError(File file, StreamSource[] schemaStreamArray)
        throws Exception
    {
        // try parsing the file
        parseFileWithStreamArrayToSdtErr(file, schemaStreamArray);
    }

    /**
     * Parses the specified file and returns the document.
     *
     * @param file The file to parse.
     * @return Document
     * @throws Exception Thrown if an error occurred.
     */
    public Document parseFile(File file)
        throws Exception
    {
        return parseFile(file, null);
    }

    /**
     * Parses the specified file and returns the document.
     *
     * @param file The file to parse.
     * @param schema The schema used to validate the specified file.
     * @return Document
     * @throws Exception Thrown if an error occurred.
     */
    public Document parseFile(File file, File schema)
        throws Exception
    {
        if (file == null || !file.exists())
            throw new IllegalArgumentException("No file to parse.");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        if (schema != null) {
            // Set the schema file
            dbf.setAttribute(JAXP_SCHEMA_SOURCE, schema);
        }
        FileInputStream is = null;
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            is = new FileInputStream(file);
            return builder.parse(is);
        } catch (Exception e) {
            throw new Exception("Not able to parse the file.", e);
        } finally {
            if (is != null) is.close();
        }
    }

    /**
     * Parses the specified file and returns the document.
     * Any validation errors are thrown as an exception.
     * @param file The file to parse.
     * @param schemaStreamArray The schema as array of stream sources used to validate the specified file.
     * @return Document
     * @throws Exception Thrown if an error occurred.
     */
    public Document parseFileWithStreamArray(File file, StreamSource[] schemaStreamArray)
        throws Exception
    {
        if (file == null || !file.exists())
            throw new IllegalArgumentException("No file to parse.");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true); // This must be set to avoid error : cvc-elt.1: Cannot find the declaration of element 'ODE'.
        SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        SchemaResolver theTestClassResolver = new SchemaResolver();
        sFactory.setResourceResolver(theTestClassResolver);

        Schema theSchema = sFactory.newSchema( schemaStreamArray );

        // Version - two step parse then validate (throws error as exception)
        DocumentBuilder builder = dbf.newDocumentBuilder();
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            Document theDoc = builder.parse(is);
            Validator validator=theSchema.newValidator();
            validator.validate(new DOMSource(theDoc));
            return theDoc;
        } catch (Exception e) {
            throw new Exception("Cannot parse the file", e);
        } finally {
            if (is != null) is.close();
        }
    }

    /**
     * Parses the specified file and returns the document.
     * Any validation errors are sent to StdErr, not thrown as an exception.
     * @param file The file to parse.
     * @param schemaStreamArray The schema as array of stream sources used to validate the specified file.
     * @return Document
     * @throws Exception Thrown if other (NON-VALIDATION) errors occurred.
     */
    public Document parseFileWithStreamArrayToSdtErr(File file, StreamSource[] schemaStreamArray)
        throws Exception
    {
        if (file == null || !file.exists())
            throw new IllegalArgumentException("No file to parse.");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true); // This must be set to avoid error : cvc-elt.1: Cannot find the declaration of element 'ODE'.
        SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        SchemaResolver theTestClassResolver = new SchemaResolver();
        sFactory.setResourceResolver(theTestClassResolver);

        Schema theSchema = sFactory.newSchema(schemaStreamArray);
        // Version - two step parse then validate (throws error as exception)
        DocumentBuilder builder = dbf.newDocumentBuilder();
        FileInputStream is = null;
        try {
            dbf.setSchema(theSchema);
            is = new FileInputStream(file);
            Document theDoc = builder.parse(is);
            return theDoc;
        } catch (Exception e) {
            throw new Exception("Cannot parse the file", e);
        } finally {
            if (is != null) is.close();
        }
    }

}