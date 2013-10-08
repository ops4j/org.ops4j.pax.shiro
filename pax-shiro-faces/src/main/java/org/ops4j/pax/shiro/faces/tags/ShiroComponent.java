/*
 * $Id: JavaTemplates.xml 53870 2013-02-12 10:32:44Z tlangfeld $
 */
package org.ops4j.pax.shiro.faces.tags;

import javax.faces.component.UIComponentBase;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class ShiroComponent extends UIComponentBase {
    
    public static final String COMPONENT_FAMILY = "org.ops4j.pax.shiro";

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    @Override
    public boolean isRendered() {
        return getSubject() != null && getSubject().getPrincipal() != null;
    }

    protected Subject getSubject() {
        return SecurityUtils.getSubject();
    }
    
    protected boolean hasRole(String roleName) {
        return getSubject() != null && getSubject().hasRole(roleName);
    }
    
    protected boolean isPermitted(String p) {
        return getSubject() != null && getSubject().isPermitted(p);
    }
    
    protected boolean isAuthenticated() {
        return getSubject() != null && getSubject().isAuthenticated();
    }
    
    protected boolean isUser() {
        return getSubject() != null && getSubject().getPrincipal() != null;
    }
    
    protected boolean isRemembered() {
        return getSubject() != null && getSubject().isRemembered();        
    }
}
