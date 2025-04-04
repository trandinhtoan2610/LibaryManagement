package DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PenaltyDetailsDTO {
    private String penaltyId, punish;
    private int subAmount;
    public PenaltyDetailsDTO() {
    }
    public PenaltyDetailsDTO(String penaltyId, String punish, int subAmount) {
        this.penaltyId = penaltyId;
        this.punish = punish;
        this.subAmount = subAmount;
    }
}
