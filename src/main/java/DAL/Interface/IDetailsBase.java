package DAL.Interface;

import java.util.List;

public interface IDetailsBase<T> {
    List<T> findAll();

    Long create(T t);

    boolean update(T t);

    boolean delete(Long idparent, String idparent2);
}
