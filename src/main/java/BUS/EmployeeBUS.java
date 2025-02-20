package BUS;

import DAL.Interface.IEmployeeDAL;
import DTO.Employee;


public class EmployeeBUS {
    private IEmployeeDAL employeeDAL;
    public Employee login(String username, String password){
        if(username == null || password == null)
            return null;
        return employeeDAL.login(username,password);
    }

    public Employee create(Employee employee){
        Long id = employeeDAL.create(employee);
        if (id != null){
            return employeeDAL.findById(id);
        }
        return null;
    }

    public boolean update(Employee employee){
        return employeeDAL.update(employee);
    }

    public boolean delete(Long id){
        return employeeDAL.delete(id);
    }
}
