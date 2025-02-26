package DTO;

import DTO.Abstract.EntityAuditNameBase;
import DTO.Enum.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class Customer extends EntityAuditNameBase<Long> {
    private String phone;
    private Gender gender;
    public Customer(Long id, String name,Gender gender, String phone, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
