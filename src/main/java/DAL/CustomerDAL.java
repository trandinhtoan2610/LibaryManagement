package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;
import DTO.Customer;
import DTO.Enum.Gender;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CustomerDAL implements IRepositoryBase<Customer> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<Customer> CustomerRowMapper = this::mapRowToCustomer ;
    private Customer mapRowToCustomer(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getLong("id"),
                rs.getString("name"),
                Gender.valueOf(rs.getString("gender").toString()),
                rs.getString("phone"),
                rs.getDate("createdAt"),
                rs.getDate("updatedAt")
        );
    }

    @Override
    public Customer findById(Long id) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        return genericDAL.queryForObject(sql, CustomerRowMapper, id);
    }

    @Override
    public List<Customer> findAll() {
        String sql = "SELECT * FROM customer";
        return genericDAL.queryForList(sql, CustomerRowMapper);
    }

    @Override
    public Long create(Customer customer) {
        String sql = "INSERT INTO customer (name, gender, phone) VALUES (?, ?, ?)";
        return genericDAL.insert(sql, customer.getName(), customer.getGender(), customer.getPhone());
    }

    @Override
    public boolean update(Customer customer) {
        String sql = "UPDATE customer SET name = ? SET gender = ? SET phone = ? WHERE id = ?";
        return genericDAL.update(sql, customer.getName(),customer.getGender(),customer.getPhone(), customer.getId());
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM customer WHERE id = ?";
        return genericDAL.delete(sql, id);
    }
}
