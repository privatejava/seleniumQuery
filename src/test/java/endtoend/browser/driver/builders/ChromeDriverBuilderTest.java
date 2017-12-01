/*
 * Copyright (c) 2017 seleniumQuery authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package endtoend.browser.driver.builders;

import static io.github.seleniumquery.SeleniumQuery.$;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeTrue;
import static testinfrastructure.testutils.EnvironmentTestUtils.isNotWindowsOS;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import endtoend.helpers.BrowserAgentTestHelper;
import testinfrastructure.junitrule.SetUpAndTearDownDriver;

public class ChromeDriverBuilderTest {

    // https://code.google.com/p/chromium/codesearch#chromium/src/chrome/test/chromedriver/chrome/mobile_device_list.cc
    private static final String CHROME_MOBILE_EMULATION_DEVICE = "iPad";
    private static final String WINDOWS_TEST_CHROMEDRIVER_LOCATION = "D:\\usr\\local\\bin\\chromedriver.exe";
    private static final String LINUX_TEST_CHROMEDRIVER_LOCATION = "/usr/local/bin/chromedriver";

    @Before
    public void setUp() {
        assumeTrue(SetUpAndTearDownDriver.driverToRunTestsIn.canRunChrome());
    }

    @After
    public void tearDown() {
        $.quit();
    }

    @Test
    public void withOptions() {
        // given
        ChromeOptions options = createChromeOptionsWithMobileEmulation();
        // when
        $.driver().useChrome().withOptions(options);
        // then
        BrowserAgentTestHelper.assertBrowserAgent(containsString(CHROME_MOBILE_EMULATION_DEVICE));
    }

    @Test
    public void withCapabilities() {
        // given
        ChromeOptions options = createChromeOptionsWithMobileEmulation();

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        // when
        $.driver().useChrome().withCapabilities(capabilities);
        // then
        BrowserAgentTestHelper.assertBrowserAgent(containsString(CHROME_MOBILE_EMULATION_DEVICE));
    }

    private ChromeOptions createChromeOptionsWithMobileEmulation() {
        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", CHROME_MOBILE_EMULATION_DEVICE);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("mobileEmulation", mobileEmulation);
        return options;
    }

    @Test
    public void withoutCapabilities() {
        // given
        // when
        $.driver().useChrome();
        // then
        BrowserAgentTestHelper.assertBrowserAgent(not(containsString(CHROME_MOBILE_EMULATION_DEVICE)));
    }

    @Test
    public void withCapabilities__should_return_the_current_ChromeDriverBuilder_instance_to_allow_further_chaining() {
        $.driver().useChrome().withCapabilities(null).withOptions(null); // should compile
    }

    @Test
    public void withPathToChromeDriver() {
        // given
        // so this test is really effective, the chromedriver executable shouldnt be in $PATH
        $.driver().useChrome().withPathToChromeDriver(getChromeDriverExecutablePath());
        // when
        BrowserAgentTestHelper.openBrowserAgentTestHelperUrl();
        // then
        // no exception is thrown while opening a page
    }

    private String getChromeDriverExecutablePath() {
        if (isNotWindowsOS()) {
            return LINUX_TEST_CHROMEDRIVER_LOCATION;
        } else {
            return WINDOWS_TEST_CHROMEDRIVER_LOCATION;
        }
    }

    @Test
    public void useChrome__should_fall_back_to_systemProperty_when_executable_not_found_in_classpath() {
        // given
        // so this test is really effective, the chromedriver executable shouldnt be in $PATH
        System.setProperty("webdriver.chrome.driver", getChromeDriverExecutablePath());
        // when
        $.driver().useChrome();
        BrowserAgentTestHelper.openBrowserAgentTestHelperUrl();
        // then
        // no exception is thrown while opening a page
    }

    @Test
    public void headlessChrome() {
        // when
        $.driver().useChrome().withHeadlessChrome();
        // then
        BrowserAgentTestHelper.assertBrowserAgent(containsString("HeadlessChrome"));
    }

}
