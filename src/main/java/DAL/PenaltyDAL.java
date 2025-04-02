package DAL;

import DAL.Interface.IRepositoryStringID;
import DAL.Interface.RowMapper;
import DTO.Enum.Pay;
import DTO.PenaltyDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PenaltyDAL implements IRepositoryStringID<PenaltyDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<PenaltyDTO> lawRowMapper = this::lawRowToLawDTO;

    public PenaltyDTO lawRowToLawDTO(ResultSet rs) throws SQLException {
        Pay stringPay = Pay.valueOf(rs.getString("pay").toUpperCase());

        return new PenaltyDTO(
                rs.getString("penaltyId"),
                rs.getString("borrowId"),
                rs.getDate("payDate"),
                rs.getInt("totalAmount"),
                stringPay
        );
    }

    @Override
    public PenaltyDTO findById(String id) {
        String sql = "SELECT * FROM penalty WHERE id = ?";
        return genericDAL.queryForObject(sql, lawRowMapper, id);
    }

    @Override
    public List<PenaltyDTO> findAll() {
        String sql = "SELECT * FROM penalty";
        return genericDAL.queryForList(sql, lawRowMapper);
    }

    @Override
    public Long create(PenaltyDTO penaltyDTO) {
        String formatID = "PEN" + String.format("%04d", penaltyDTO.getPenaltyId());
        String sql = "INSERT INTO penalty (penaltyId, borrowId, payDate, totalAmount, pay)" +
                " VALUES (?, ?, ?, ?, ?)";
        return genericDAL.insert(sql, formatID
                , penaltyDTO.getBorrowId()
                , penaltyDTO.getPayDate()
                , penaltyDTO.getTotalAmount()
                , penaltyDTO.getPay()
        );
    }

    @Override
    public boolean update(PenaltyDTO penaltyDTO) {
        String formatID = "PEN" + String.format("%04d", penaltyDTO.getPenaltyId());
        String sql = "UPDATE penalty SET penaltyId = ?, SET borrowId = ?, SET payDate = ?, SET totalAmount = ?, SET pay = ? WHERE id = ?";
        return genericDAL.update(sql, formatID,
                penaltyDTO.getBorrowId(),
                penaltyDTO.getPayDate(),
                penaltyDTO.getTotalAmount(),
                penaltyDTO.getPay()
        );
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM penalty WHERE id = ?";
        return genericDAL.delete(sql, id);
    }
}
