package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;
import DTO.BorrowDTO;
import DTO.Enum.Status;

import java.util.List;

public class BorrowSheetDAL implements IRepositoryBase<BorrowDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<BorrowDTO> borrowRowMapper = this::mapRowToBorrow;

    private BorrowDTO mapRowToBorrow(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new BorrowDTO(
                rs.getString("id"),
                rs.getLong("employeeId"),
                rs.getLong("customerId"),
                Status.valueOf(rs.getString("status").toString()),
                rs.getDate("duedate"),
                rs.getDate("createdAt"),
                rs.getDate("updatedAt")
        );
    }

    @Override
    public BorrowDTO findById(Long id) {
        String sql = "SELECT * FROM borrow WHERE id = ?";
        return genericDAL.queryForObject(sql, borrowRowMapper, id);
    }

    @Override
    public List<BorrowDTO> findAll() {
        String sql = "SELECT * FROM borrow";
        return genericDAL.queryForList(sql, borrowRowMapper);
    }

    @Override
    public Long create(BorrowDTO borrowDTO) {
        String sql = "INSERT INTO borrow (employeeId, readerId, status, borrowedDate, duedate, actualReturnDate) VALUES (?, ?, ?, ?, ?, ?)";
        return genericDAL.insert(sql,
                borrowDTO.getEmployeeId(),
                borrowDTO.getReaderId(),
                borrowDTO.getStatus(),
                borrowDTO.getBorrowedDate(),
                borrowDTO.getDuedate(),
                borrowDTO.getActualReturnDate()
        );
    }

    @Override
    public boolean update(BorrowDTO borrowDTO) {
        String sql = "UPDATE borrow SET employeeId = ?, SET readerId = ?, SET status = ?,SET borrowedDate = ?, SET duedate = ?, SET actualReturnDate = ? WHERE id = ?";
        return genericDAL.update(sql,
                borrowDTO.getEmployeeId(),
                borrowDTO.getReaderId(),
                borrowDTO.getStatus(),
                borrowDTO.getBorrowedDate(),
                borrowDTO.getDuedate(),
                borrowDTO.getActualReturnDate(),
                borrowDTO.getId()
        );
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM borrow WHERE id = ?";
        return genericDAL.delete(sql, id);
    }
}
