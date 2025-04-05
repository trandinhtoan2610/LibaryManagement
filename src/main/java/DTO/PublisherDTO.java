package DTO;

import DTO.Abstract.EntityAuditNameBase;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PublisherDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    public PublisherDTO() {
    }

    public PublisherDTO(Long id, String firstName, String lastName, String phone, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }
    public String getName(){
        return firstName+" "+lastName;
    }
}
