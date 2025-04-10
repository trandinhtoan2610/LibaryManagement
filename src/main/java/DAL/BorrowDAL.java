package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;
import DTO.BorrowDTO;
import DTO.Enum.Status;

import java.util.List;

public class BorrowDAL implements IRepositoryBase<BorrowDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<BorrowDTO> borrowRowMapper = this::mapRowToBorrow;

    private BorrowDTO mapRowToBorrow(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new BorrowDTO(
                rs.getLong("id"),
                rs.getLong("employeeId"),
                rs.getLong("readerId"),
                rs.getDate("borrowedDate"),
                rs.getDate("duedate"),
                rs.getDate("actualReturnDate"),
                Status.valueOf(rs.getString("status").toString())
        );
    }

    @Override
    public BorrowDTO findById(Long id) {
        String sql = "SELECT * FROM borrow_in_sheet WHERE id = ?";
        return genericDAL.queryForObject(sql, borrowRowMapper, id);
    }

    @Override
    public List<BorrowDTO> findAll() {
        String sql = "SELECT * FROM borrow_in_sheet";
        return genericDAL.queryForList(sql, borrowRowMapper);
    }

    @Override
    public Long create(BorrowDTO borrowDTO) {
        String sql = "INSERT INTO borrow_in_sheet (employeeId, readerId, status, borrowedDate, duedate, actualReturnDate) VALUES (?, ?, ?, ?, ?, ?)";
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
        String sql = "UPDATE borrow_in_sheet SET employeeId = ?, SET readerId = ?, SET status = ?,SET borrowedDate = ?, SET duedate = ?, SET actualReturnDate = ? WHERE id = ?";
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
        String sql = "DELETE FROM borrow_in_sheet WHERE id = ?";
        return genericDAL.delete(sql, id);
    }
}
