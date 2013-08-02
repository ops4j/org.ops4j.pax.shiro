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

import static org.apache.shiro.SecurityUtils.getSecurityManager;
import static org.apache.shiro.SecurityUtils.getSubject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.shiro.ShiroException;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

@ApplicationScoped
public class ShiroFactory {

    @Produces
    public Subject subject() {
        return proxy(Subject.class, new SubjectInvocationhandler());
    }

    @Produces
    public SecurityManager securityManager() {
        return proxy(SecurityManager.class, new SecurityManagerInvocationhandler());
    }

    @Produces
    public Session session() {
        return proxy(Session.class, new SessionInvocationhandler());
    }

    @SuppressWarnings("unchecked")
    private <T> T proxy(final Class<T> clazz, final InvocationHandler ih) {
        return (T) Proxy
            .newProxyInstance(getClass().getClassLoader(), new Class<?>[] { clazz }, ih);
    }

    private static class SubjectInvocationhandler extends Handler {

        public Object handlerInvoke(final Object proxy, final Method method, final Object[] args) {
            try {
                return method.invoke(getSubject(), args);
            }
            catch (IllegalAccessException exc) {
                throw new ShiroException(exc);
            }
            catch (IllegalArgumentException exc) {
                throw new ShiroException(exc);
            }
            catch (InvocationTargetException exc) {
                throw new ShiroException(exc);
            }
        }
    }

    private class SecurityManagerInvocationhandler extends Handler {

        public Object handlerInvoke(Object proxy, Method method, Object[] args) {
            try {
                return method.invoke(getSecurityManager(), args);
            }
            catch (UnavailableSecurityManagerException exc) {
                throw new ShiroException(exc);
            }
            catch (IllegalAccessException exc) {
                throw new ShiroException(exc);
            }
            catch (IllegalArgumentException exc) {
                throw new ShiroException(exc);
            }
            catch (InvocationTargetException exc) {
                throw new ShiroException(exc.getCause());
            }
        }
    }

    private class SessionInvocationhandler extends Handler {

        public Object handlerInvoke(Object proxy, Method method, Object[] args) {
            try {
                return method.invoke(getSubject().getSession(), args);
            }
            catch (IllegalAccessException exc) {
                throw new ShiroException(exc);
            }
            catch (IllegalArgumentException exc) {
                throw new ShiroException(exc);
            }
            catch (InvocationTargetException exc) {
                throw new ShiroException(exc.getCause());
            }
        }
    }

    private abstract static class Handler implements InvocationHandler {

        public abstract Object handlerInvoke(Object proxy, Method method, Object[] args);

        public Object invoke(Object proxy, Method method, Object[] args) {
            return handlerInvoke(proxy, method, args);
        }
    }
}
