
package DTO;
import DTO.Abstract.EntityAuditGPABase;
import DTO.Enum.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReaderDTO extends EntityAuditGPABase<String> {
    private boolean isActive = true;

    public ReaderDTO() {};
    public ReaderDTO(String id, String firstName, String lastName, Gender gender, String phone, String address) {
        this.id = id;
        this.firstName = lastName;
        this.lastName = firstName;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }
}
