package ode.formats.test.util;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.prefs.BackingStoreException;

import org.ini4j.IniPreferences;
import org.ini4j.InvalidIniFormatException;

public class TestEngineConfig extends IniPreferences
{

    public static enum ErrorOn {
        never,
        minimal,
        any;
    }

	/** Root node of the configuration file. */
	public static final String CONFIG_ROOT = "main";

	/** ODE server hostname */
	public static final String CONFIG_HOSTNAME = "hostname";

	/** ODE experimenter name (username) */
	public static final String CONFIG_USERNAME = "username";

    /** ODE experimenter password */
	public static final String CONFIG_PASSWORD = "password";

	/** ODE session key */
	public static final String CONFIG_SESSIONKEY = "sessionkey";

	/** ODE server port */
	public static final String CONFIG_PORT = "port";

	/** Whether or not to populate initiation files with metadata */
	public static final String CONFIG_POPULATE = "populate";

	/** If we're performing a single directory test run */
	public static final String CONFIG_RECURSE = "recurse";

    /** Target directory for the test engine to work with */
    public static final String CONFIG_TARGET = "target_directory";

    /** Value for which an error (non-0 exitcode) will be signalled */
    public static final String CONFIG_ERRORON = "error_on";

    /** Feedback url to be used */
    public static final String CONFIG_FEEDBACK = "feedback_url";

    public static final String DEFAULT_FEEDBACK = "http://qa.bhojpur.net";

    public static final String CONFIG_FEEDBACK_LOGIN_URL = "login_url";

    public static final String CONFIG_FEEDBACK_LOGIN_USERNAME = "login_username";

    public static final String CONFIG_FEEDBACK_LOGIN_PASSWORD = "login_password";

    public static final String CONFIG_FEEDBACK_MESSAGE_URL = "message_url";

    public static final String CONFIG_FEEDBACK_COMMENT_URL = "comment_url";

    public static final String DEFAULT_COMMENT_URL = "http://qa.bhojpur.net/qa/initial/";

	/**
	 * Test engine configuration, backed by an initiation file.
	 * @param file The file to use as a backing store.
	 * @throws BackingStoreException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws InvalidIniFormatException
	 */
	public TestEngineConfig(InputStream stream)
		throws InvalidIniFormatException, FileNotFoundException, IOException
	{
		super(stream);
	}

	/**
	 * Sets the hostname configuration property.
	 * @param hostname Value to set.
	 */
	public void setHostname(String hostname)
	{
		this.node(CONFIG_ROOT).put(CONFIG_HOSTNAME, hostname);
	}

	/**
	 * Returns the hostname configuration property.
	 * @return See above.
	 */
	public String getHostname()
	{
		return node(CONFIG_ROOT).get(CONFIG_HOSTNAME, null);
	}

	/**
	 * Sets the username configuration property.
	 * @param username Value to set.
	 */
	public void setUsername(String username)
	{
		node(CONFIG_ROOT).put(CONFIG_USERNAME, username);
	}

	/**
	 * Returns the username configuration property.
	 * @return See above.
	 */
	public String getUsername()
	{
		return node(CONFIG_ROOT).get(CONFIG_USERNAME, null);
	}

	/**
	 * Sets the password configuration property.
	 * @param password Value to set.
	 */
	public void setPassword(String password)
	{
		node(CONFIG_ROOT).put(CONFIG_PASSWORD, password);
	}

	/**
	 * Returns the password configuration property.
	 * @return See above.
	 */
	public String getPassword()
	{
		return node(CONFIG_ROOT).get(CONFIG_PASSWORD, null);
	}

	/**
	 * Sets the session key configuration property.
	 * @param sessionKey Value to set.
	 */
	public void setSessionKey(String sessionKey)
	{
		node(CONFIG_ROOT).put(CONFIG_SESSIONKEY, sessionKey);
	}

	/**
	 * Returns the password configuration property.
	 * @return See above.
	 */
	public String getSessionKey()
	{
		return node(CONFIG_ROOT).get(CONFIG_SESSIONKEY, null);
	}

	/**
	 * Sets the port configuration property.
	 * @param port Value to set.
	 */
	public void setPort(int port)
	{
		node(CONFIG_ROOT).putInt(CONFIG_PORT, port);
	}

	/**
	 * Returns the port configuration property.
	 * @return See above.
	 */
	public int getPort()
	{
		return node(CONFIG_ROOT).getInt(CONFIG_PORT, 4064);
	}

	/**
	 * Sets the populate configuration property.
	 * @param populate Value to set.
	 */
	public void setPopulate(boolean populate)
	{
		node(CONFIG_ROOT).putBoolean(CONFIG_POPULATE, populate);
	}

	/**
	 * Returns the populate configuration property.
	 * @return See above.
	 */
	public boolean getPopulate()
	{
		return node(CONFIG_ROOT).getBoolean(CONFIG_POPULATE, false);
	}

	/**
	 * Sets the recursion configuration property.
	 * @param rescurse Value to set.
	 */
	public void setRecurse(boolean recurse)
	{
		node(CONFIG_ROOT).putBoolean(CONFIG_RECURSE, recurse);
	}

	/**
	 * Returns the recursion configuration property.
	 * @return See above.
	 */
	public boolean getRecurse()
	{
		return node(CONFIG_ROOT).getBoolean(CONFIG_RECURSE, true);
	}

    /**
     * Sets the target directory configuration property.
     * @param rescurse Value to set.
     */
    public void setTarget(String target)
    {
        node(CONFIG_ROOT).put(CONFIG_TARGET, target);
    }

    /**
     * Returns the target directory configuration property.
     * @return See above.
     */
    public String getTarget()
    {
        return node(CONFIG_ROOT).get(CONFIG_TARGET, null);
    }

    /**
     * Sets the feedback url configuration property.
     */
    public void setFeedbackUrl(String feedbackUrl)
    {
        node(CONFIG_ROOT).put(CONFIG_FEEDBACK, feedbackUrl);
    }

    /**
     * Returns the feedback url configuration property.
     */
    public String getFeedbackUrl()
    {
        return node(CONFIG_ROOT).get(CONFIG_FEEDBACK, DEFAULT_FEEDBACK);
    }

    /**
     * Returns the feedback login url configuration property.
     */
    public String getFeedbackLoginUrl()
    {
        return node(CONFIG_ROOT).get(CONFIG_FEEDBACK_LOGIN_URL, null);
    }

    /**
     * Returns the feedback login url configuration property.
     */
    public String getFeedbackLoginUsername()
    {
        return node(CONFIG_ROOT).get(CONFIG_FEEDBACK_LOGIN_USERNAME, null);
    }

    /**
     * Returns the feedback login url configuration property.
     */
    public String getFeedbackLoginPassword()
    {
        return node(CONFIG_ROOT).get(CONFIG_FEEDBACK_LOGIN_PASSWORD, null);
    }

    /**
     * Returns the feedback login url configuration property.
     */
    public String getFeedbackMessageUrl()
    {
        return node(CONFIG_ROOT).get(CONFIG_FEEDBACK_MESSAGE_URL, null);
    }

    /**
     * Returns the comment system url configuration property.
     */
    public String getCommentUrl()
    {
        return node(CONFIG_ROOT).get(CONFIG_FEEDBACK_COMMENT_URL, DEFAULT_COMMENT_URL);
    }

    /**
     * Sets the error-on configuration property. Does this by first lowercasing
     * the string, and then checking for an equivalent enumeration in
     * {@link ErrorOn}
     */
    public void setErrorOn(String errorOn)
    {
        if (errorOn == null) {
            errorOn = "";
        }
        errorOn = errorOn.toLowerCase();
        ErrorOn.valueOf(errorOn); // Throws if necessary.
        node(CONFIG_ROOT).put(CONFIG_ERRORON, errorOn);
    }

    /**
     * Returns the feedback url configuration property.
     */
    public String getErrorOn()
    {
        return node(CONFIG_ROOT).get(CONFIG_ERRORON, "any");
    }

	/**
	 * Checks if we have the configuration properties available to perform an
	 * ODE server login.
	 * @return <code>true</code> if we have the configuration properties
	 * available, <code>false</code> otherwise.
	 */
	public boolean validateLogin()
	{
        if (((getUsername() == null || getPassword() == null)
		  && getSessionKey() == null) || getHostname() == null)
        {
            return false;
        }
        return true;
	}
}