package DTO;
import DTO.Abstract.EntityAuditNameBase;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;
@Getter
@Setter
public class Book extends EntityAuditNameBase<Long> {
    private Long categoryId, authorId, bookshelfId, publisherId;
    private int quantity;
    private Year yearOfPublication;
}
