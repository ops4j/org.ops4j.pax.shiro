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

package org.ops4j.pax.shiro.faces.tags;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import javax.faces.component.FacesComponent;

import org.apache.shiro.ShiroException;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Tag used to print out the String value of a user's default principal, or a specific principal as
 * specified by the tag's attributes.
 * <p/>
 * If no attributes are specified, the tag prints out the {@code toString()} value of the user's
 * default principal. If the {@code type} attribute is specified, the tag looks for a principal with
 * the given type. If the {@code property} attribute is specified, the tag prints the string value
 * of the specified property of the principal. If no principal is found or the user is not
 * authenticated, the tag displays nothing unless a {@code defaultValue} is specified.
 */
@FacesComponent("org.ops4j.pax.shiro.component.Principal")
public class PrincipalComponent extends ShiroOutputComponent {

    enum PropertyKeys {

        /**
         * The type of principal to be retrieved, or null if the default principal should be used.
         */
        type,

        /**
         * The property name to retrieve of the principal, or null if the {@code toString()} value
         * should be used.
         */
        property,

        /**
         * The default value that should be displayed if the user is not authenticated, or no principal
         * is found.
         */
        defaultValue;
    }

    public String getType() {
        return (String) getStateHelper().eval(PropertyKeys.type);
    }

    public void setType(String type) {
        getStateHelper().put(PropertyKeys.type, type);
    }

    public String getProperty() {
        return (String) getStateHelper().eval(PropertyKeys.property);
    }

    public void setProperty(String property) {
        getStateHelper().put(PropertyKeys.property, property);
    }

    public String getDefaultValue() {
        return (String) getStateHelper().eval(PropertyKeys.defaultValue);
    }

    public void setDefaultValue(String defaultValue) {
        getStateHelper().put(PropertyKeys.defaultValue, defaultValue);
    }
    
    @Override
    public Object getValue() {
        String strValue = null;

        if (getSubject() != null) {
            // Get the principal to print out
            Object principal;

            if (getType() == null) {
                principal = getSubject().getPrincipal();
            }
            else {
                principal = getPrincipalFromClassName();
            }

            // Get the string value of the principal
            if (principal != null) {
                if (getProperty() == null) {
                    strValue = principal.toString();
                }
                else {
                    strValue = getPrincipalProperty(principal, getProperty());
                }
            }
        }

        if (strValue == null) {
            strValue = getDefaultValue();
        }
        return strValue;
    }
    
    private Object getPrincipalFromClassName() {
        Object principal = null;

        try {
            Class<?> cls = Class.forName(getType());
            PrincipalCollection principals = getSubject().getPrincipals();
            if (principals != null) {
                principal = principals.oneByType(cls);
            }
        }
        catch (ClassNotFoundException e) {
            log.error("Unable to find class for name [" + getType() + "]");
        }
        return principal;
    }

    private String getPrincipalProperty(Object principal, String _property) {
        String strValue = null;
        boolean foundProperty = false;
        try {
            BeanInfo bi = Introspector.getBeanInfo(principal.getClass());

            // Loop through the properties to get the string value of the specified property
            for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                if (pd.getName().equals(_property)) {
                    Object value = pd.getReadMethod().invoke(principal);
                    strValue = String.valueOf(value);
                    foundProperty = true;
                    break;
                }
            }
        }
        catch (ReflectiveOperationException exc) {
            throw new ShiroException(exc);
        }
        catch (IntrospectionException exc) {
            throw new ShiroException(exc);
        }
        if (!foundProperty) {
            final String message = "Property [" + _property + "] not found in principal of type ["
                + principal.getClass().getName() + "]";
            log.error(message);
            throw new ShiroException(message);
        }
        return strValue;
    }
}
