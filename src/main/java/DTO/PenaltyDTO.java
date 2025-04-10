package DTO;

import DTO.Enum.Pay;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PenaltyDTO {
    private Long id, penaltyId, borrowId;
    private Pay pay;
    private Date payDate;
    private int totalAmount;
    public PenaltyDTO() {};
    public PenaltyDTO(Long id, Long penaltyId, Long borrowId, Date payDate, int totalAmount, Pay pay) {
        this.id = id;
        this.penaltyId = penaltyId;
        this.borrowId = borrowId;
        this.pay = pay;
        this.payDate = payDate;
        this.totalAmount = totalAmount;
    }
}
