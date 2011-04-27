/*
 * Phylontal - a phylogenetically aware ontology alignment tool
 * Copyright 2009-2011 Peter E. Midford
 * 
 * Created on Oct 5, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.fe;

import static org.junit.Assert.*;

import java.awt.EventQueue;

import org.ethontos.phylontal.fe.MainFrame;
import org.ethontos.phylontal.fe.UserFrontEnd;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestUserFrontEnd {

    private UserFrontEnd fe;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        fe = new UserFrontEnd();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMain() {
       assertNotNull("A frontend was created",fe);
       EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainFrame mf = new MainFrame("Test Frontend",fe);
                assertEquals("Should be equal",fe,mf.getApp());
                mf.getApp().runUI(mf);
            }
        });        
    }

}
