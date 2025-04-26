package DAL;

import DAL.Interface.IRepositoryDetails;
import DAL.Interface.RowMapper;
import DTO.Enum.PayStatus;
import DTO.PenaltyDTO;

import java.util.List;

public class PenaltyDAL implements IRepositoryDetails<PenaltyDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<PenaltyDTO> penaltyDTORowMapper = rs -> new PenaltyDTO(
                rs.getString("id"),
                rs.getDate("penaltyDate"),
                PayStatus.valueOf(rs.getString("status")),
                rs.getLong("totalamount"),
                rs.getDate("payDate"),
                rs.getObject("employeeID") == null ? null : rs.getLong("employeeID")
            );


    @Override
    public PenaltyDTO findById(String id) {
        String sql = "SELECT * FROM penalty WHERE id = ? ";
        return genericDAL.queryForObject(sql, penaltyDTORowMapper, id);
    }

    @Override
    public List<PenaltyDTO> findAll() {
        String sql = "SELECT * FROM penalty";
        return genericDAL.queryForList(sql,penaltyDTORowMapper);
    }

    @Override
    public Long create(PenaltyDTO p){
        String sql = "INSERT INTO penalty (id, penaltyDate, totalamount, status, payDate, employeeID) " +
                     " VALUES(?, ?, ?, ?, ?, ?)";
        return genericDAL.insert(sql, p.getId(), p.getPenaltyDate(), p.getTotalAmount(), p.getPayStatus().name() ,p.getPayDate(), p.getEmployeeID());
    }

    @Override
    public boolean update(PenaltyDTO penaltyDTO) {
        String sql = "UPDATE penalty SET " +
                    " penaltyDate = ?, totalamount = ?, status = ?, payDate = ?, employeeID = ? WHERE id = ?";
        return genericDAL.update(sql,penaltyDTO.getPenaltyDate(), penaltyDTO.getTotalAmount(), penaltyDTO.getPayStatus().name(),
                                penaltyDTO.getPayDate(), penaltyDTO.getEmployeeID(), penaltyDTO.getId() );
    }

    @Override
    public boolean delete(PenaltyDTO penaltyDTO) {
        String sql = "DELETE FROM penalty WHERE id = ? ";
        return genericDAL.delete(sql, penaltyDTO.getId());
    }

    public Long getCurrentID(){
        String sql = "SELECT MAX(CAST(SUBSTRING(id,3) AS UNSIGNED)) FROM penalty";
        return genericDAL.getMaxID(sql);
    }

}