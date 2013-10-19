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

package org.ops4j.pax.shiro.samples.web.cdi;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.shiro.authc.credential.PasswordService;
import org.ops4j.pax.shiro.cdi.ShiroIni;

/**
 * Simple memory-based user repository. Two users are inserted when this bean is created. The
 * passwords get encrypted using an injected PasswordService.
 * 
 * @author Harald Wellmann
 * 
 */
@ApplicationScoped
public class UserRepository {

    private Map<String, User> users = new HashMap<String, User>();

    @Inject
    @ShiroIni
    private PasswordService passwordService;

    @PostConstruct
    public void init() {
        createUser("admin", "secret", "Administrator");
        createUser("user", "changeme", "Mary Smith");
    }

    public void createUser(String username, String rawPassword, String displayName) {
        User user = new User(username, passwordService.encryptPassword(rawPassword));
        user.setDisplayName(displayName);
        users.put(username, user);
    }

    public User findUser(String username) {
        return users.get(username);
    }
}
