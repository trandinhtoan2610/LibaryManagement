package BUS;

import DAL.EmployeeDAL;
import DTO.Employee;

import java.util.List;


public class EmployeeBUS {
    private final EmployeeDAL employeeDAL = new EmployeeDAL();

    public Employee login(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username or password is null or empty");
        }
        return employeeDAL.login(username, password);
    }
    public List<Employee> getAllEmployees() {
        return employeeDAL.findAll();
    }
    public long addEmployee(Employee employee) {
        return employeeDAL.create(employee);
    }
    public long updateEmployee(Employee employee) {
        return employeeDAL.create(employee);
    }
    public boolean deleteEmployee(long id) {
        return employeeDAL.delete(id);
    }
    public Employee create(Employee employee) {
        Long id = employeeDAL.create(employee);
        if (id != null) {
            return employeeDAL.findById(id);
        }
        return null;
    }
    public long getCurrentID(){
        return employeeDAL.getCurrentID();
    }
}
