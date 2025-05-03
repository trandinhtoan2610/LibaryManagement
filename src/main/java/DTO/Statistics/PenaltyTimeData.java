package DTO.Statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PenaltyTimeData {
    private int time;
    private int penaltySheetQuantity;
    private int penaltyReaderQuantity;
    private int lostBookQuantity;
    private Long penaltyFee;

    public PenaltyTimeData(int time, int penaltySheetQuantity, int penaltyReaderQuantity, int lostBookQuantity, Long penaltyFee) {
        this.time = time;
        this.penaltySheetQuantity = penaltySheetQuantity;
        this.penaltyReaderQuantity = penaltyReaderQuantity;
        this.lostBookQuantity = lostBookQuantity;
        this.penaltyFee = penaltyFee;
    }
}
