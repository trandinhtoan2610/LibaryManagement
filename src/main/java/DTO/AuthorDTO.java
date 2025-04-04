package DTO;

import DTO.Abstract.EntityAuditGPABase;
import DTO.Enum.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDTO {
    private long id;
    private String lastName;
    private String firstName;
    private long productQuantity;

    public AuthorDTO() {}

    public AuthorDTO(Long id, String lastName, String firstName, long quantity) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.productQuantity = quantity;
    }


    public String getName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }
}