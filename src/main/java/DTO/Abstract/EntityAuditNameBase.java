package DTO.Abstract;

import DTO.Interface.INameBase;

public abstract class EntityAuditNameBase<T> extends EntityAuditBase<T> implements INameBase {
    protected String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
