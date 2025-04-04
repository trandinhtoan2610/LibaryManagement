package DTO;

import DTO.Abstract.EntityBase;
import DTO.Enum.SubStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowDetailDTO extends EntityBase<Long> {
    private String borrowSheetId;
    private Long bookId;
    private int quantity;
    private SubStatus status;
    public BorrowDetailDTO(Long id, Long bookId, String borrowSheetId, int quantity, SubStatus status) {
        this.id = id;
        this.bookId = bookId;
        this.borrowSheetId = borrowSheetId;
        this.quantity = quantity;
        this.status = status;
    }
}
