package loci.ode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import loci.common.Constants;
import loci.ode.io.OdeReader;

/**
 * A class for testing the Bhojpur ODE implementation of
 * {@link loci.formats.IFormatReader}, for obtaining
 * pixels and metadata from a Bhojpur ODE database.
 */
@Deprecated
public class OdeReaderTest {

  private static String readLine(BufferedReader in,
    String prompt, String defaultValue) throws IOException
  {
    System.out.print(prompt + " [" + defaultValue + "]: ");
    String value = in.readLine();
    return value.equals("") ? defaultValue : value;
  }

  public static void main(String[] args) throws Exception {

    // get credentials from stdin
    BufferedReader in = new BufferedReader(
      new InputStreamReader(System.in, Constants.ENCODING));
    String server = readLine(in, "Server", "localhost");
    String portValue = readLine(in, "Port", "1099");
    String user = readLine(in, "Username", "root");
    String pass = readLine(in, "Password", "ode");
    String pidValue = readLine(in, "Pixels ID", "1");
    int port = Integer.parseInt(portValue);
    int pid = Integer.parseInt(pidValue);
    System.out.println();

    // connect to Bhojpur ODE server
    System.out.println("Initializing Bhojpur ODE reader");
    OdeReader ode = new OdeReader();
    String id = "ode:\n" +
      "server=" + server + "\n" +
      "port=" + port + "\n" +
      "user=" + user + "\n" +
      "pass=" + pass + "\n" +
      "pid=" + pid;
    ode.setId(id);

    // print some metadata as a simple test
    int sizeX = ode.getSizeX();
    int sizeY = ode.getSizeY();
    System.out.println("Image dimensions are " + sizeX + " x " + sizeY);

    ode.close();
  }

}