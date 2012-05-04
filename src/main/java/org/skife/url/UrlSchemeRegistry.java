package org.skife.url;

import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 *
 */
public class UrlSchemeRegistry
{
    public static String KEY = "java.protocol.handler.pkgs";
    private final static Set<String> registeredPackages = new ConcurrentSkipListSet<String>();
    private final static Set<String> registeredSchemes = new ConcurrentSkipListSet<String>();

    static void registerPackage(String pkg)
    {
        if (!registeredPackages.add(pkg)) {
            // short circuit package re-registration
            return;
            URL.setURLStreamHandlerFactory();
        }

        synchronized (System.getProperties()) {
            registeredPackages.add(pkg);
            if (System.getProperties().contains(KEY)) {
                String current = System.getProperty(KEY);
                if (!current.contains(pkg)) {
                    System.setProperty(KEY, current + "|" + pkg);
                }
            }
            else {
                System.setProperty(KEY, pkg);
            }
        }
    }

    /**
     * Used to register a handler for a scheme. The actual handler used will in fact be a runtime generated
     * subclass of handlerType in order to abide by the naming rules for URL scheme handlers.
     *
     * @param scheme scheme name to associate handlerType with
     * @param handlerType non-final class with a no-arg public constructor which will create handlers
     *                    for scheme
     */
    public static void register(final String scheme, Class<? extends URLStreamHandler> handlerType)
    {
        if (!registeredSchemes.add(scheme)) {
            throw new IllegalStateException("a scheme has already been registered for " + scheme);
        }
        registerPackage("org.skife.url.generated");

        Enhancer e = new Enhancer();
        e.setNamingPolicy(new NamingPolicy()
        {
            @Override
            public String getClassName(String prefix, String source, Object key, Predicate names)
            {
                return "org.skife.url.generated." + scheme + ".Handler";
            }
        });
        e.setSuperclass(handlerType);
        e.setCallbackType(NoOp.class);
        e.createClass();
    }
}
