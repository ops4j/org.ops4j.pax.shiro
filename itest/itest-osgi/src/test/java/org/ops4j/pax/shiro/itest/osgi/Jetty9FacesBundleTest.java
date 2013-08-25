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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.frameworkProperty;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import java.io.IOException;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExamServer;
import org.ops4j.pax.exam.util.PathUtils;

/**
 * Tests shiro-faces in OSGi mode. 
 * <p>
 * JSF support in Jetty requires a custom extension fragment sample-jetty-osgi-boot-jsf.
 * 
 * @author Harald Wellmann
 *
 */
public class Jetty9FacesBundleTest {

    @ClassRule
    public static PaxExamServer exam = new PaxExamServer();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private WebDriver webDriver = new HtmlUnitDriver();

    private String port = System.getProperty("pax.shiro.itest.http.port", "18181");


    @Configuration
    public Option[] configuration() {
        return options(
            frameworkProperty("osgi.console").value("6666"),
            frameworkProperty("osgi.compatibility.bootdelegation").value("true"),
            systemProperty("jetty.home.bundle").value("org.eclipse.jetty.osgi.boot"),
            systemProperty("jetty.port").value(port),
            systemProperty("org.eclipse.jetty.osgi.tldbundles").value(
                "org.apache.myfaces.core.impl"),

            // Set logback configuration via system property.
            // This way, both the driver and the container use the same configuration
            systemProperty("logback.configurationFile").value(
                "file:" + PathUtils.getBaseDir() + "/src/test/resources/logback.xml"),

            mavenBundle("org.slf4j", "slf4j-api", "1.6.4"),
            mavenBundle("org.slf4j", "jcl-over-slf4j", "1.6.4"),
            mavenBundle("ch.qos.logback", "logback-classic", "1.0.0"),
            mavenBundle("ch.qos.logback", "logback-core", "1.0.0"),

            mavenBundle("org.eclipse.jetty.osgi", "jetty-osgi-boot").versionAsInProject(),
            mavenBundle("org.eclipse.jetty.osgi", "jetty-osgi-boot-jsp").versionAsInProject()
                .noStart(),
            mavenBundle("org.ops4j.pax.shiro.samples", "sample-jetty-osgi-boot-jsf").versionAsInProject().noStart(),

            mavenBundle("org.eclipse.jetty", "jetty-deploy").versionAsInProject(),
            mavenBundle("org.eclipse.jetty", "jetty-http").versionAsInProject(),
            mavenBundle("org.eclipse.jetty", "jetty-io").versionAsInProject(),
            mavenBundle("org.eclipse.jetty", "jetty-jndi").versionAsInProject(),
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
            mavenBundle("org.eclipse.jetty.orbit", "javax.mail.glassfish", "1.4.1.v201005082020"),
            mavenBundle("org.eclipse.jetty.orbit", "javax.servlet.jsp").versionAsInProject(),
            mavenBundle("org.eclipse.jetty.orbit", "javax.servlet.jsp.jstl").versionAsInProject(),
            mavenBundle("org.eclipse.jetty.orbit", "org.apache.jasper.glassfish")
                .versionAsInProject(),
            mavenBundle("org.eclipse.jetty.orbit", "org.apache.taglibs.standard.glassfish",
                "1.2.0.v201112081803"),
            mavenBundle("org.eclipse.jetty.orbit", "org.eclipse.jdt.core", "3.8.2.v20130121"),

            mavenBundle("commons-beanutils", "commons-beanutils", "1.8.3"),
            mavenBundle("commons-collections", "commons-collections", "3.2.1"),
            mavenBundle("org.apache.shiro", "shiro-web", "1.2.2"),
            mavenBundle("org.apache.shiro", "shiro-core", "1.2.2"),

            mavenBundle("org.apache.myfaces.core", "myfaces-api", "2.1.12"),
            bundle("reference:file:" + PathUtils.getBaseDir() + "/target/myfaces-impl.jar"),

            mavenBundle("org.apache.geronimo.specs", "geronimo-annotation_1.1_spec", "1.0.1"),
            mavenBundle("org.apache.geronimo.specs", "geronimo-validation_1.0_spec", "1.1"),
            mavenBundle("commons-codec", "commons-codec", "1.7"),
            mavenBundle("commons-digester", "commons-digester", "1.8.1"),

            mavenBundle("org.ops4j.pax.shiro", "pax-shiro-faces").versionAsInProject(),
            mavenBundle("org.ops4j.pax.shiro.samples", "sample-faces-bundle").versionAsInProject()

        );
    }

    @Before
    public void logOut() throws IOException, InterruptedException {
        // wait for server to come up
        Thread.sleep(2000);

        // Make sure we are logged out
        webDriver.get(getBaseUri());
        try {
            webDriver.findElement(By.partialLinkText("Log out")).click();
        }
        catch (NoSuchElementException e) {
            // Ignore
        }
    }

    @Test
    public void logIn() {

        webDriver.get(getBaseUri() + "login.jsf");
        webDriver.findElement(By.name("login:username")).sendKeys("root");
        webDriver.findElement(By.name("login:password")).sendKeys("secret");
        webDriver.findElement(By.name("login:submit")).click();

        // This'll throw an expection if not logged in
        webDriver.findElement(By.partialLinkText("Log out"));
    }

    @Test
    public void shouldRememberMeOnClientRestart() throws Exception {

        webDriver.get(getBaseUri() + "login.jsf");
        webDriver.findElement(By.name("login:username")).sendKeys("root");
        webDriver.findElement(By.name("login:password")).sendKeys("secret");
        webDriver.findElement(By.name("login:rememberMe")).click();
        webDriver.findElement(By.name("login:submit")).click();

        Cookie cookie = webDriver.manage().getCookieNamed("rememberMe");
        webDriver.close();

        webDriver = new HtmlUnitDriver();
        webDriver.get(getBaseUri());
        webDriver.manage().addCookie(cookie);

        webDriver.get(getBaseUri());
        webDriver.findElement(By.partialLinkText("Log out"));

        webDriver.findElement(By.partialLinkText("account")).click();

        // login page should be shown again - user remembered but not authenticated
        webDriver.findElement(By.name("login:username"));
    }

    @Test
    public void shouldNotRememberMeWithoutCookie() throws Exception {

        webDriver.get(getBaseUri() + "login.jsf");
        webDriver.findElement(By.name("login:username")).sendKeys("root");
        webDriver.findElement(By.name("login:password")).sendKeys("secret");
        webDriver.findElement(By.name("login:rememberMe")).click();
        webDriver.findElement(By.name("login:submit")).click();

        Cookie cookie = webDriver.manage().getCookieNamed("rememberMe");
        assertThat(cookie, is(notNullValue()));
        webDriver.close();

        webDriver = new HtmlUnitDriver();
        webDriver.get(getBaseUri());

        thrown.expect(NoSuchElementException.class);
        webDriver.findElement(By.partialLinkText("Log out"));
    }

    private String getBaseUri() {
        return "http://localhost:" + port + "/sample-faces-bundle/";
    }
}
