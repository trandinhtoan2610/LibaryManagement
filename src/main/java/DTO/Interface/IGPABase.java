package DTO.Interface;

import DTO.Enum.Gender;

public interface IGPABase {
    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    Gender getGender();

    void setGender(Gender gender);

    String getPhone();

    void setPhone(String phone);

    String getAddress();

    void setAddress(String address);
}
