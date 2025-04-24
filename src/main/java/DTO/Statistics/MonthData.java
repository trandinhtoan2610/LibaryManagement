package DTO.Statistics;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MonthData {
    private int thang;
    private int soluong;

    public MonthData(int thang, int soluong) {
        this.thang = thang;
        this.soluong = soluong;
    }
}
