package DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PublisherDTO {
    private Long id;
    private String name, phone, address;

    public PublisherDTO(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }
    public PublisherDTO(Long id, String name, String phone, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }
}