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

package org.ops4j.pax.shiro.cdi;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.webbeans.cditest.CdiTestContainer;
import org.apache.webbeans.cditest.CdiTestContainerLoader;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public abstract class CDITest {
    private static CdiTestContainer container;
    protected static DefaultSecurityManager securityManager;
    protected static SimpleAccountRealm realm;

    @BeforeClass
    public static void start() throws Exception {
        securityManager = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(securityManager);

        container = CdiTestContainerLoader.getCdiContainer();
        container.bootContainer();

        realm = new SimpleAccountRealm("test-realm");
        realm.addRole("role");
        realm.addAccount("foo", "bar", "role");
        realm.addAccount("bilbo", "precious", "hobbit");
        realm.setRolePermissionResolver(new RolePermissionResolver() {
            public Collection<Permission> resolvePermissionsInRole(String roleString) {
                if ("role".equals(roleString)) {
                    final Permission dp = new WildcardPermission("permission");
                    return Arrays.asList(dp);
                }
                return Collections.emptyList();
            }
        });
        securityManager.setRealm(realm);
    }

    @AfterClass
    public static void close() throws Exception {
        container.shutdownContainer();
        SecurityUtils.setSecurityManager(null);
    }

    protected static <T> T bean(Class<T> clazz) {
        return container.getInstance(clazz);
    }
}
