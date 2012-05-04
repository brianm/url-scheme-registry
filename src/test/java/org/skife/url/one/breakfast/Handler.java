package org.skife.url.one.breakfast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler
{
    @Override
    protected URLConnection openConnection(URL u) throws IOException
    {
        final String breakfast = u.getHost();
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
