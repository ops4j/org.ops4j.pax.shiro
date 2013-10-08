/*
 * $Id: JavaTemplates.xml 53870 2013-02-12 10:32:44Z tlangfeld $
 */
package org.ops4j.pax.shiro.faces.tags;

import javax.faces.component.FacesComponent;

/**
 * Tag that renders the tag body only if the current user has the string permissions
 * specified in <tt>name</tt> attribute.
 */
@FacesComponent("org.ops4j.pax.shiro.component.HasPermission")
public class HasPermissionComponent extends ShiroComponent {
    
    protected String readName() {
        return (String) getAttributes().get("name");
    }

    
    @Override
    public boolean isRendered() {
        return getSubject() != null && getSubject().isPermitted(readName());
    }
}
