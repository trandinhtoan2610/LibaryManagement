package DTO;

import DTO.Enum.PurchaseStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor // Lombok sẽ tạo constructor đầy đủ cho tất cả các thuộc tính
@NoArgsConstructor  // Lombok sẽ tạo constructor mặc định (không tham số)
public class PurchaseOrderDTO {
    private long id;
    private long employeeId;
    private String supplierId;
    private PurchaseStatus status;
    private BigDecimal totalAmount;
    private Date buyDate;
}
