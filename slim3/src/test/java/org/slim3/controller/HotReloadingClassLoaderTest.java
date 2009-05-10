/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.slim3.controller;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class HotReloadingClassLoaderTest extends TestCase {

    private static final String CONTROLLER_PACKAGE =
        HotReloadingClassLoaderTest.class.getPackage().getName()
            + ".controller";
    /**
     * 
     */
    protected static final String CONTROLLER_CLASS_NAME =
        CONTROLLER_PACKAGE + ".HogeController";

    /**
     * 
     */
    protected ClassLoader originalClassLoader;

    /**
     * 
     */
    protected HotReloadingClassLoader hotClassLoader;

    @Override
    protected void setUp() throws Exception {
        System.setProperty(
            ControllerConstants.CONTROLLER_PACKAGE_KEY,
            CONTROLLER_PACKAGE);
        super.setUp();
        originalClassLoader = Thread.currentThread().getContextClassLoader();
        hotClassLoader = new HotReloadingClassLoader(originalClassLoader);
    }

    @Override
    protected void tearDown() throws Exception {
        originalClassLoader = null;
        hotClassLoader = null;
        System.clearProperty(ControllerConstants.CONTROLLER_PACKAGE_KEY);
        super.tearDown();
    }

    /**
     * @throws Exception
     * 
     */
    public void testLoadClass() throws Exception {
        Class<?> clazz = hotClassLoader.loadClass(CONTROLLER_CLASS_NAME);
        assertNotNull(clazz);
        assertSame(hotClassLoader, clazz.getClassLoader());
        assertSame(clazz, hotClassLoader.loadClass(CONTROLLER_CLASS_NAME));
        assertNotNull(clazz.getPackage());
    }

    /**
     * @throws Exception
     */
    public void testIsTarget() throws Exception {
        assertTrue(hotClassLoader.isTarget(CONTROLLER_CLASS_NAME));
        assertFalse(hotClassLoader.isTarget(String.class.getName()));
    }
}