/*
 * $Id: JavaTemplates.xml 53870 2013-02-12 10:32:44Z tlangfeld $
 */
package org.ops4j.pax.shiro.faces.tags;

import javax.faces.component.FacesComponent;

@FacesComponent("org.ops4j.pax.shiro.component.HasRole")
public class HasRoleComponent extends ShiroComponent {
    
    /**
     * @return Returns the {@link #role}.
     */
    protected String readName() {
        return (String) getAttributes().get("name");
    }

    
    @Override
    public boolean isRendered() {
        return getSubject() != null && getSubject().hasRole(readName());
    }
}
