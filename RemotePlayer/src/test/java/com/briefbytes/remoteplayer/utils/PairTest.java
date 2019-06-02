package com.briefbytes.remoteplayer.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PairTest {

    private Pair<Integer, String> pair;

    @Before
    public void setUp() throws Exception {
        pair = new Pair<Integer, String>(1, "test");
    }

    @Test
    public void testEquals() throws Exception {
        assertEquals(pair, new Pair<Integer, String>(1, "test"));
        assertNotEquals(pair, new Pair<Integer, String>(2, "test2"));
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(pair.hashCode(), new Pair<Integer, String>(1, "test").hashCode());
        assertNotEquals(pair.hashCode(), new Pair<Integer, String>(2, "test2").hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("1 - test", pair.toString());
    }

}