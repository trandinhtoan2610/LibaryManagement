package DAL;

import DAL.Interface.IRepositoryBase;
import DTO.Book;

import java.util.List;

public class BookDAL implements IRepositoryBase<Book> {
    @Override
    public Book findById(Long id) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return List.of();
    }

    @Override
    public Long create(Book book) {
        return 0L;
    }

    @Override
    public boolean update(Book book) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
