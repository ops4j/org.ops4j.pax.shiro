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
import static org.ops4j.pax.exam.CoreOptions.frameworkProperty;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import java.io.IOException;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
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
 * Some tests currently fail due to incomplete JSF support in Pax Web.
 * Tags from other bundles do not get recognized and are rendered verbatim.
 * 
 * @author Harald Wellmann
 *
 */
public class FacesBundleTest {

    private static final String PAX_WEB_VERSION = "3.0.2";
    private static final String JETTY_VERSION = "8.1.4.v20120524";

    /** We use a class rule to start Pax Web only once. */
    @ClassRule
    public static PaxExamServer exam = new PaxExamServer();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private WebDriver webDriver = new HtmlUnitDriver();
    
    private String port = System.getProperty("pax.shiro.itest.http.port", "18181");
        

    @Configuration
    public Option[] configuration() {
        return options(frameworkProperty("felix.bootdelegation.implicit").value("false"),
            frameworkProperty("osgi.console").value("6666"),
            frameworkProperty("org.osgi.service.http.port").value(port),

            // Set logback configuration via system property.
            // This way, both the driver and the container use the same configuration
            systemProperty("logback.configurationFile").value(
                "file:" + PathUtils.getBaseDir() + "/src/test/resources/logback.xml"),

            mavenBundle("org.ops4j.pax.url", "pax-url-commons").version("1.6.0"),
            mavenBundle("org.ops4j.pax.url", "pax-url-classpath").version("1.6.0"),
            mavenBundle("org.ops4j.base", "ops4j-base-lang").version("1.4.0"),
            mavenBundle("org.ops4j.base", "ops4j-base-util-property").version("1.4.0"),
            mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-core").version("1.7.0"),
            mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-property").version("1.7.0"),
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
            mavenBundle("org.apache.shiro", "shiro-web").versionAsInProject(),
            mavenBundle("org.apache.shiro", "shiro-core").versionAsInProject(),

            mavenBundle("org.apache.myfaces.core", "myfaces-impl").versionAsInProject(),
            mavenBundle("org.apache.myfaces.core", "myfaces-api").versionAsInProject(),
            mavenBundle("org.apache.geronimo.specs", "geronimo-annotation_1.1_spec").version("1.0.1"),
            mavenBundle("org.apache.geronimo.specs", "geronimo-validation_1.0_spec").version("1.1"),
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
            //Ignore
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
    @Ignore("missing JSF support in Pax Web")
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

        // fails since tags get rendered verbatim, so we do see the
        // content that should be hidden
        thrown.expect(NoSuchElementException.class);
        webDriver.findElement(By.partialLinkText("Log out"));
    }    
    
    

    private String getBaseUri() {
        return "http://localhost:" + port + "/sample-faces-bundle/";
    }    
}
