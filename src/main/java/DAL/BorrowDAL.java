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
        Status status = Status.valueOf(rs.getString("status"));
        return new BorrowDTO(
                rs.getLong("id"),
                rs.getLong("employeeId"),
                rs.getString("readerId"),
                rs.getDate("borrowedDate"),
                rs.getDate("duedate"),
                rs.getDate("actualReturnDate"),
                status
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
        String sql = "INSERT INTO borrow_in_sheet (employeeId, readerId ,borrowedDate, duedate, status) VALUES (?, ?, ?, ?, ?)";
        return genericDAL.insert(sql,
                borrowDTO.getEmployeeId(),
                borrowDTO.getReaderId(),
                borrowDTO.getBorrowedDate(),
                borrowDTO.getDuedate(),
                borrowDTO.getStatus().name()
        );
    }

    @Override
    public boolean update(BorrowDTO borrowDTO) {
        String sql = "UPDATE borrow_in_sheet SET employeeId = ?, readerId = ?, borrowedDate = ?, duedate = ?, actualReturnDate = ?, status = ? WHERE id = ?";
        Long id = Long.parseLong(borrowDTO.getId().substring(2));
        return genericDAL.update(sql,
                borrowDTO.getEmployeeId(),
                borrowDTO.getReaderId(),
                borrowDTO.getBorrowedDate(),
                borrowDTO.getDuedate(),
                borrowDTO.getActualReturnDate(),
                borrowDTO.getStatus().name(),
                id
        );
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM borrow_in_sheet WHERE id = ?";
        return genericDAL.delete(sql, id);
    }

    public long getCurrentID() {
        String sql = "SELECT MAX(id) FROM borrow_in_sheet";
        return genericDAL.getMaxID(sql);
    }
}
