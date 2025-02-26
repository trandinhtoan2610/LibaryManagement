package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;
import DTO.Publisher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PublisherDAL implements IRepositoryBase<Publisher> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<Publisher> publisherRowMapper = this::mapRowToPublisher;
    public Publisher mapRowToPublisher(ResultSet rs) throws SQLException {
        return new Publisher(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getDate("createdAt"),
                rs.getDate("updatedAt")
        );
    }
    @Override
    public Publisher findById(Long id) {
        String sql = "SELECT * FROM publisher WHERE id = ?";
        return genericDAL.queryForObject(sql, publisherRowMapper, id);
    }

    @Override
    public List<Publisher> findAll() {
        String sql = "SELECT * FROM publisher";
        return genericDAL.queryForList(sql, publisherRowMapper);
    }

    @Override
    public Long create(Publisher publisher) {
        String sql = "INSERT INTO publisher (name, phone, address) VALUES (?, ?, ?)";
        return genericDAL.insert(sql, publisher.getName(),publisher.getPhone(), publisher.getAddress());
    }

    @Override
    public boolean update(Publisher publisher) {
        String sql = "UPDATE publisher SET name = ?,SET phone = ? address = ? WHERE id = ?";
        return genericDAL.update(sql, publisher.getName(),publisher.getPhone(), publisher.getAddress(), publisher.getId());
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM publisher WHERE id = ?";
        return genericDAL.delete(sql, id);
    }
}
