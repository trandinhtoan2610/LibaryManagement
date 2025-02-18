package DTO;
import DTO.Abstract.EntityAuditBase;
import java.util.UUID;

public class Book extends EntityAuditBase<Long> {
    private String name;
}
