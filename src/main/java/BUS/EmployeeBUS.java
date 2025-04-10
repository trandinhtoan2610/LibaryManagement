package BUS;

import DAL.EmployeeDAL;
import DTO.Employee;

import java.util.ArrayList;
import java.util.List;


public class EmployeeBUS {
    private final EmployeeDAL employeeDAL = new EmployeeDAL();

    public Employee login(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username or password is null or empty");
        }
        return employeeDAL.login(username, password);
    }
    public Employee getEmployeeById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return employeeDAL.findById(id);
    }
    public List<Employee> getAllEmployees() {
        return employeeDAL.findAll();
    }
    public long addEmployee(Employee employee) {
        return employeeDAL.create(employee);
    }

    public boolean updateEmployee(Employee employee) {
        try {
            return employeeDAL.update(employee);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteEmployee(long id) {
        try {
            return employeeDAL.delete(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public long getCurrentID(){
        return employeeDAL.getCurrentID();
    }
}
