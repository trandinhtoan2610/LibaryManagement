package DTO;

import DTO.Abstract.EntityAuditGPABase;
import DTO.Abstract.EntityAuditNameBase;
import DTO.Enum.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Author extends EntityAuditGPABase<Long> {
    public Author(){};
    public Author(Long id, String firstName,String lastName, Gender gender, String phone, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }
}
