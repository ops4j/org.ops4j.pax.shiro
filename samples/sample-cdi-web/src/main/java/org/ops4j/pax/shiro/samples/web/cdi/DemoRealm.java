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

import javax.inject.Inject;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.ops4j.pax.shiro.cdi.ShiroIni;

/**
 * Simple realm implementation, delegating to a UserDao.
 * The {@code ShiroIni} annotation ensures we can reference
 * this CDI bean in shiro.ini
 * 
 * @author Harald Wellmann
 * 
 */
@ShiroIni
public class DemoRealm extends AuthenticatingRealm {

    @Inject
    private UserDao userDao;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        User user = userDao.findUser(upToken.getUsername());
        if (user == null) {
            throw new AuthenticationException();
        }
        return new SimpleAccount(user, user.getHashedPassword(), getName());
    }
}
