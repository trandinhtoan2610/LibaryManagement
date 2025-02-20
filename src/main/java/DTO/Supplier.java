package DTO;
import DTO.Abstract.EntityAuditNameBase;

import java.util.Date;

public class Supplier extends EntityAuditNameBase<Long> {
    public Supplier(Long id, String name, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
