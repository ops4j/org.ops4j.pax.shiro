/*
 * Copyright 2013 Harald Wellmann
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.ops4j.pax.shiro.itest.osgi;

import static org.ops4j.pax.exam.CoreOptions.frameworkProperty;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExamServer;
import org.ops4j.pax.exam.util.PathUtils;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class JspBundleTest {
    
    private static final String PAX_WEB_VERSION = "3.0.2";
    private static final String JETTY_VERSION = "8.1.4.v20120524";

    @Rule
    public PaxExamServer exam = new PaxExamServer();

    private String port = System.getProperty("pax.shiro.itest.http.port", "18181");
    
    private WebClient webClient = new WebClient();

    

    @Configuration
    public Option[] configuration() {
        return options(
            frameworkProperty("osgi.console").value("6666"),
            frameworkProperty("org.osgi.service.http.port").value(port),

            // Set logback configuration via system property.
            // This way, both the driver and the container use the same configuration
            systemProperty("logback.configurationFile").value(
                "file:" + PathUtils.getBaseDir() + "/src/test/resources/logback.xml"),

            mavenBundle("org.ops4j.base", "ops4j-base-lang").version("1.4.0"),
            mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-core").version("1.6.0"),
            mavenBundle("org.apache.xbean", "xbean-asm-shaded", "3.12"),
            mavenBundle("org.apache.xbean", "xbean-finder-shaded", "3.12"),
            mavenBundle("org.ops4j.pax.web", "pax-web-spi").version(PAX_WEB_VERSION),
            mavenBundle("org.ops4j.pax.web", "pax-web-api").version(PAX_WEB_VERSION),
            mavenBundle("org.ops4j.pax.web", "pax-web-extender-war").version(PAX_WEB_VERSION),
            mavenBundle("org.ops4j.pax.web", "pax-web-extender-whiteboard").version(PAX_WEB_VERSION),
            mavenBundle("org.ops4j.pax.web", "pax-web-jetty").version(PAX_WEB_VERSION),
            mavenBundle("org.ops4j.pax.web", "pax-web-runtime").version(PAX_WEB_VERSION),
            mavenBundle("org.ops4j.pax.web", "pax-web-jsp").version(PAX_WEB_VERSION),
            mavenBundle("org.eclipse.jdt.core.compiler", "ecj").version("3.5.1"),
            mavenBundle("org.eclipse.jetty", "jetty-util").version(JETTY_VERSION),
            mavenBundle("org.eclipse.jetty", "jetty-io").version(JETTY_VERSION),
            mavenBundle("org.eclipse.jetty", "jetty-http").version(JETTY_VERSION),
            mavenBundle("org.eclipse.jetty", "jetty-continuation").version(JETTY_VERSION),
            mavenBundle("org.eclipse.jetty", "jetty-server").version(JETTY_VERSION),
            mavenBundle("org.eclipse.jetty", "jetty-security").version(JETTY_VERSION),
            mavenBundle("org.eclipse.jetty", "jetty-xml").version(JETTY_VERSION),
            mavenBundle("org.eclipse.jetty", "jetty-servlet").version(JETTY_VERSION),
            mavenBundle("org.apache.geronimo.specs", "geronimo-servlet_3.0_spec").version("1.0"),
            mavenBundle("org.osgi", "org.osgi.compendium", "4.3.1"),

            mavenBundle("org.slf4j", "slf4j-api").versionAsInProject(),
            mavenBundle("org.slf4j", "jcl-over-slf4j").versionAsInProject(),
            mavenBundle("ch.qos.logback", "logback-classic").versionAsInProject(),
            mavenBundle("ch.qos.logback", "logback-core").versionAsInProject(),

            mavenBundle("commons-beanutils", "commons-beanutils", "1.8.3"),
            mavenBundle("commons-collections", "commons-collections", "3.2.1"),
            mavenBundle("org.apache.shiro", "shiro-web", "1.2.2"),
            mavenBundle("org.apache.shiro", "shiro-core", "1.2.2"),

            mavenBundle("org.ops4j.pax.shiro.samples", "sample-jsp-bundle").versionAsInProject()

        );
    }

    @Before
    public void logOut() throws IOException, InterruptedException {
        
        // wait for server to come up
        Thread.sleep(2000);
        
        // make sure we are logged out
        HtmlPage homePage = webClient.getPage(getBaseUri());
        try {
            homePage.getAnchorByHref("/logout").click();
        }
        catch (ElementNotFoundException exc) {
            // ignore
        }
    }

    @Test
    public void logIn() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {

        HtmlPage page = webClient.getPage(getBaseUri() + "login.jsp");
        HtmlForm form = page.getFormByName("loginform");
        form.<HtmlInput>getInputByName("username").setValueAttribute("root");
        form.<HtmlInput>getInputByName("password").setValueAttribute("secret");
        page = form.<HtmlInput>getInputByName("submit").click();

        // This'll throw an exception if not logged in
        page.getAnchorByHref("/sample-jsp-bundle/logout");
    }
    
    private String getBaseUri() {
        return "http://localhost:" + port + "/sample-jsp-bundle/";
    }    
}
