/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.ops4j.pax.shiro.cdi.impl;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.spi.BeanManager;

import org.apache.webbeans.cditest.CdiTestContainer;
import org.apache.webbeans.cditest.CdiTestContainerLoader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ops4j.pax.shiro.cdi.impl.NamedBeanMap;

public class NamedBeanMapTest {
    private static CdiTestContainer container;

    @BeforeClass
    public static void start() throws Exception {
        container = CdiTestContainerLoader.getCdiContainer();
        container.bootContainer();
        container.startContexts();
    }

    @AfterClass
    public static void close() throws Exception {
        container.stopContexts();
        container.shutdownContainer();
    }

    protected static <T> T bean(Class<T> clazz) {
        return container.getInstance(clazz);
    }
    
    @Test
    public void findNamedBeans() {
        BeanManager beanManager = container.getBeanManager();
        NamedBeanMap namedBeanMap = new NamedBeanMap(beanManager);
        assertThat(namedBeanMap.isEmpty(), is(false));
        
        List<String> origins = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : namedBeanMap.entrySet()) {
            Object object = entry.getValue();
            if (object instanceof Food) {
                Food food = (Food) object;
                origins.add(food.getOrigin());
            }
        }
        assertThat(origins.size(), is(3));
        assertThat(origins, hasItems("Germany", "India", "Italy"));
    }
}
