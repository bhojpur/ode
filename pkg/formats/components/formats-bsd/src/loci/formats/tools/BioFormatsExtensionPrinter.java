/*
 * BSD implementations of Bhojpur ODE-Formats readers and writers
 */

/*
IMPORTANT NOTE: Although this software is distributed according to a
"BSD-style" license, it requires the Bhojpur ODE-Formats Java library
to do anything useful, which is licensed under the GPL v2 or later.
As such, if you wish to distribute this software with ODE-Formats itself,
your combined work must be distributed under the terms of the GPL.
*/

package loci.formats.tools;

import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

import loci.common.Constants;

/**
 * This class is used to generate a text file containing all of the
 * image file extensions supported by Bhojpur ODE-Formats.
 */
public class OdeFormatsExtensionPrinter {

	public static void main(String[] args) throws IOException {
		System.out.println("Generating list of ODE-Formats supported suffixes...");
		try (IFormatReader reader = new ImageReader()) {
			String[] suffixes = reader.getSuffixes();

			PrintWriter fo = null;

			fo =
				new PrintWriter(new File("OdeFormatsSuffixes.txt"), Constants.ENCODING);

			for (String s : suffixes) fo.println("*." + s);

			fo.close();
			System.out.println(suffixes.length + " suffixes discovered.");
		}
	}

}
