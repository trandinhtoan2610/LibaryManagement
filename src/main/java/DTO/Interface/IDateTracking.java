package DTO.Interface;

import java.util.Date;


public interface IDateTracking {
    Date getCreatedAt();

    void setCreatedAt(Date createdAt);

    Date getUpdatedAt();

    void setUpdatedAt(Date updatedAt);
}
