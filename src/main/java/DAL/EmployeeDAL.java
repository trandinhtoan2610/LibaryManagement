package DAL;

import DAL.Interface.IEmployeeDAL;
import DAL.Interface.RowMapper;
import DTO.Employee;
import DTO.Enum.Gender;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EmployeeDAL implements IEmployeeDAL {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<Employee> employeeRowMapper = this::mapRowToEmployee;

    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        Gender gender = Gender.valueOf(rs.getString("gender"));
        return new Employee(
                rs.getLong("id"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                gender,
                rs.getString("username"),
                rs.getString("password"),
                rs.getLong("roleId"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getFloat("salary")
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
        try {
            String sql = "SELECT * FROM employee";
            return genericDAL.queryForList(sql, employeeRowMapper);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch all employees", e);
        }
    }

    @Override
    public Long create(Employee employee) {
        String sql = "INSERT INTO employee (firstName, lastName, gender, username, password, roleId , phone, address, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return genericDAL.insert(sql,
                employee.getFirstName(),
                employee.getLastName(),
                employee.getGender().name(),
                employee.getUsername(),
                employee.getPassword(),
                employee.getRoleID(),
                employee.getPhone(),
                employee.getAddress(),
                employee.getSalary()
        );
    }

    @Override
    public boolean update(Employee employee) {
        String sql = "UPDATE employee SET firstName = ?, lastName = ?, gender = ?, username = ?, password = ? ,roleId = ?, phone = ?, address = ?, salary = ? WHERE id = ?";
        return genericDAL.update(sql, employee.getFirstName(), employee.getLastName(), employee.getGender().name(), employee.getUsername(), employee.getPassword(),employee.getRoleID(), employee.getPhone(), employee.getAddress(), employee.getSalary(), employee.getId());
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        return genericDAL.delete(sql, id);
    }
    public List<Employee> findByGender(Gender gender) {
        String sql = "SELECT * FROM employee WHERE gender = ?";
        return genericDAL.queryForList(sql, employeeRowMapper, gender.name());
    }
    public long getCurrentID(){
        String sql = "SELECT MAX(id) FROM employee";
        return genericDAL.getMaxID(sql);
    }
}
