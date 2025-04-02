package DTO;

import DTO.Enum.Pay;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PenaltyDTO {
    private String penaltyId, borrowId;
    private Pay pay;
    private Date payDate;
    private int totalAmount;
    public PenaltyDTO() {};
    public PenaltyDTO(String penaltyId, String borrowId, Date payDate, int totalAmount, Pay pay) {
        this.penaltyId = penaltyId;
        this.borrowId = borrowId;
        this.pay = pay;
        this.payDate = payDate;
        this.totalAmount = totalAmount;
    }
}
