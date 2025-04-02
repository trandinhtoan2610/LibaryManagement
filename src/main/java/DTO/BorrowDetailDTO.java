package DTO;

import DTO.Abstract.EntityBase;
import DTO.Enum.SubStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowDetailDTO extends EntityBase<Long> {
    private Long bookId, borrowSheetId;
    private int quantity;
    private SubStatus status;
}
