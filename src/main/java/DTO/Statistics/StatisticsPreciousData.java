package DTO.Statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticsPreciousData<T> {
    private T id;
    private Long totalQ1;
    private Long totalQ2;
    private Long totalQ3;
    private Long totalQ4;
    private Long countQ1;
    private Long countQ2;
    private Long countQ3;
    private Long countQ4;

    public StatisticsPreciousData(T id, Long totalQ1, Long totalQ2, Long totalQ3, Long totalQ4, Long countQ1, Long countQ2, Long countQ3, Long countQ4) {
        this.id = id;
        this.totalQ1 = totalQ1;
        this.totalQ2 = totalQ2;
        this.totalQ3 = totalQ3;
        this.totalQ4 = totalQ4;
        this.countQ1 = countQ1;
        this.countQ2 = countQ2;
        this.countQ3 = countQ3;
        this.countQ4 = countQ4;
    }

    public Long sumTotal(){
        return totalQ1 + totalQ2 + totalQ3 + totalQ4;
    }

    public Long sumCount(){
        return countQ1 + countQ2 + countQ3 + countQ4;
    }
}
