package DTO.Abstract;

import DTO.Interface.IEntityBase;

public abstract class EntityBase<T> implements IEntityBase<T> {
    protected T id;

    @Override
    public T getId() {
        return id;
    }

    @Override
    public void setId(T id) {
        this.id = id;
    }
}
