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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.URLResource;


public class BundleResource extends URLResource {

    protected BundleResource() throws MalformedURLException {
        super(new URL("file:/dummy"), null);
    }
    
    @Override
    public Resource addPath(String path) throws IOException {
        String bundleUrl = path.substring(1);
        if (!path.startsWith("/bundle:") && !path.startsWith("/bundleentry:")) {
            throw new MalformedURLException("not a bundle URL: " + path);
        }
        URL url = new URL(bundleUrl);
        return Resource.newResource(url);
    }
    
    @Override
    public boolean exists() {
        return true;
    }
    
    @Override
    public boolean isDirectory() {
        return true;
    }
}
