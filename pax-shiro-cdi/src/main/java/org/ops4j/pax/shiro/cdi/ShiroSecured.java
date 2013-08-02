/*
 * $Id: JavaTemplates.xml 36713 2011-08-02 10:04:03Z hwellmann $
 */
package org.ops4j.pax.shiro.cdi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

/**
 * Interceptor binding for declarative security checks using the annotations from the 
 * {@code org.apache.shiro.authz.annotation} package.
 * <p>
 * Usage:
 * <ul>
 * <li>Enable the {@code org.apache.shiro.cdi.interceptor.ShiroInterceptor} in {@code beans.xml}.</li>
 * <li>Add authorization annotations (e.g. {@code @RequiresRoles("admin")}) to classes or
 * methods you want to protect.</li>
 * <li>Add {@code @ShiroSecured} to the given classes to enable the interceptor.</li>
 * </ul>
 * The secured methods will fail with an {@code AuthorizationException} if the current subject
 * does not match the security constraints.
 * 
 * @author Harald Wellmann
 *
 */
@Inherited
@InterceptorBinding
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ShiroSecured {
    //
}
