package BUS;

import DAL.PenaltyDetailsDAL;
import DTO.PenaltyDTO;
import DTO.PenaltyDetailsDTO;

import java.util.List;

public class PenaltyDetailsBUS {
    private final PenaltyDetailsDAL penaltyDetailsDAL = new PenaltyDetailsDAL();
    public List<PenaltyDetailsDTO> getAllPenaltyDetails() {
        return penaltyDetailsDAL.findAll();
    }
    public long addPenaltyDetails(PenaltyDetailsDTO penaltyDetails) {
        return penaltyDetailsDAL.create(penaltyDetails);
    }
    public boolean updatePenalty(PenaltyDetailsDTO penaltyDetails) {
        try {
            return penaltyDetailsDAL.update(penaltyDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
