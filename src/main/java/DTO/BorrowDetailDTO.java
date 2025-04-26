package DTO;

import DTO.Enum.SubStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
public class BorrowDetailDTO {
    private Long bookId, borrowSheetId;
    private int quantity;
    private SubStatus status;
    private Date actualReturnDate;
    public BorrowDetailDTO(Long bookId, Long borrowSheetId, int quantity, SubStatus status, Date actualReturnDate) {
        this.bookId = bookId;
        this.borrowSheetId = borrowSheetId;
        this.quantity = quantity;
        this.status = status;
        this.actualReturnDate = actualReturnDate;
    }
    public String getBorrowSheetId(){
        return "HD" + String.format("%04d", this.borrowSheetId);
    }
    public Long getLongBorrowSheetID(){
        return this.borrowSheetId;
    }

    @Override
    public String toString() {
        return "BorrowDetailDTO{" +
                "bookId=" + bookId +
                ", borrowSheetId=" + borrowSheetId +
                ", quantity=" + quantity +
                ", status=" + status +
                ", actualReturnDate=" + actualReturnDate +
                '}';
    }
}
