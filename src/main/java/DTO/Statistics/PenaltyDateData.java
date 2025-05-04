package DTO.Statistics;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PenaltyDateData {
    private String penaltyID;
    private Date penaltyDate;
    private String readerID;
    private int lostBooks;
    private Long totalAmount;

    public PenaltyDateData(String penaltyID, Date penaltyDate, String readerID, int lostBooks, Long totalAmount) {
        this.penaltyID = penaltyID;
        this.penaltyDate = penaltyDate;
        this.readerID = readerID;
        this.lostBooks = lostBooks;
        this.totalAmount = totalAmount;
    }
}
