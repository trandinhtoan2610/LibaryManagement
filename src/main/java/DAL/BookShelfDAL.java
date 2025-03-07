package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;
import DTO.Bookshelf;

import java.util.List;

public class BookShelfDAL implements IRepositoryBase<Bookshelf> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<Bookshelf> bookshelfRowMapper = this::mapRowToBookShelf;

    private Bookshelf mapRowToBookShelf(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new Bookshelf(
                rs.getLong("id"),
                rs.getString("name")
        );
    }

    @Override
    public Bookshelf findById(Long id) {
        String sql = "SELECT * FROM bookshelf WHERE id = ?";
        return genericDAL.queryForObject(sql, bookshelfRowMapper, id);
    }

    @Override
    public List<Bookshelf> findAll() {
        String sql = "SELECT * FROM bookshelf";
        return genericDAL.queryForList(sql, bookshelfRowMapper);
    }

    @Override
    public Long create(Bookshelf bookshelf) {
        String sql = "INSERT INTO bookshelf (name) VALUES (?)";
        return genericDAL.insert(sql, bookshelf.getName());
    }

    @Override
    public boolean update(Bookshelf bookshelf) {
        String sql = "UPDATE bookshelf SET name = ? WHERE id = ?";
        return genericDAL.update(sql, bookshelf.getName(), bookshelf.getId());
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM bookshelf WHERE id = ?";
        return genericDAL.delete(sql, id);
    }
}
