package DTO;

import DTO.Abstract.EntityAuditBase;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Borrow extends EntityAuditBase<Long> {
    private Long employeeId,customerId;
    private Status status;
    private Date duedate;
    public enum Status{
        BORROW,
        RETURNED,
        OVERDUE
    }
}


