package org.openqa.selenium.browserlaunchers;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Proxy;

/**
 * This is a workaround for PhantomJS to work with Selenium 2.44.0. Until a newer version of
 * GhostDriver is released, this will be needed.
 *
 * See: https://github.com/detro/ghostdriver/issues/397
 *      https://github.com/detro/ghostdriver/pull/399
 */
public class Proxies {

    public static Proxy extractProxy(Capabilities capabilities) {
        return Proxy.extractFrom(capabilities);
    }

}