package DTO.Statistics;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QuarterData {
    private int quarter;
    private int Id;
    private int soluong;
    public QuarterData(int quarter, int Id, int soluong) {
        this.quarter = quarter;
        this.Id = Id;
        this.soluong = soluong;
    }
}
