package DTO.Statistics;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QuarterDataStringId {
    private int quarter;
    private String Id;
    private int soluong;
    public QuarterDataStringId(int quarter, String Id, int soluong) {
        this.quarter = quarter;
        this.Id = Id;
        this.soluong = soluong;
    }
}
