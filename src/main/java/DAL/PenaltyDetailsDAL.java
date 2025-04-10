package DAL;

import DAL.Interface.IRepositoryDetails;
import DAL.Interface.RowMapper;
import DTO.Enum.Pay;
import DTO.PenaltyDTO;
import DTO.PenaltyDetailsDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PenaltyDetailsDAL implements IRepositoryDetails<PenaltyDetailsDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<PenaltyDetailsDTO> penaltyDetailsRowMapper = this::penaltyDetailsRowToPenaltyDetailsDTO;

    public PenaltyDetailsDTO penaltyDetailsRowToPenaltyDetailsDTO(ResultSet rs) throws SQLException {
        return new PenaltyDetailsDTO(
                rs.getLong("penaltyId"),
                rs.getString("punish"),
                rs.getInt("subAmount")
        );
    }

    @Override
    public PenaltyDetailsDTO findById(String id) {
        String sql = "SELECT * FROM penaltyDetails WHERE id = ?";
        return genericDAL.queryForObject(sql, penaltyDetailsRowMapper, id);
    }

    @Override
    public List<PenaltyDetailsDTO> findAll() {
        String sql = "SELECT * FROM penaltyDetails";
        return genericDAL.queryForList(sql, penaltyDetailsRowMapper);
    }

    @Override
    public Long create(PenaltyDetailsDTO penaltyDetailsDTO) {
        String sql = "INSERT INTO penaltyDetails (penaltyId, punish, subAmount)" +
                " VALUES (?, ?, ?)";
        return genericDAL.insert(sql, penaltyDetailsDTO.getPenaltyId()
                , penaltyDetailsDTO.getPunish()
                , penaltyDetailsDTO.getSubAmount()
        );
    }

    @Override
    public boolean update(PenaltyDetailsDTO penaltyDetailsDTO) {
        String sql = "UPDATE penaltyDetails SET penaltyId = ?, punish = ?, subAmount = ? WHERE id = ?";
        return genericDAL.update(sql, penaltyDetailsDTO.getPenaltyId(),
                penaltyDetailsDTO.getPunish(),
                penaltyDetailsDTO.getSubAmount(),
                penaltyDetailsDTO.getPenaltyId()
        );
    }
    @Override
    public boolean delete(PenaltyDetailsDTO penaltyDetailsDTO) {
        String sql = "DELETE FROM penaltyDetails WHERE i";
        return genericDAL.delete(sql, penaltyDetailsDTO);
    }
}
