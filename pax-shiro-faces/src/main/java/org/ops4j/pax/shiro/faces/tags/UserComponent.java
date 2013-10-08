/*
 * $Id: JavaTemplates.xml 53870 2013-02-12 10:32:44Z tlangfeld $
 */
package org.ops4j.pax.shiro.faces.tags;

import javax.faces.component.FacesComponent;

@FacesComponent("org.ops4j.pax.shiro.component.User")
public class UserComponent extends ShiroComponent {
    
    @Override
    public boolean isRendered() {
        return getSubject() != null && getSubject().getPrincipal() != null;
    }
}
