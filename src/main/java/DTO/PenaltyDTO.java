package DTO;


import DTO.Enum.PayStatus;
import DTO.Enum.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PenaltyDTO {
   private String id;
   private Date penaltyDate;
   private PayStatus payStatus;
   private Long totalAmount;
   private Date payDate;
   private Long employeeID;

    public  PenaltyDTO(){}

    public PenaltyDTO(String id, Date penaltyDate, PayStatus payStatus, Long totalAmount, Date payDate, Long employeeID) {
        this.id = id;
        this.penaltyDate = penaltyDate;
        this.payStatus = payStatus;
        this.payDate = payDate;
        this.totalAmount = totalAmount;
        this.employeeID = employeeID;
    }

}
