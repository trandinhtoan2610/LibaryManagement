package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;
import DTO.Author;
import DTO.Bookshelf;
import DTO.Enum.Gender;

import java.util.List;

public class AuthorDAL implements IRepositoryBase<Author> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<Author> authorRowMapper = this::mapRowToAuthor;

    private Author mapRowToAuthor(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new Author(
                rs.getLong("id"),
                rs.getString("firstname"),
                rs.getString("lastname"),
                Gender.valueOf(rs.getString("gender").toString()),
                rs.getString("phone"),
                rs.getString("address")
        );
    }

    @Override
    public Author findById(Long id) {
        String sql = "SELECT * FROM author WHERE id = ?";
        return genericDAL.queryForObject(sql, authorRowMapper, id);
    }

    @Override
    public List<Author> findAll() {
        String sql = "SELECT * FROM author";
        return genericDAL.queryForList(sql, authorRowMapper);
    }

    @Override
    public Long create(Author author) {
        String sql = "INSERT INTO author (name, gender, phone, address) VALUES (?, ?, ?, ?)";
        return genericDAL.insert(sql, author.getName(), author.getGender(), author.getPhone(), author.getAddress());
    }

    @Override
    public boolean update(Author author) {
        String sql = "UPDATE author SET name = ?,SET gender = ? SET phone = ?, SET address = ? WHERE id = ?";
        return genericDAL.update(sql,
                author.getName(),
                author.getGender(),
                author.getPhone(),
                author.getAddress(),
                author.getId()
        );
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM author WHERE id = ?";
        return genericDAL.delete(sql, id);
    }
}
