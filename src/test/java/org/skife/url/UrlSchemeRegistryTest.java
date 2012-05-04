package org.skife.url;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;

public class UrlSchemeRegistryTest
{

    @Test
    public void testRegisterHandler() throws Exception
    {
        UrlSchemeRegistry.register("dinner", DinnerHandler.class);

        assertThat(read(new URL("dinner://steak"))).isEqualTo("steak");
    }

    @Test(expected = IllegalStateException.class)
    public void testOnlyAllowsOneSchemePerUrl() throws Exception
    {
        UrlSchemeRegistry.register("dinner", DinnerHandler.class);
        UrlSchemeRegistry.register("dinner", DinnerHandler2.class);
    }



    /* Tests for internal package registration logic -- registerPackage is NOT public API */

    @Test
    public void testSinglePackageRegistration() throws Exception
    {
        UrlSchemeRegistry.registerPackage("org.skife.url.one");
        URL url = new URL("breakfast://pancakes");
        Reader in = new InputStreamReader(url.openStream(), Charsets.UTF_8);
        String body = CharStreams.toString(in);
        in.close();

        assertThat(body).isEqualTo("pancakes");
    }

    @Test
    public void testMultiplePackageRegistration() throws Exception
    {
        UrlSchemeRegistry.registerPackage("org.skife.url.one");
        UrlSchemeRegistry.registerPackage("org.skife.url.two");


        assertThat(read(new URL("breakfast://pancakes"))).isEqualTo("pancakes");
        assertThat(read(new URL("lunch://sammy"))).isEqualTo("sammy");
    }

    private static String read(URL url) throws IOException
    {
        Reader r = new InputStreamReader(url.openStream(), Charsets.UTF_8);
        String body = CharStreams.toString(r);
        r.close();
        return body;
    }
}
