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

import java.io.File;
import java.util.prefs.BackingStoreException;

import org.ini4j.IniFile;


class TestEngineIniFile extends IniFile
{
    private boolean populate = true;

    public TestEngineIniFile(File file)
        throws BackingStoreException
    {
        super(file, Mode.RW);
    }

    public String[] getFileList()
    {
        try
        {
            return childrenNames();
        } catch (BackingStoreException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("Unimplemented exception.");
        }
    }

    public void addFile(String fileName)
    {
        node(fileName);
    }

    public String getNote(String file)
    {
        return node(file).get("note", null);
    }

    public String[] getFileTypes()
    {
        String[] types = null;
        String fileTypes = node("populate_options").get("filetypes", null);
        if (fileTypes != null)
        {
            types = fileTypes.split(",");
            for (int i = 0; i<types.length; i++)
            {
                types[i] = types[i].trim();
            }
        }
        return types;
    }

    // Test a string value, setting new values if existing one in the ini file is empty
    /**
     * @param section
     * @param key
     * @param value - can be string or common primitive that accepts toString() method
     */
    public void testValue(String section, String key, Object value)
    {
        if (value != null && !(value instanceof String))
        {
            value = value.toString();
        }

        if (value != null && value.toString().length() > 255)
        {
            value = value.toString().substring(0, 252) + "...";
        }

        String storedValue = node(section).get(key, null);
        if ((storedValue == null || populate == true) && value != null)
        {
            System.err.println("Storing value for " + section + ": key=" + key + ", value=: " + value);
            node(section).put(key, (String) value);
        } else if ((value!= null && !storedValue.equals(value)) || (value == null && storedValue != null))
        {
            System.err.println("Value mismatch in " + section + ": key=" + key + ", stored value=" + storedValue + " new value=" + value);
        } else if (value == null)
        {
            System.err.println("Skipping null value: " + section + ": key=" + key);
        }
    }
}