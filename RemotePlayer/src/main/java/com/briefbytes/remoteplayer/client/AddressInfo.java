package com.briefbytes.remoteplayer.client;

import java.net.InetAddress;

public class AddressInfo implements Comparable<AddressInfo> {

    private InetAddress address;
    private String info;

    public AddressInfo(InetAddress address, String info) {
        if (address == null || info == null) throw new IllegalArgumentException("Address cannot be null");
        this.address = address;
        this.info = info;
    }

    public InetAddress getAddress() {
        return address;
    }

    public String getInfo() {
        return info;
    }

    public String address() {
        return address.getHostAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressInfo)) return false;

        AddressInfo that = (AddressInfo) o;

        return address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }

    @Override
    public String toString() {
        return "(" + address.getHostAddress() + ") " + info;
    }

    @Override
    public int compareTo(AddressInfo o) {
        int result = info.compareTo(o.info);
        if (result == 0) {
            return address().compareTo(o.address());
        }
        return result;
    }

}
