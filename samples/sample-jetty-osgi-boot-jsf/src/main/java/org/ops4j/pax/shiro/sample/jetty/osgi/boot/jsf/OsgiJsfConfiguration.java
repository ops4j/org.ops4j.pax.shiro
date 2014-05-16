/*
 * Copyright 2013 Harald Wellmann
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.ops4j.pax.shiro.sample.jetty.osgi.boot.jsf;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.jetty.osgi.boot.OSGiWebappConstants;
import org.eclipse.jetty.osgi.boot.utils.internal.PackageAdminServiceTracker;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.AbstractConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.osgi.framework.Bundle;

public class OsgiJsfConfiguration extends AbstractConfiguration {
    
    private static final String CONFIG_FILES_KEY = "javax.faces.CONFIG_FILES";
    
    private static final String FACELETS_LIBS_KEY = "javax.faces.FACELETS_LIBRARIES";

    @Override
    public void configure(WebAppContext context) throws Exception {
        // comma-separated config files
        String configFilesParam = context.getInitParameter(CONFIG_FILES_KEY);

        // semicolon-separated facelet libs
        String faceletLibsParam = context.getInitParameter(FACELETS_LIBS_KEY);

        List<URL> configFiles = new ArrayList<URL>();
        List<URL> faceletLibs = new ArrayList<URL>();
        Bundle webBundle = (Bundle) context.getAttribute(OSGiWebappConstants.JETTY_OSGI_BUNDLE);
        Bundle[] bundles = PackageAdminServiceTracker.INSTANCE
            .getFragmentsAndRequiredBundles(webBundle);
        for (Bundle bundle : bundles) {
            Enumeration<URL> e = bundle.findEntries("/META-INF", "*taglib.xml", false);
            if (e != null) {
                while (e.hasMoreElements()) {
                    URL u = e.nextElement();
                    faceletLibs.add(u);
                }
            }
            e = bundle.findEntries("/META-INF", "*faces-config.xml", false);
            if (e != null) {
                while (e.hasMoreElements()) {
                    URL u = e.nextElement();
                    configFiles.add(u);
                }
            }
        }

        if (!configFiles.isEmpty()) {
            StringBuilder builder;
            if (configFilesParam == null) {
                builder = new StringBuilder();
            }
            else {
                builder = new StringBuilder(configFilesParam);
                builder.append(",");
            }
            for (URL url : configFiles) {
                builder.append("/");
                builder.append(url);
                builder.append(",");
            }
            String param = builder.toString();
            param = param.substring(0, param.length() - 1);
            context.setInitParameter(CONFIG_FILES_KEY, param);
        }

        if (!faceletLibs.isEmpty()) {
            StringBuilder builder;
            if (faceletLibsParam == null) {
                builder = new StringBuilder();
            }
            else {
                builder = new StringBuilder(faceletLibsParam);
                builder.append(";");
            }
            for (URL url : faceletLibs) {
                builder.append("/");
                builder.append(url);
                builder.append(";");
            }
            String param = builder.toString();
            param = param.substring(0, param.length() - 1);
            context.setInitParameter(FACELETS_LIBS_KEY, param);
        }
        Resource baseResource = context.getBaseResource();
        context.setBaseResource(new ResourceCollection(baseResource, new BundleResource()));
    }
}
