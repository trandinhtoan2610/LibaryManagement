package DTO;
import DTO.Abstract.EntityAuditBase;
import DTO.Abstract.EntityAuditNameBase;

import java.util.Date;


public class Employee extends EntityAuditNameBase<Long> {
    private String username,password,phone;
    private Long roleId;

    public Employee(Long id, Long roleId, String name, String username, String password, String phone, Date createdAt, Date updatedAt) {
        this.id = id;
        this.roleId = roleId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Employee(Long roleId, String name, String username, String password, String phone) {
        this.roleId = roleId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
