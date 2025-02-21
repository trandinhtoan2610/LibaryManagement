package DTO;

import DTO.Abstract.EntityAuditNameBase;

import java.util.Date;

public class Bookshelf extends EntityAuditNameBase<Long> {
    public Bookshelf(Long id, String name, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
