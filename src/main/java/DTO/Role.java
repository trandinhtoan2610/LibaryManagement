package DTO;

import DTO.Abstract.EntityAuditNameBase;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Role extends EntityAuditNameBase<Long> {
    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
