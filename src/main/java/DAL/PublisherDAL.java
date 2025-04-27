package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;
import DTO.PublisherDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PublisherDAL implements IRepositoryBase<PublisherDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<PublisherDTO> publisherRowMapper = this::mapRowToPublisher;

    public PublisherDTO mapRowToPublisher(ResultSet rs) throws SQLException {
        return new PublisherDTO(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("address")
        );
    }

    @Override
    public PublisherDTO findById(Long id) {
        String sql = "SELECT * FROM publisher WHERE id = ? AND isActive = 1";
        return genericDAL.queryForObject(sql, publisherRowMapper, id);
    }

    @Override
    public List<PublisherDTO> findAll() {
        String sql = "SELECT * FROM publisher WHERE isActive = 1";
        return genericDAL.queryForList(sql, publisherRowMapper);
    }

    @Override
    public Long create(PublisherDTO publisherDTO) {
        String sql = "INSERT INTO publisher (name, phone, address) VALUES (?, ?, ?)";
        return genericDAL.insert(sql, publisherDTO.getName(), publisherDTO.getPhone(), publisherDTO.getAddress());
    }

    @Override
    public boolean update(PublisherDTO publisherDTO) {
        String sql = "UPDATE publisher SET name = ?, phone = ?, address = ? WHERE id = ?";
        return genericDAL.update(sql, publisherDTO.getName(), publisherDTO.getPhone(), publisherDTO.getAddress(), publisherDTO.getId());
    }

    @Override
    public boolean delete(Long id) {
        String sql = "UPDATE publisher SET isActive = 0 WHERE id = ?";
        return genericDAL.delete(sql, id);
    }
    public long getCurrentID() {
        String sql = "SELECT MAX(id) FROM publisher";
        return genericDAL.getMaxID(sql);
    }
}
