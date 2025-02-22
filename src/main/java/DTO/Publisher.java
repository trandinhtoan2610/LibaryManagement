package DTO;

import DTO.Abstract.EntityAuditNameBase;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class Publisher extends EntityAuditNameBase<Long> {
    private String address;
    public String getAddress() {
        return address;
    }
    public Publisher(Long id, String name, String address, Date createdAt, Date UpdateAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = UpdateAt;
    }
}
