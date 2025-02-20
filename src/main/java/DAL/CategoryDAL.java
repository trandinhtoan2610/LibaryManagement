package DAL;

import DAL.Interface.IRepositoryBase;
import DTO.Category;

import java.util.List;

public class CategoryDAL implements IRepositoryBase<Category> {
    @Override
    public Category findById(Long id) {
        return null;
    }

    @Override
    public List<Category> findAll() {
        return List.of();
    }

    @Override
    public Long create(Category category) {
        return 0L;
    }

    @Override
    public boolean update(Category category) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
