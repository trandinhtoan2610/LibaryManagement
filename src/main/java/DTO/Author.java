package DTO;

import DTO.Abstract.EntityAuditNameBase;
import DTO.Enum.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Author extends EntityAuditNameBase<Long> {
    private Gender gender;
    private String phone, address;

    public Author(Long id, String name,Gender gender, String phone, String address, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
