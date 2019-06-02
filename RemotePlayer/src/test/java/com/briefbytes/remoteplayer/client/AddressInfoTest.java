package com.briefbytes.remoteplayer.client;

import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.*;

public class AddressInfoTest {

    private AddressInfo addressInfo;

    @Before
    public void setUp() throws Exception {
        addressInfo = new AddressInfo(InetAddress.getByName("127.0.0.1"), "info");
    }

    @Test
    public void testAddress() throws Exception {
        assertEquals("127.0.0.1", addressInfo.address());
    }

    @Test
    public void testEquals() throws Exception {
        assertEquals(addressInfo, new AddressInfo(InetAddress.getByName("127.0.0.1"), "other info"));
        assertNotEquals(addressInfo, new AddressInfo(InetAddress.getByName("127.0.0.2"), "info"));
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(addressInfo.hashCode(), new AddressInfo(InetAddress.getByName("127.0.0.1"), "other info").hashCode());
        assertNotEquals(addressInfo.hashCode(), new AddressInfo(InetAddress.getByName("127.0.0.2"), "info").hashCode());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("(127.0.0.1) info", addressInfo.toString());
    }

    @Test
    public void testCompareTo() throws Exception {
        // same values
        assertTrue(0 == addressInfo.compareTo(new AddressInfo(InetAddress.getByName("127.0.0.1"), "info")));
        // same address, different info
        assertTrue(0 < addressInfo.compareTo(new AddressInfo(InetAddress.getByName("127.0.0.1"), "a")));
        // different address, same info
        assertTrue(0 > addressInfo.compareTo(new AddressInfo(InetAddress.getByName("127.0.0.2"), "info")));
    }
}