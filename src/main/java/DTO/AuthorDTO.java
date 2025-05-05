package DTO;

import DTO.Abstract.EntityAuditGPABase;
import DTO.Enum.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDTO {
    private Long id;
    private String lastName;
    private String firstName;
    private long productQuantity;
    private boolean isActive = true;

    public AuthorDTO() {}

    public AuthorDTO(Long id, String lastName, String firstName, long quantity) {
        this.id = id;
        this.lastName = firstName;
        this.firstName = lastName;
        this.productQuantity = quantity;
    }


    public String getName() {
        return (lastName != null ? lastName : "") + " " + (firstName != null ? firstName : "");
    }

    public void setProductQuantity(long productQuantity) {
        this.productQuantity = productQuantity;
    }
    
    
    
    
}