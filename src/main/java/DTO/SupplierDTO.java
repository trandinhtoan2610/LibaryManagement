package DTO;

import DTO.Abstract.EntityAuditNameBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SupplierDTO{
    private String id,name,phone, address;
    private boolean isActive = true;

    public SupplierDTO(String id, String name, String phone, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        
    }
}