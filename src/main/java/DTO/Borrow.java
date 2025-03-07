package DTO;

import DTO.Abstract.EntityAuditBase;
import DTO.Enum.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Borrow extends EntityAuditBase<Long> {
    private Long employeeId, customerId;
    private Status status;
    private Date duedate;

    public Borrow(Long id, Long employeeId, Long customerId, Status status, Date createAt, Date updateAt, Date duedate) {
        this.id = id;
        this.employeeId = employeeId;
        this.customerId = customerId;
        this.status = status;
        this.duedate = duedate;
        this.createdAt = createAt;
        this.updatedAt = updateAt;
    }
}


