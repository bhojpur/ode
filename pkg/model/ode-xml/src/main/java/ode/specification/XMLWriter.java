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
// integration.XMLWriter

//Java imports
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//Third-party libraries
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

//Application-internal dependencies
import ode.xml.model.ODE;

/**
 * Methods to write the XML tags to the files.
 */
public class XMLWriter
{

	/** Identifies the <code>ID</code> attribute. */
	public static final String ID_ATTRIBUTE = "ID";

	/** Identifies the <code>Name</code> attribute. */
	public static final String NAME_ATTRIBUTE = "Name";

	/** Identifies the <code>DimensionOrder</code> attribute. */
	public static final String DIMENSION_ORDER_ATTRIBUTE = "DimensionOrder";

	/** Identifies the <code>PixelType</code> attribute. */
	public static final String PIXELS_TYPE_ATTRIBUTE = "PixelType";

	/** Identifies the <code>SizeC</code> attribute. */
	public static final String SIZE_C_ATTRIBUTE = "SizeC";

	/** Identifies the <code>SizeT</code> attribute. */
	public static final String SIZE_T_ATTRIBUTE = "SizeT";

	/** Identifies the <code>SizeZ</code> attribute. */
	public static final String SIZE_Z_ATTRIBUTE = "SizeZ";

	/** Identifies the <code>SizeX</code> attribute. */
	public static final String SIZE_X_ATTRIBUTE = "SizeX";

	/** Identifies the <code>SizeY</code> attribute. */
	public static final String SIZE_Y_ATTRIBUTE = "SizeY";

	/** Identifies the <code>Compression</code> attribute. */
	public static final String COMPRESSION_ATTRIBUTE = "Compression";

	/** Identifies the <code>BigEndian</code> attribute. */
	public static final String BIG_ENDIAN_ATTRIBUTE = "BigEndian";

	/** Identifies the <code>Length</code> attribute. */
	public static final String LENGTH_ATTRIBUTE = "Length";

	/** Identifies the <code>ODE</code> tag. */
	public static final String ODE_TAG = "ODE";

	/** Identifies the <code>Image</code> tag. */
	public static final String IMAGE_TAG = "Image";

	/** Identifies the <code>Pixels</code> tag. */
	public static final String PIXELS_TAG = "Pixels";

	/** Identifies the <code>BinData</code> tag. */
	public static final String BIN_DATA_TAG = "BinData";
	
	/** The schema language. */
	private static final String JAXP_SCHEMA_LANGUAGE =
	    "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

	/** The schemas. */
	private static final String[] SCHEMAS = {
	    "http://www.bhojpur.net/Schemas/ODE/2018-03/ode.xsd"};

	/** The XML namespace. */
	private static final String XML_NS =
		"http://www.bhojpur.net/Schemas/ODE/2018-03";

	/** The XSI namespace. */
	private static final String XSI_NS =
		"http://www.w3.org/2001/XMLSchema-instance";

	/** The schema location. */
	private static final String SCHEMA_LOCATION =
		"http://www.bhojpur.net/Schemas/ODE/2018-03/ode.xsd";

	/** A default plane. */
	private static final String PLANE =
	"ZrXEfwslJ9N1nDrbtxxWh4fRHo4w8nZ2N0I74Lgj9oIKN9qrPbBK24z+w+9zYzRQ" +
	"WJXfEwwAKXgV4Z1jCPhE9woGjJaarHTsFwy21nF2IoJDkd3L/zSWMSVk508+jpxV" +
	"4t5p93HE1uE4K34WCVEeeZ1dSRli/b7/6RhF56DjdB6KboGly3zuN2/eZt9uJ2Mh" +
	"HZtktzpjFtn2mhf4i7iggpQyWx74xvFs9VxXQf1QoxN1KcTGXbdfPNoj3qmzz7Wm" +
	"8/iXXw7wpOrC2MRcbt98VH4UaQxFgu6VPer73JAS+r2Kd2C67ZFbweyR/LCoUiic" +
	"h866SrwJk3IrTD9AlnGO6SjHIz27yWVh1omr36H1qOuD4ULSknM2txm4FrB02gxH" +
	"WHbgaJWGT02eT1nwGNXygHe7gdYVP8o6Ms9sT/nBwhoMK8NuQINx7KJP/jTP0p5g" +
	"NjEHZeAN1To9Qp3AF3jaWK2671Dyy/l9BBRMhD3gEqXJ12ZXZ0par2pvqVtMcbpA" +
	"Zk96GKsSWDQP48yDkNYTG7RDBMzRJxiem7eifg1gpUP1rmmaNEu12+0wclsGBUeH" +
	"1d9HiN+rDnppycrVQIgvKbXKlUkQH230IYHDESKnlLCZALLJuRuAT5qsNri5950O" +
	"lphUxeYAnNfUkXYRUHGGnGXw58nmnBCp7iuHDC8AJdCRyK+0wk/xtt6EeADkPs9Q" +
	"q90H2kXvvGVbcL03IV1mb0PkdqWg2ovrkSLXKhLXb65ruPPz43TAT9xv4QJdmFqJ" +
	"baMHta8Wd1Fs9cffChHWJT3RS9U8VrhGlBB5+1D9PMlqLruYtp7ulUpMSJFOKkbo" +
	"yXoECSzJuzknqP2Cj1KWrNk+gSsnAlq5zko6KUyPXWMBVgPGNrXR+ivtIXmyQGu5" +
	"jSTuA+S+ogaPraRPQELmmuQ2wcoWI7O9Vpht1tFmgXkrdqCTD7+JwdXlbHSoRz3t" +
	"i9dpJY+LyKBisuKcDgdxWulwtydNliNSKKyt7qGC2B90VLo+XsYLLEYU+w95l2ZO" +
	"umqBquStdKntlReWtCDu8HfbK6AryfZXL5hqMTdqFubcXl4n5ZfBNtHaru8/LswN" +
	"VGua9VJUsvZV9rMniNwoU7Ev+oLc/0SZkJrwL/r+9Jl5k02DRymhE4XISJ3UXcnt" +
	"2K57w/OmIJK3HzznrIXgPJA9Nq7M6XjXDDXuBF08709iSEfOWWZ0Yz5ySoszOlSO" +
	"0OGoRYv8X9xUeOfWi4oizQeSOj2ZTXegqZLxj/g8Y7ykyDkG4NsMS0Kx2fZvxqKE" +
	"9EdUAXMvDN09X0fKdurqYqPBsRq79Id8YIJhamEP969OjHs9VXIETMmCkoUz2//7" +
	"BfeaCUzv5c61/asdOR6CJ4ANUX7hQA7hlTk8qllaaLIEWQyGeaDoaw9b5xq0Adhw" +
	"OZSeCKNIyQVpApdCOnXYuZVoTBNDdW7/7OPZD2uyS9gZ+7JGmuoV9/gRZT72oAQs" +
	"4++/GpC5h6uOx9Rt5265siOZjfYYX++/qUX8M5Fs9whPwL8NqrJ4qZrUbTYUzQaI";

	/**
	 * Copies the array to the file.
	 *
	 * @param file  The file where to copy the array
	 * @param values The array to copy.
	 * @throws Exception Thrown if an error occurred while copy the data.
	 */
	private void copyValues(File file, byte[] values)
		throws Exception
	{
		OutputStream stream = new FileOutputStream(file);
		stream.write(values);
		stream.close();
	}

	/**
	 * Creates the <code>String</code> version of the XML file
	 * creating using the specified root node.
	 *
	 * @param ode The root node.
	 * @param binaryData Pass <code>true</code> to add the binary data,
	 *                   <code>false</code> otherwise.
	 * @return See above.
	 * @throws Exception Thrown if an error occurred while writing the XML file.
	 */
	private String createXMLDocument(ODE ode, boolean binaryData)
		throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = factory.newDocumentBuilder();
		Document document = parser.newDocument();
		Element root = ode.asXMLElement(document);
		root.setAttribute("xmlns", XML_NS);
		root.setAttribute("xmlns:xsi", XSI_NS);
		root.setAttribute("xsi:schemaLocation", XML_NS + " " + SCHEMA_LOCATION);
		document.appendChild(root);

		//Add Planar data
		if (binaryData) {
			NodeList nodes = document.getElementsByTagName(BIN_DATA_TAG);
			for (int i = 0; i < nodes.getLength(); i++) {
				nodes.item(i).setTextContent(PLANE);
			}
		}
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");
		Source source = new DOMSource(document);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Result result = new StreamResult(new OutputStreamWriter(os, "utf-8"));
		transformer.transform(source, result);
		return os.toString();
	}

	/**
	 * Validates if the file is compatible with the latest model.
	 *
	 * @param file The file to validate.
	 * @throws Exception Thrown if the file cannot be validated.
	 */
	private void validate(File file)
		throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		factory.setAttribute(JAXP_SCHEMA_LANGUAGE, SCHEMAS);

		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.parse(file);
	}

	/** Creates a new instance. */
	public XMLWriter() {}

	/**
	 * Writes the data to the file. Binary data will be added.
	 *
	 * @param file The file to handle.
	 * @param ode  The element to write to the file.
	 * @throws Exception Thrown if an error occurred while writing the XML file.
	 */
	public void writeFile(File file, ODE ode)
		throws Exception
	{
		writeFile(file, ode, true);
	}

	/**
	 * Writes the data to the file.
	 *
	 * @param file The file to handle.
	 * @param ode  The element to write to the file.
	 * @param binaryData Pass <code>true</code> to add the binary data,
	 *                   <code>false</code> otherwise.
	 * @throws Exception Thrown if an error occurred while writing the XML file.
	 */
	public void writeFile(File file, ODE ode, boolean binaryData)
		throws Exception
	{
		String values = createXMLDocument(ode, binaryData);
		copyValues(file, values.getBytes());
		//validate(file);
	}

}