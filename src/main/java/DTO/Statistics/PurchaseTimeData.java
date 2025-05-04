package DTO.Statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseTimeData {
    private int time;
    private Long purchaseFee;
    private int bookQuantity;
    private int doneSheet;
    private int cancelSheet;

    public PurchaseTimeData(int time, Long purchaseFee, int bookQuantity, int doneSheet, int cancelSheet) {
        this.time = time;
        this.purchaseFee = purchaseFee;
        this.bookQuantity = bookQuantity;
        this.doneSheet = doneSheet;
        this.cancelSheet = cancelSheet;
    }
}
