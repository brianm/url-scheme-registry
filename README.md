Library to make it easy to register new URL schemes for
java.net.URL. Consider the URL handler:

    public class DinnerHandler extends URLStreamHandler
    {
      @Override
      protected URLConnection openConnection(URL u) throws IOException
      {
        final String breakfast = u.getHost();
        return new URLConnection(u)
        {
          @Override
          public void connect() throws IOException { }

          @Override
          public InputStream getInputStream() throws IOException
          {
              return new ByteArrayInputStream(breakfast.getBytes());
          }
        };
      }
    }

Which can then be registered globally to the dinner scheme like:

    @Test
    public void testRegisterHandler() throws Exception
    {
        UrlSchemeRegistry.register("dinner", DinnerHandler.class);

        assertThat(readBody(new URL("dinner://steak"))).isEqualTo("steak");
    }

The library uses the <code>java.protocol.handler.pkgs</code> system
property and runtime generated classes following the correct package
and class name conventions to accomplish this. It does *not* use
<code>URL.setURLStreamHandlerFactory(...);</code> for this (so code
using this library should run fine inside Tomcat which does use that
method).

Releases are distributed via
[Maven Central](http://search.maven.org/). There are two variants, one
with a dependency on cglib-2.2.2

    <dependency>
        <groupId>org.skife.url</groupId>
        <artifactId>url-scheme-registry</artifactId>
        <version>0.0.1</version>
    </dependency>

or  and one which renamespaces and bundles cglib-2.2.2.

    <dependency>
        <groupId>org.skife.url</groupId>
        <artifactId>url-scheme-registry</artifactId>
        <classifier>nodep</classifier>
        <version>0.0.1</version>
    </dependency>

Good luck!
