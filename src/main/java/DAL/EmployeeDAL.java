package DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import DAL.Interface.IEmployeeDAL;
import DAL.Interface.RowMapper;
import DTO.Employee;
import DTO.Enum.Gender;

public class EmployeeDAL implements IEmployeeDAL {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<Employee> employeeRowMapper = this::mapRowToEmployee;

    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getLong("id"),
                rs.getLong("roleId"),
                rs.getString("name"),
                Gender.valueOf(rs.getString("gender").toString()),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getDate("createdAt"),
                rs.getDate("updatedAt")
        );
    }


    @Override
    public Employee login(String username, String password) {
        String sql = "SELECT * FROM employee WHERE username = ? AND password = ?";
        return genericDAL.queryForObject(sql, employeeRowMapper, username, password);
    }


    @Override
    public Employee findById(Long id) {
        String sql = "SELECT * FROM employee WHERE id = ?";
        return genericDAL.queryForObject(sql, employeeRowMapper, id);
    }

    @Override
    public List<Employee> findAll() {
        String sql = "SELECT * FROM employee";
        return genericDAL.queryForList(sql, employeeRowMapper);
    }

    @Override
    public Long create(Employee employee) {
        String sql = "INSERT INTO employee (roleId, name, gender, username, password, phone, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return genericDAL.insert(sql,
                employee.getRoleId(),
                employee.getName(),
                employee.getGender(),
                employee.getUsername(),
                employee.getPassword(),
                employee.getPhone(),
                employee.getAddress()
        );
    }

    @Override
    public boolean update(Employee employee) {
        String sql = "UPDATE employee SET name = ?, gender = ? username = ?, password = ?, phone = ?, address = ? WHERE id = ?";
        return genericDAL.update(sql, employee.getName(), employee.getGender(), employee.getUsername(), employee.getPassword(), employee.getPhone(), employee.getAddress(), employee.getId());
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        return genericDAL.delete(sql, id);
    }
}
