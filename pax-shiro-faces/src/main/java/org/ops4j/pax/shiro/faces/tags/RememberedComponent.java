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

import javax.faces.component.FacesComponent;

/**
 * Tag that renders the tag body only if the current user's identity (aka principals) is remembered from a
 * successful authentication during a previous session and the user has <b>not</b> executed a successful authentication
 * attempt during their current session.
 * <p/>
 * <b>Note:</b> This is <em>less</em> restrictive than the <code>AuthenticatedComponent</code> since it only assumes
 * the user is who they say they are <em>via Remember Me services</em>, which
 * makes no guarantee the user is who they say they are.  The <code>AuthenticatedComponent</code> however
 * guarantees that the current user has logged in <em>during their current session</em>, proving they really are
 * who they say they are.
 */
@FacesComponent("org.ops4j.pax.shiro.component.Remembered")
public class RememberedComponent extends ShiroComponent {

    @Override
    public boolean isRendered() {
        return getSubject() != null && getSubject().isRemembered();
    }
}
