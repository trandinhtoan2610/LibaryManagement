package DTO;

import BUS.BookBUS;
import DTO.Abstract.EntityBase;
import DTO.Enum.SubStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BorrowDetailDTO {
    private Long bookId, borrowSheetId;
    private int quantity;
    private SubStatus status;
    private BookViewModel book;
    public BorrowDetailDTO(Long bookId, Long borrowSheetId, int quantity, SubStatus status) {
        this.bookId = bookId;
        this.borrowSheetId = borrowSheetId;
        this.quantity = quantity;
        this.status = status;
    }
    public String getBorrowSheetId(){
        return "HD" + String.format("%04d", this.borrowSheetId);
    }
}
