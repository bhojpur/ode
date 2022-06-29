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

import java.io.InputStream;
import org.w3c.dom.ls.LSResourceResolver;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;

/**
 * A resolver for Schema locations that pulls them from jar resources.
 */
public class SchemaResolver implements LSResourceResolver
{
    private DOMImplementationLS theDOMImplementationLS;

    // the static string to strip when mapping schema locations
    private static String GIT_MASTER_PATH  = "http://git.bhojpur.net/src/master/components/specification";
    private static String GIT_DEVELOP_PATH = "http://git.bhojpur.net/src/develop/components/specification";
    private static String MAIN_PATH = "http://www.bhojpur.net/Schemas/";
    private static String MAIN_SEARCH_PATH = "/released-schema/";
    private static String LEGACY_AC_PATH = "http://www.bhojpur.net/XMLschemas/AnalysisChain/RC1/";
    private static String LEGACY_AM_PATH = "http://www.bhojpur.net/XMLschemas/AnalysisModule/RC1/";
    private static String LEGACY_BF_PATH = "http://www.bhojpur.net/XMLschemas/BinaryFile/RC1/";
    private static String LEGACY_CA_PATH = "http://www.bhojpur.net/XMLschemas/CA/RC1/";
    private static String LEGACY_CL_PATH = "http://www.bhojpur.net/XMLschemas/CLI/RC1/";
    private static String LEGACY_DH_PATH = "http://www.bhojpur.net/XMLschemas/DataHistory/IR3/";
    private static String LEGACY_ML_PATH = "http://www.bhojpur.net/XMLschemas/MLI/IR2/";
    private static String LEGACY_OM_PATH = "http://www.bhojpur.net/XMLschemas/ODE/FC/";
    private static String LEGACY_ST_PATH = "http://www.bhojpur.net/XMLschemas/STD/RC2/";
    private static String LEGACY_SEARCH_PATH = "/released-schema/2018-FC/";

    public SchemaResolver() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        // Create the objects necessary to make the correct LSInput return types
        theDOMImplementationLS = null;
        
        // if version 7 ?
        try {
        DOMImplementationRegistry theDOMImplementationRegistryA =
            DOMImplementationRegistry.newInstance();
        theDOMImplementationLS =
            (DOMImplementationLS) theDOMImplementationRegistryA.getDOMImplementation("XML 3.0 LS 3.0");
        } catch  (Exception e) {
            // theDOMImplementationLS should still be null
        }

        // if version 6 ?
        if (theDOMImplementationLS == null) {
          try {
            DOMImplementationRegistry theDOMImplementationRegistryB =
                DOMImplementationRegistry.newInstance();
            theDOMImplementationLS =
                (DOMImplementationLS) theDOMImplementationRegistryB.getDOMImplementation("LS");
          } catch (Exception e) {
            // theDOMImplementationLS should still be null
          }
        }

        // if verson 4 & 5 ?
        if (theDOMImplementationLS == null) {
          try {
            System.setProperty(
                DOMImplementationRegistry.PROPERTY,
                "org.apache.xerces.dom.DOMImplementationSourceImpl");
            DOMImplementationRegistry theDOMImplementationRegistryC =
                DOMImplementationRegistry.newInstance();
            theDOMImplementationLS =
                (DOMImplementationLS) theDOMImplementationRegistryC.getDOMImplementation("LS");
          } catch (Exception e) {
            // theDOMImplementationLS should still be null
          }
        }
        if (theDOMImplementationLS == null) {
          throw new InstantiationException("SchemaResolver could not create suitable DOMImplementationLS");
        }
    }


    /**
     * Resolves known namespace locations to their appropriate jar resource
     *
     * @param type Not used by function.
     * @param namespaceURI Not used by function.
     * @param publicId Not used by function.
     * @param systemId The schema location that will be used to choose the resource to return.
     * @param baseURI Not used by function.
     * @return The requested resource.
     */
    @Override
    public LSInput  resolveResource(
        String type, String namespaceURI, String publicId,
        String systemId, String baseURI)
    {
        LSInput theResult = null;

        // Match the requested schema locations and create the appropriate LSInput object
        if (systemId.equals("http://www.w3.org/2001/xml.xsd"))
        {
            theResult = makeSubstutionStream("/released-schema/external/xml.xsd", systemId);
        }
        else if (systemId.startsWith(GIT_MASTER_PATH))
        {
            theResult = makeSubstutionStream(systemId.substring(GIT_MASTER_PATH.length()), systemId);
        }
        else if (systemId.startsWith(GIT_DEVELOP_PATH))
        {
            theResult = makeSubstutionStream(systemId.substring(GIT_DEVELOP_PATH.length()), systemId);
        }
        else if (systemId.startsWith(MAIN_PATH))
        {
            theResult = makeSubstutionStream(MAIN_SEARCH_PATH + systemId.substring(MAIN_PATH.length()), systemId);
        }
        else if (systemId.startsWith(LEGACY_AC_PATH))
        {
            theResult = makeSubstutionStream(LEGACY_SEARCH_PATH + systemId.substring(LEGACY_AC_PATH.length()), systemId);
        }
        else if (systemId.startsWith(LEGACY_AM_PATH))
        {
            theResult = makeSubstutionStream(LEGACY_SEARCH_PATH + systemId.substring(LEGACY_AM_PATH.length()), systemId);
        }
        else if (systemId.startsWith(LEGACY_BF_PATH))
        {
            theResult = makeSubstutionStream(LEGACY_SEARCH_PATH + systemId.substring(LEGACY_BF_PATH.length()), systemId);
        }
        else if (systemId.startsWith(LEGACY_CA_PATH))
        {
            theResult = makeSubstutionStream(LEGACY_SEARCH_PATH + systemId.substring(LEGACY_CA_PATH.length()), systemId);
        }
        else if (systemId.startsWith(LEGACY_CL_PATH))
        {
            theResult = makeSubstutionStream(LEGACY_SEARCH_PATH + systemId.substring(LEGACY_CL_PATH.length()), systemId);
        }
        else if (systemId.startsWith(LEGACY_DH_PATH))
        {
            theResult = makeSubstutionStream(LEGACY_SEARCH_PATH + systemId.substring(LEGACY_DH_PATH.length()), systemId);
        }
        else if (systemId.startsWith(LEGACY_ML_PATH))
        {
            theResult = makeSubstutionStream(LEGACY_SEARCH_PATH + systemId.substring(LEGACY_ML_PATH.length()), systemId);
        }
        else if (systemId.startsWith(LEGACY_OM_PATH))
        {
            theResult = makeSubstutionStream(LEGACY_SEARCH_PATH + systemId.substring(LEGACY_OM_PATH.length()), systemId);
        }
        else if (systemId.startsWith(LEGACY_ST_PATH))
        {
            theResult = makeSubstutionStream(LEGACY_SEARCH_PATH + systemId.substring(LEGACY_ST_PATH.length()), systemId);
        }
        else
        {
            throw new RuntimeException("SchemaResolver does not know path to resolve: [" + systemId + "] from Bhojpur ODE specification jar.");
        }

        return theResult;
    }

    /**
     * Creates the LSInput object from the resource path
     *
     * @param theResourcePath Path to the schema in the Specification jar.
     * @param systemId
     * @return The requested LSInput object.
     */
    private LSInput makeSubstutionStream(
        String theResourcePath, String systemId)
    {
        LSInput theResult = null;
        theResult = theDOMImplementationLS.createLSInput();
        InputStream theResourcesStream = getClass().getResourceAsStream(theResourcePath);
        theResult.setByteStream(theResourcesStream);
        theResult.setSystemId(systemId);
        return theResult;
    }
}