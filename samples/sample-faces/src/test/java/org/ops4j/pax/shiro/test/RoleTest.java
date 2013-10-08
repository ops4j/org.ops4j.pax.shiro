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

public class RoleTest extends AbstractHtmlUnitContainerTest {
    
    private void openTestPage() {
        webDriver.get(getBaseUri() + "testRoles.jsf");                
    }
    
    @Test
    public void shouldHaveAdminRole() {
        logIn("root", "secret");
        openTestPage();
        
        webDriver.findElement(By.cssSelector(".hasRole"));
        webDriver.findElement(By.cssSelector(".hasRoleComposite"));
        
        assertThat(webDriver.findElements(By.cssSelector(".lacksRole")), is(empty()));
        assertThat(webDriver.findElements(By.cssSelector(".lacksRoleComposite")), is(empty()));
    }

    @Test
    public void shouldNotHaveAdminRole() {
        logIn("lonestarr", "vespa");
        openTestPage();
        
        webDriver.findElement(By.cssSelector(".lacksRole"));
        webDriver.findElement(By.cssSelector(".lacksRoleComposite"));
        
        assertThat(webDriver.findElements(By.cssSelector(".hasRole")), is(empty()));
        assertThat(webDriver.findElements(By.cssSelector(".hasRoleComposite")), is(empty()));
    }

    @Test
    public void goodguyShouldHaveGoodguyOrDarklordRole() {
        logIn("lonestarr", "vespa");
        openTestPage();
        
        webDriver.findElement(By.cssSelector(".hasAnyRole"));
        webDriver.findElement(By.cssSelector(".hasAnyRoleComposite"));
    }

    @Test
    public void darklordShouldHaveGoodguyOrDarklordRole() {
        logIn("darkhelmet", "ludicrousspeed");
        openTestPage();
        
        webDriver.findElement(By.cssSelector(".hasAnyRole"));
        webDriver.findElement(By.cssSelector(".hasAnyRoleComposite"));
    }
}
