package BUS;

import DAL.EmployeeDAL;
import DTO.Employee;


public class EmployeeBUS {
    private final EmployeeDAL employeeDAL = new EmployeeDAL();

    public Employee login(String username, String password){
        if(username == null || password == null)
            return null;
        return employeeDAL.Login(username,password);
    }
}
