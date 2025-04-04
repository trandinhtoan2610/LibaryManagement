package DTO;

import DTO.Abstract.EmployeeAuditBase;
import DTO.Enum.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee extends EmployeeAuditBase {
    public Employee(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Employee(Long id, String firstName, String lastName, Gender gender, String username, String password, Long roleId, String phone, String address, Float salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.phone = phone;
        this.address = address;
        this.salary = salary;
    }
    public Employee(Long roleId, String firstName, String lastName, Gender gender, String username, String password, String phone, String address) {
        this.roleId = roleId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }
}
