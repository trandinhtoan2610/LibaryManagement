package DTO;
import DTO.Abstract.EntityAuditBase;
import DTO.Abstract.EntityAuditNameBase;
import DTO.Enum.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class Employee extends EntityAuditNameBase<Long> {
    private String username,password,phone;
    private Long roleId;
    private Gender gender;
    public Employee(Long id, Long roleId, String name, Gender gender,  String username, String password, String phone, Date createdAt, Date updatedAt) {
        this.id = id;
        this.roleId = roleId;
        this.name = name;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Employee(Long roleId, String name,Gender gender, String username, String password, String phone) {
        this.roleId = roleId;
        this.name = name;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.phone = phone;
    }
}
