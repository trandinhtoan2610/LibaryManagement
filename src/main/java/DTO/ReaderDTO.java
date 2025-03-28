
package DTO;
import DTO.Abstract.EntityAuditGPABase;
import DTO.Enum.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReaderDTO extends EntityAuditGPABase<Long> {
    public ReaderDTO(Long id, String firstName, String lastName, Gender gender, String phone, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }
}
