package DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PenaltyDetailsDTO {
    private Long penaltyId;
    private String punish;
    private int subAmount;
    public PenaltyDetailsDTO() {
    }
    public PenaltyDetailsDTO(Long penaltyId, String punish, int subAmount) {
        this.penaltyId = penaltyId;
        this.punish = punish;
        this.subAmount = subAmount;
    }
}
