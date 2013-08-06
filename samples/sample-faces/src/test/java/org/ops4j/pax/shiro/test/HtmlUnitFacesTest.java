/*
 * Copyright 2013 Harald Wellmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ops4j.pax.shiro.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HtmlUnitFacesTest extends AbstractHtmlUnitContainerTest {
    
    private WebDriver webDriver = new HtmlUnitDriver();
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void logOut() throws IOException {
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
    public void shouldRememberMeOnServerRestart() throws Exception {

        webDriver.get(getBaseUri() + "login.jsf");
        webDriver.findElement(By.name("login:username")).sendKeys("root");
        webDriver.findElement(By.name("login:password")).sendKeys("secret");
        webDriver.findElement(By.name("login:rememberMe")).click();
        webDriver.findElement(By.name("login:submit")).click();

        
        server.stop();
        server.start();
        
        webDriver.get(getBaseUri());
        webDriver.findElement(By.partialLinkText("Log out"));

        webDriver.findElement(By.partialLinkText("account")).click();

        // login page should be shown again - user remembered but not authenticated
        webDriver.findElement(By.name("login:username"));
        
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
}
