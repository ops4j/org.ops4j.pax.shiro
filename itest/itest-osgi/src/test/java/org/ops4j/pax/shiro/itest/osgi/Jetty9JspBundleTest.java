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

import static org.ops4j.pax.exam.CoreOptions.bundle;
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

public class Jetty9JspBundleTest {

    @Rule
    public PaxExamServer exam = new PaxExamServer();

    private String port = System.getProperty("pax.shiro.itest.http.port", "18181");

    private WebClient webClient = new WebClient();

    @Configuration
    public Option[] configuration() {
        return options(
            frameworkProperty("osgi.console").value("6666"),
            frameworkProperty("osgi.console.enable.builtin").value("true"),  
            frameworkProperty("osgi.compatibility.bootdelegation").value("true"),
            frameworkProperty("org.osgi.service.http.port").value(port),

            systemProperty("jetty.home.bundle").value("org.eclipse.jetty.osgi.boot"),
            systemProperty("jetty.port").value(port),

            // Set logback configuration via system property.
            // This way, both the driver and the container use the same configuration
            systemProperty("logback.configurationFile").value(
                "file:" + PathUtils.getBaseDir() + "/src/test/resources/logback.xml"),

            mavenBundle("org.slf4j", "slf4j-api").versionAsInProject(),
            mavenBundle("org.slf4j", "jcl-over-slf4j").versionAsInProject(),
            mavenBundle("ch.qos.logback", "logback-classic").versionAsInProject(),
            mavenBundle("ch.qos.logback", "logback-core").versionAsInProject(),

            mavenBundle("org.eclipse.jetty.osgi", "jetty-osgi-boot").versionAsInProject(),
            mavenBundle("org.eclipse.jetty.osgi", "jetty-osgi-boot-jsp").versionAsInProject()
                .noStart(),

            mavenBundle("org.eclipse.jetty", "jetty-deploy").versionAsInProject(),
            mavenBundle("org.eclipse.jetty", "jetty-http").versionAsInProject(),
            mavenBundle("org.eclipse.jetty", "jetty-io").versionAsInProject(),
            mavenBundle("org.eclipse.jetty", "jetty-security").versionAsInProject(),
            mavenBundle("org.eclipse.jetty", "jetty-server").versionAsInProject(),
            mavenBundle("org.eclipse.jetty", "jetty-servlet").versionAsInProject(),
            mavenBundle("org.eclipse.jetty", "jetty-xml").versionAsInProject(),
            mavenBundle("org.eclipse.jetty", "jetty-util").versionAsInProject(),
            mavenBundle("org.eclipse.jetty", "jetty-webapp").versionAsInProject(),
            mavenBundle("org.apache.geronimo.specs", "geronimo-servlet_3.0_spec").version("1.0"),
            mavenBundle("org.osgi", "org.osgi.compendium", "4.3.1"),

            mavenBundle("org.eclipse.jetty.orbit", "com.sun.el").versionAsInProject(),
            mavenBundle("org.eclipse.jetty.orbit", "javax.el", "2.2.0.v201303151357"),
            mavenBundle("org.eclipse.jetty.orbit", "javax.servlet.jsp").versionAsInProject(),
            mavenBundle("org.eclipse.jetty.orbit", "javax.servlet.jsp.jstl").versionAsInProject(),
            mavenBundle("org.eclipse.jetty.orbit", "org.apache.jasper.glassfish").versionAsInProject(),
            mavenBundle("org.eclipse.jetty.orbit", "org.eclipse.jdt.core", "3.8.2.v20130121"),

            mavenBundle("commons-beanutils", "commons-beanutils").versionAsInProject(),
            mavenBundle("commons-collections", "commons-collections").versionAsInProject(),
            mavenBundle("org.apache.shiro", "shiro-core").versionAsInProject(),

            // Jetty/Jasper does not recognize taglibs when the bundle URL does not have a JAR extension
            // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=414387
            bundle("reference:file:" + PathUtils.getBaseDir() + "/target/shiro-web.jar"),
            bundle("reference:file:" + PathUtils.getBaseDir() + "/target/org.apache.taglibs.standard.glassfish.jar"),
            
            
            mavenBundle("org.ops4j.pax.shiro.samples", "sample-jsp-bundle").versionAsInProject());
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
    public void logIn() throws FailingHttpStatusCodeException, MalformedURLException, IOException,
        InterruptedException {

        HtmlPage page = webClient.getPage(getBaseUri() + "login.jsp");
        HtmlForm form = page.getFormByName("loginform");
        form.<HtmlInput> getInputByName("username").setValueAttribute("root");
        form.<HtmlInput> getInputByName("password").setValueAttribute("secret");
        page = form.<HtmlInput> getInputByName("submit").click();

        // This'll throw an exception if not logged in
        page.getAnchorByHref("/sample-jsp-bundle/logout");
    }

    private String getBaseUri() {
        return "http://localhost:" + port + "/sample-jsp-bundle/";
    }
}
