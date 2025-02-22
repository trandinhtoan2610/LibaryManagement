package DTO;

import DTO.Abstract.EntityAuditNameBase;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class Bookshelf extends EntityAuditNameBase<Long> {
    public Bookshelf(Long id, String name, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
