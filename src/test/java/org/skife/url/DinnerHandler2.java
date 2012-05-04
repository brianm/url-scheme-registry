package org.skife.url;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class DinnerHandler2 extends URLStreamHandler
{
    @Override
    protected URLConnection openConnection(URL u) throws IOException
    {
        final String breakfast = u.getHost().toUpperCase();
        return new URLConnection(u)
        {
            @Override
            public void connect() throws IOException
            {
            }

            @Override
            public InputStream getInputStream() throws IOException
            {
                return new ByteArrayInputStream(breakfast.getBytes());
            }
        };
    }
}
