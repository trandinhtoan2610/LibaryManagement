package DTO;

import DTO.Abstract.EntityAuditGPABase;
import DTO.Enum.Gender;
import lombok.Getter;
import lombok.Setter;


public class AuthorDTO {
    
    private long id;
    private String lastName;
    private String firstName;
    private long productQuantity;
    
    public AuthorDTO(){};
    
    public AuthorDTO(Long id, String lastName, String firstName, long quantity){
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.productQuantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setProductQuantity(long quantity){
        this.productQuantity = quantity;
    }
    
    public Long getProductQuantity(){
        return this.productQuantity;
    }
}
