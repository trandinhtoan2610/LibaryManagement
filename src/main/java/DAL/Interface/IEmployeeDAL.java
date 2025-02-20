package DAL.Interface;

import DTO.Employee;

public interface IEmployeeDAL extends IRepositoryBase<Employee> {
    Employee login(String username, String password);
}
