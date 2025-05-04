package DTO.Statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LostBookPreciousData {
    private Long bookID;
    private Long sumQ1;
    private Long sumQ2;
    private Long sumQ3;
    private Long sumQ4;

    public LostBookPreciousData(Long bookID, Long sumQ1, Long sumQ2, Long sumQ3, Long sumQ4) {
        this.bookID = bookID;
        this.sumQ1 = sumQ1;
        this.sumQ2 = sumQ2;
        this.sumQ3 = sumQ3;
        this.sumQ4 = sumQ4;
    }

    public Long sumQuarter(){
        return sumQ1 + sumQ2 + sumQ3 + sumQ4;
    }
}
