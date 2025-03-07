package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;
import DTO.Borrow;
import DTO.Enum.Status;

import java.util.List;

public class BorrowSheetDAL implements IRepositoryBase<Borrow> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<Borrow> borrowRowMapper = this::mapRowToBorrow;

    private Borrow mapRowToBorrow(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new Borrow(
                rs.getLong("id"),
                rs.getLong("employeeId"),
                rs.getLong("customerId"),
                Status.valueOf(rs.getString("status").toString()),
                rs.getDate("duedate"),
                rs.getDate("createdAt"),
                rs.getDate("updatedAt")
        );
    }

    @Override
    public Borrow findById(Long id) {
        String sql = "SELECT * FROM borrow WHERE id = ?";
        return genericDAL.queryForObject(sql, borrowRowMapper, id);
    }

    @Override
    public List<Borrow> findAll() {
        String sql = "SELECT * FROM borrow";
        return genericDAL.queryForList(sql, borrowRowMapper);
    }

    @Override
    public Long create(Borrow borrow) {
        String sql = "INSERT INTO borrow (employeeid, customerid, status, duedate) VALUES (?, ?, ?, ?)";
        return genericDAL.insert(sql,
                borrow.getEmployeeId(),
                borrow.getCustomerId(),
                borrow.getStatus(),
                borrow.getDuedate()
        );
    }

    @Override
    public boolean update(Borrow borrow) {
        String sql = "UPDATE borrow SET employeeId = ?, SET customerId = ? SET status = ?, SET duedate = ? WHERE id = ?";
        return genericDAL.update(sql,
                borrow.getEmployeeId(),
                borrow.getCustomerId(),
                borrow.getStatus(),
                borrow.getDuedate(),
                borrow.getId()
        );
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM borrow WHERE id = ?";
        return genericDAL.delete(sql, id);
    }
}
