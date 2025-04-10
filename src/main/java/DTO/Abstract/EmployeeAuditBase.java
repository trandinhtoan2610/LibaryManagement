package DTO.Abstract;


public class EmployeeAuditBase extends EntityAuditGPABase<Long> {
    protected Long salary;
    protected Long roleId;
    protected String username, password;

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Long getRoleID() {
        return roleId;
    }

    public void setRoleID(Long roleID) {
        this.roleId = roleID;
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
}
