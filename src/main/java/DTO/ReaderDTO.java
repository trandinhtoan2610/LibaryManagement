
package DTO;
import DTO.Abstract.EntityAuditGPABase;
import DTO.Enum.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReaderDTO extends EntityAuditGPABase<String> {
    private int complianceCount;
    private boolean isActive = true;

    public ReaderDTO() {};
    public ReaderDTO(String id, String firstName, String lastName, Gender gender, String phone, String address, int count) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.complianceCount = count;

    }

    public boolean getStatus() {
       if (complianceCount == 0){
           return false;
       }
       else
          return true;
    }

    public int getComplianceCount() {
        return complianceCount;
    }

    public void setComplianceCount(int complianceCount) {
        this.complianceCount = complianceCount;
    }







}
