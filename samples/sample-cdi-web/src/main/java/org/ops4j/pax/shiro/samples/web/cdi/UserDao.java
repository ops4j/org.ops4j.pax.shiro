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

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.ops4j.pax.shiro.cdi.ShiroIni;


/**
 * @author Harald Wellmann
 *
 */
@Stateless
public class UserDao {
    
    private static Map<String, User> users = new HashMap<String, User>();
    
    @Inject @ShiroIni
    private PasswordService passwordService;
    
    static {
        DefaultPasswordService passwordService = new DefaultPasswordService();
        User user = new User("admin", passwordService.encryptPassword("secret"));
        users.put("admin", user);
    }
    
    
    public void createUser(String username, String rawPassword) {
        User user = new User("admin", passwordService.encryptPassword("secret"));
        users.put("admin", user);
    }
    
    
    public User findUser(String username) {
        return users.get(username);
    }
}
