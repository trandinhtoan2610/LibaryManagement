package DTO;

import DTO.Abstract.EntityAuditNameBase;

public class Publisher extends EntityAuditNameBase<Long> {
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;
}
