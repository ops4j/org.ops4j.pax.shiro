/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ops4j.pax.shiro.test;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

public class ContainerIntegrationTest extends AbstractContainerTest {

    @Before
    public void logOut() throws IOException {
        // Make sure we are logged out
        final HtmlPage homePage = webClient.getPage(getBaseUri());
        try {
            homePage.getAnchorByHref("/logout.jsf").click();
        }
        catch (ElementNotFoundException e) {
            //Ignore
        }
    }

    @Test
    public void logIn() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {

        HtmlPage page = webClient.getPage(getBaseUri() + "login.jsf");
        HtmlForm form = page.getFormByName("login");
        form.<HtmlInput>getInputByName("login:username").setValueAttribute("root");
        form.<HtmlInput>getInputByName("login:password").setValueAttribute("secret");
        page = form.<HtmlInput>getInputByName("login:submit").click();
        // This'll throw an expection if not logged in
        page.getAnchorByHref("/logout.jsf");
    }

    @Test
    public void logInAndRememberMe() throws Exception {
        HtmlPage page = webClient.getPage(getBaseUri() + "login.jsf");
        HtmlForm form = page.getFormByName("login");
        form.<HtmlInput>getInputByName("login:username").setValueAttribute("root");
        form.<HtmlInput>getInputByName("login:password").setValueAttribute("secret");
        HtmlCheckBoxInput checkbox = form.getInputByName("login:rememberMe");
        checkbox.setChecked(true);
        page = form.<HtmlInput>getInputByName("login:submit").click();
        server.stop();
        server.start();
        page = webClient.getPage(getBaseUri());
        // page.getAnchorByHref("/logout");
        WebAssert.assertLinkPresentWithText(page, "Log out");
        page = page.getAnchorByHref("/account/home.jsf").click();
        // login page should be shown again - user remembered but not authenticated
        WebAssert.assertFormPresent(page, "login");
    }

}
