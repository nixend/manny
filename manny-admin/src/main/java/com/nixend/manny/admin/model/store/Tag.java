package com.nixend.manny.admin.model.store;

/**
 * @author panyox
 */
public class Tag {
    String name;
    String[] addresses;

    public Tag(String name, String[] addresses) {
        this.name = name;
        this.addresses = addresses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getAddresses() {
        return addresses;
    }

    public void setAddresses(String[] addresses) {
        this.addresses = addresses;
    }
}
