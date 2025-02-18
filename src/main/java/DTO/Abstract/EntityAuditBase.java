package DTO.Abstract;
import DTO.Interface.IAuditable;

import java.util.Date;


public abstract class EntityAuditBase<T> extends EntityBase<T> implements IAuditable {
    protected Date createdAt;
    protected Date updatedAt;

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
