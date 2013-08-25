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

import java.util.Arrays;

import org.eclipse.jetty.osgi.boot.AbstractWebAppProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class FragmentActivator implements BundleActivator {

    public static final String OSGI_JSF_CONFIGURATION = "org.ops4j.pax.shiro.sample.jetty.osgi.boot.jsf.OsgiJsfConfiguration";

    @Override
    public void start(BundleContext bc) throws Exception {
        String[] defaultConfigurations = AbstractWebAppProvider.getDefaultConfigurations();
        String[] configurations = Arrays.copyOf(defaultConfigurations,
            defaultConfigurations.length + 1);
        configurations[configurations.length - 1] = OSGI_JSF_CONFIGURATION;
        AbstractWebAppProvider.setDefaultConfigurations(configurations);

    }

    @Override
    public void stop(BundleContext arg0) throws Exception {
        String[] defaultConfigurations = AbstractWebAppProvider.getDefaultConfigurations();
        String[] configurations = new String[defaultConfigurations.length - 1];
        int i = 0;
        for (String defaultConfiguration : defaultConfigurations) {
            if (!defaultConfiguration.equals(OSGI_JSF_CONFIGURATION)) {
                configurations[i++] = defaultConfiguration;
            }
        }
        AbstractWebAppProvider.setDefaultConfigurations(configurations);
    }
}
