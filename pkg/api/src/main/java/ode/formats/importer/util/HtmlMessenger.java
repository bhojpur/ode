package ode.formats.importer.util;

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

import java.io.InputStreamReader;
import java.io.Reader;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 * This class allows you to submit a URL and a String hash map of
 * key-value pairs to be posted to the URL in question.
 */
public class HtmlMessenger
{

	/** proxy host. */
	static final String PROXY_HOST = "http.proxyHost";

	/** proxy port. */
	static final String PROXY_PORT = "http.proxyPort";

	/** connection_timeout **/
	static final int CONN_TIMEOUT = 10000;

	/** The request to post.*/
	private HttpPost request;

	private boolean secure;

	/**
     * Creates a connection.
     *
     * @return See above
     * @throws HtmlMessengerException Thrown if an error occurred while creating the
     *                            SSL context.
     */
    private SSLConnectionSocketFactory createSSLConnection()
        throws HtmlMessengerException
    {
        SSLContext sslcontext = SSLContexts.createSystemDefault();
        final TrustManager trustEverything = new X509TrustManager() {
            private final X509Certificate[] 
                    acceptedIssuers = new X509Certificate[0];

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return this.acceptedIssuers;
            }
        };
        
        TrustManager[] managers = {trustEverything};
        try {
            sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, managers, null);
        } catch (Exception e) {
            throw new HtmlMessengerException("Cannot create security context", e);
        }
        return new SSLConnectionSocketFactory(sslcontext,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }

    /**
     * Creates a communicator.
     *
     * @param secure <code>Pass</code> true if https, <code>false</code>
     *              otherwise.
     * @return See above
     * @throws HtmlMessengerException Thrown if an error occurred while creating
     *                                the communicator.
     */
	private CloseableHttpClient getCommunicationLink(boolean secure)
	        throws HtmlMessengerException
	{
	  //Default connection configuration
        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setCookieSpec(CookieSpecs.BEST_MATCH)
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM,
                        AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC));
        builder.setConnectTimeout(CONN_TIMEOUT);
        String proxyHost = System.getProperty(PROXY_HOST);
        String proxyPort = System.getProperty(PROXY_PORT);
        if (StringUtils.isNotBlank(proxyHost) &&
                StringUtils.isNotBlank(proxyPort)) {
            builder.setProxy(new HttpHost(proxyHost,
                    Integer.parseInt(proxyPort)));
        }
        HttpClientBuilder httpBuilder = HttpClients.custom();
        httpBuilder.setDefaultRequestConfig(builder.build());

        if (secure)
            httpBuilder.setSSLSocketFactory(createSSLConnection());
        return httpBuilder.build();
	}

	/**
	 * Instantiate messenger
	 *
	 * @param url the HTTP POST URL
	 * @param postList variables list in post
	 * @throws HtmlMessengerException if the POST failed
	 */
	public HtmlMessenger(String url, Map<String, String> postList)
	        throws HtmlMessengerException
	{
		try
		{
		  //Create request.
	        if (StringUtils.isBlank(url))
	            throw new HtmlMessengerException("No URL specified.");
	        request = new HttpPost(url);
	        request.addHeader("Accept", "text/plain");
	        request.addHeader("Content-type", "application/x-www-form-urlencoded");
	        List<BasicNameValuePair> p = new ArrayList<BasicNameValuePair>();
	        Entry<String, String> entry;
	        Iterator<Entry<String, String>> k = postList.entrySet().iterator();
	        while (k.hasNext()) {
	            entry = k.next();
	            p.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	        }
	        request.setEntity(new UrlEncodedFormEntity(p));
	        secure = url.toLowerCase().startsWith("https");
		} catch (Exception e) {
			throw new HtmlMessengerException("Error creating post parameters", e);
		}

	}

	/**
	 * Creates a client to communicate.
	 *
	 * @param url get a HTTP client connection
	 * @return if the connection is encrypted
	 * @throws HtmlMessengerException if connection to the URL failed
	 */
	public CloseableHttpClient getCommunicationLink(String url)
	        throws HtmlMessengerException
	{
	    if (StringUtils.isBlank(url))
            throw new HtmlMessengerException("No URL specified.");
	    return getCommunicationLink(url.toLowerCase().startsWith("https"));
	}

	/**
	 * Execute a post action and retrieve server reply
	 * This method executes the post created when this class is instantiated
	 *
	 * @return server reply
	 * @throws HtmlMessengerException if the HTTP POST failed
	 */
	 public String executePost() throws HtmlMessengerException
	 {
		String serverReply = "";
		Reader reader = null;
		CloseableHttpClient client = null;
		try {
			// Execute the POST method
		    client = getCommunicationLink(secure);
		    CloseableHttpResponse response = client.execute(request);
		    HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            reader = new InputStreamReader(entity.getContent());
                char[] buf = new char[32678];
                StringBuilder str = new StringBuilder();
                for (int n; (n = reader.read(buf)) != -1;)
                    str.append(buf, 0, n);
                serverReply = str.toString();
	        }
		} catch( Exception e ) {
			throw new HtmlMessengerException("Cannot Connect", e);
		} finally {
		    if (client != null) {
		        try {
		            client.close();
                } catch (Exception ex) {}
		    }
		    if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ex) {}
            }
		}
		return serverReply;
	 }

}