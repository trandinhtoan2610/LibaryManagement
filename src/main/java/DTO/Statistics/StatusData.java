package DTO.Statistics;

import DTO.Enum.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StatusData {
    private Long id;
    private Status status;

    public StatusData(Long id, Status status) {
        this.id = id;
        this.status = status;
    }
}
