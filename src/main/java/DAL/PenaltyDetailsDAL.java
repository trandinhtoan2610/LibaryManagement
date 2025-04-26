package DAL;

import DAL.Interface.IDetailsBase;
import DAL.Interface.RowMapper;
import DTO.PenaltyDetailsDTO;

import java.util.List;

public class PenaltyDetailsDAL implements IDetailsBase<PenaltyDetailsDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<PenaltyDetailsDTO> penaltyDetailsDTORowMapper = rs -> new PenaltyDetailsDTO(
            rs.getString("penaltyId"),
            rs.getLong("borrowId"),
            rs.getLong("bookId"),
            rs.getInt("bookQuantity"),
            rs.getString("name"),
            rs.getLong("subamount")
    );

    @Override
    public List<PenaltyDetailsDTO> findAll() {
        String sql = "SELECT * FROM penaltydetails";
        return genericDAL.queryForList(sql, penaltyDetailsDTORowMapper);
    }

    @Override
    public Long create(PenaltyDetailsDTO p) {
        String sql = "INSERT INTO penaltydetails (penaltyId, bookId, borrowId, bookQuantity, name, subamount) " +
                      " VALUES(?, ?, ?, ? ,?, ?)";
        return genericDAL.insert(sql, p.getPenaltyID(), p.getBookID(), p.getBorrowID(),
                p.getBookQuantity() ,p.getViolation(), p.getPunishFee() );
    }

    @Override
    public boolean update(PenaltyDetailsDTO p) {
        String sql = "UPDATE penaltydetails SET bookId = ?, name = ?, subamount = ?,  bookQuantity = ? WHERE penaltyID = ? AND borrowId = ?";
        return genericDAL.update(sql, p.getBookID(), p.getViolation(), p.getPunishFee(), p.getBookQuantity(), p.getPenaltyID(), p.getBorrowID());
    }

    @Override
    public boolean delete(PenaltyDetailsDTO p) {
        String sql = "DELETE FROM penaltydetails WHERE penaltyID = ? AND borrowID = ?";
        return genericDAL.delete(sql, p.getPenaltyID(), p.getBorrowID() );
    }

    public List<PenaltyDetailsDTO> findByPenaltyID(String penaltyID){
        String sql = "SELECT * FROM penaltydetails WHERE penaltyId = ?";
        return genericDAL.queryForList(sql, penaltyDetailsDTORowMapper ,penaltyID);
    }
}