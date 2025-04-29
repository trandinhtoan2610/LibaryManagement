package DTO;

import DTO.Abstract.EntityAuditNameBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category extends EntityAuditNameBase<Long> {
    private boolean isActive = true;
    public Category() {}
    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
