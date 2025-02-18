package DAL.Interface;

import DTO.Employee;

import java.util.List;


public interface IEmployeeDAL {
    Employee findById(Long id);
    List<Employee> findAll();
    Long create(Employee employee);
    boolean update(Employee employee);
    boolean delete(Long id);
}
