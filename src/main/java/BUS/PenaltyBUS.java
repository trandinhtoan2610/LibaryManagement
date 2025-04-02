package BUS;

import DAL.PenaltyDAL;
import DTO.PenaltyDTO;

import java.util.List;

public class PenaltyBUS {
    private final PenaltyDAL penaltyDAL = new PenaltyDAL();

    public List<PenaltyDTO> getAllPenalties() {
        return penaltyDAL.findAll();
    }

    public long addPenalty(PenaltyDTO penalty) {
        return penaltyDAL.create(penalty);
    }

    public boolean updatePenalty(PenaltyDTO penalty) {
        try {
            return penaltyDAL.update(penalty);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePenalty(String id) {
        try {
            return penaltyDAL.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
