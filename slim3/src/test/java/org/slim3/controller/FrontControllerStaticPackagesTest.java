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

import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class FrontControllerStaticPackagesTest extends TestCase {

    /**
     * @throws Exception
     * 
     */
    public void testNormal() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        FrontController frontController = new FrontController();
        frontController.servletContext = servletContext;
        servletContext.setInitParameter(
            ControllerConstants.STATIC_PACKAGES_KEY,
            "model, filter");
        frontController.initStaticPackageNames();
        String[] names = frontController.staticPackageNames;
        assertEquals(2, names.length);
        assertEquals("model", names[0]);
        assertEquals("filter", names[1]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testNotFound() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        FrontController frontController = new FrontController();
        frontController.servletContext = servletContext;
        try {
            frontController.initStaticPackageNames();
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testNotFoundForModel() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        FrontController frontController = new FrontController();
        frontController.servletContext = servletContext;
        servletContext.setInitParameter(
            ControllerConstants.STATIC_PACKAGES_KEY,
            "filter");
        try {
            frontController.initStaticPackageNames();
            fail();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}