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

public class FacesBundleTest {

    @Rule
    public PaxExamServer exam = new PaxExamServer();

    private String port = System.getProperty("pax.shiro.itest.http.port", "18181");
    
    private WebClient webClient = new WebClient();

    

    @Configuration
    public Option[] configuration() {
        return options(frameworkProperty("felix.bootdelegation.implicit").value("false"),
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
            mavenBundle("org.ops4j.pax.web", "pax-web-spi").version("3.0.2"),
            mavenBundle("org.ops4j.pax.web", "pax-web-api").version("3.0.2"),
            mavenBundle("org.ops4j.pax.web", "pax-web-extender-war").version("3.0.2"),
            mavenBundle("org.ops4j.pax.web", "pax-web-extender-whiteboard").version("3.0.2"),
            mavenBundle("org.ops4j.pax.web", "pax-web-jetty").version("3.0.2"),
            mavenBundle("org.ops4j.pax.web", "pax-web-runtime").version("3.0.2"),
            mavenBundle("org.ops4j.pax.web", "pax-web-jsp").version("3.0.2"),
            mavenBundle("org.eclipse.jdt.core.compiler", "ecj").version("3.5.1"),
            mavenBundle("org.eclipse.jetty", "jetty-util").version("8.1.4.v20120524"),
            mavenBundle("org.eclipse.jetty", "jetty-io").version("8.1.4.v20120524"),
            mavenBundle("org.eclipse.jetty", "jetty-http").version("8.1.4.v20120524"),
            mavenBundle("org.eclipse.jetty", "jetty-continuation").version("8.1.4.v20120524"),
            mavenBundle("org.eclipse.jetty", "jetty-server").version("8.1.4.v20120524"),
            mavenBundle("org.eclipse.jetty", "jetty-security").version("8.1.4.v20120524"),
            mavenBundle("org.eclipse.jetty", "jetty-xml").version("8.1.4.v20120524"),
            mavenBundle("org.eclipse.jetty", "jetty-servlet").version("8.1.4.v20120524"),
            mavenBundle("org.apache.geronimo.specs", "geronimo-servlet_3.0_spec").version("1.0"),
            mavenBundle("org.osgi", "org.osgi.compendium", "4.3.1"),

            mavenBundle("org.slf4j", "slf4j-api", "1.6.4"),
            mavenBundle("org.slf4j", "jcl-over-slf4j", "1.6.4"),
            mavenBundle("ch.qos.logback", "logback-classic", "1.0.0"),
            mavenBundle("ch.qos.logback", "logback-core", "1.0.0"),

            mavenBundle("commons-beanutils", "commons-beanutils", "1.8.3"),
            mavenBundle("commons-collections", "commons-collections", "3.2.1"),
            mavenBundle("org.apache.shiro", "shiro-web", "1.2.2"),
            mavenBundle("org.apache.shiro", "shiro-core", "1.2.2"),

            mavenBundle("org.apache.myfaces.core", "myfaces-impl", "2.1.12"),
            mavenBundle("org.apache.myfaces.core", "myfaces-api", "2.1.12"),
            mavenBundle("org.apache.geronimo.specs", "geronimo-annotation_1.1_spec").version("1.0.1"),
            mavenBundle("org.apache.geronimo.specs", "geronimo-validation_1.0_spec").version("1.1"),
            mavenBundle("commons-codec", "commons-codec", "1.7"),
            mavenBundle("commons-digester", "commons-digester", "1.8.1"),

            mavenBundle("org.ops4j.pax.shiro.samples", "sample-faces-bundle", "0.1.0-SNAPSHOT")

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

        HtmlPage page = webClient.getPage(getBaseUri() + "login.jsf");
        HtmlForm form = page.getFormByName("login");
        form.<HtmlInput>getInputByName("login:username").setValueAttribute("root");
        form.<HtmlInput>getInputByName("login:password").setValueAttribute("secret");
        page = form.<HtmlInput>getInputByName("login:submit").click();

        // This'll throw an exception if not logged in
        page.getAnchorByHref("/sample-faces-bundle/logout.jsf");
    }
    
    private String getBaseUri() {
        return "http://localhost:" + port + "/sample-faces-bundle/";
    }    
}
