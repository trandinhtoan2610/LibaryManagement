package DTO;

import DTO.Abstract.EntityBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowDetail extends EntityBase<Long> {
    private Long bookId, brorrowId;
    private int quantity;
}
