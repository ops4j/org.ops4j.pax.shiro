/*
 * $Id: JavaTemplates.xml 53870 2013-02-12 10:32:44Z tlangfeld $
 */
package org.ops4j.pax.shiro.faces.tags;

import javax.faces.component.FacesComponent;

import org.apache.shiro.util.StringUtils;

@FacesComponent("org.ops4j.pax.shiro.component.HasAnyPermissions")
public class HasAnyPermissionsComponent extends HasPermissionComponent {
    
    @Override
    public boolean isRendered() {
        if (getSubject() == null) {
            return false;
        }
        for (String permission : StringUtils.split(readName())) {
            if (getSubject().isPermitted(permission)) {
                return true;
            }
        }
        return false;
    }
}
