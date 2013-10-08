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
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.openqa.selenium.By;

public class PermissionTest extends AbstractHtmlUnitContainerTest {
    
    private void openTestPage() {
        webDriver.get(getBaseUri() + "testPermissions.jsf");                
    }
    
    @Test
    public void rootShouldHaveLightsaberGreenPermission() {
        logIn("root", "secret");
        openTestPage();
        
        webDriver.findElement(By.cssSelector(".hasPermission"));
        webDriver.findElement(By.cssSelector(".hasPermissionComposite"));        
        assertThat(webDriver.findElements(By.cssSelector(".lacksPermission")), is(empty()));
        assertThat(webDriver.findElements(By.cssSelector(".lacksPermissionComposite")), is(empty()));
    }

    @Test
    public void lonestarrShouldHaveLightsaberGreenPermission() {
        logIn("lonestarr", "vespa");
        openTestPage();
        
        webDriver.findElement(By.cssSelector(".hasPermission"));
        webDriver.findElement(By.cssSelector(".hasPermissionComposite"));        
        assertThat(webDriver.findElements(By.cssSelector(".lacksPermission")), is(empty()));
        assertThat(webDriver.findElements(By.cssSelector(".lacksPermissionComposite")), is(empty()));
    }

    @Test
    public void presidentskroobShouldHaveLightsaberGreenPermission() {
        logIn("presidentskroob", "12345");
        openTestPage();
        
        webDriver.findElement(By.cssSelector(".lacksPermission"));
        webDriver.findElement(By.cssSelector(".lacksPermissionComposite"));        
        assertThat(webDriver.findElements(By.cssSelector(".hasPermission")), is(empty()));
        assertThat(webDriver.findElements(By.cssSelector(".hasPermissionComposite")), is(empty()));
    }
}
