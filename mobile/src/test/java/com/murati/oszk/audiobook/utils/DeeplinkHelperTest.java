package com.murati.oszk.audiobook.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class DeeplinkHelperTest {

    @Test
    public void testMekURLResolution() throws Exception {
        String mediaID = DeeplinkHelper.getMekIdFromUri("http://mek.oszk.hu/18900/18907/");
        //assertEquals("MEKID", MediaIDHelper.getCategoryValueFromMediaID(mediaID));
        assertNotNull(mediaID);
    }
}
