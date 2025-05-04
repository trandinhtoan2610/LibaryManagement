package DTO.Statistics;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PurchaseDateData {
    private Long purchaseID;
    private Date purchaseDate;
    private String supplierID;
    private Long purchaseFee;
    private Long bookQuantity;

    public PurchaseDateData(Long purchaseID, Date purchaseDate, String supplierID, Long purchaseFee, Long bookQuantity) {
        this.purchaseID = purchaseID;
        this.purchaseDate = purchaseDate;
        this.supplierID = supplierID;
        this.purchaseFee = purchaseFee;
        this.bookQuantity = bookQuantity;
    }
}
