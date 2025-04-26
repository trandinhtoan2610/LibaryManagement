package DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PenaltyDetailsDTO {
    private String penaltyID;
    private Long borrowID;
    private Long bookID;
    private int bookQuantity;
    private String violation;
    private Long punishFee;

    public PenaltyDetailsDTO(String penaltyID, Long borrowID, Long bookID, int bookQuantity, String violation, Long punishFee) {
        this.penaltyID = penaltyID;
        this.borrowID = borrowID;
        this.bookID = bookID;
        this.bookQuantity = bookQuantity;
        this.violation = violation;
        this.punishFee = punishFee;
    }

    public PenaltyDetailsDTO(){}
}
