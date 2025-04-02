package DAL.Interface;

import java.util.List;

public interface IRepositoryStringID<T> {
    T findById(String id);

    List<T> findAll();

    Long create(T t);

    boolean update(T t);

    boolean delete(String id);
}
