package DTO;
import DTO.Abstract.EntityAuditNameBase;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class Supplier extends EntityAuditNameBase<Long> {
    private String phone, address;
    public Supplier(Long id, String name,String phone, String address, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
