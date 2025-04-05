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
        try {
            return new PublisherDTO(
                    rs.getLong("id"),
                    rs.getString("firstName"), // Ánh xạ firstName
                    rs.getString("lastName"),  // Ánh xạ lastName
                    rs.getString("phone"),
                    rs.getString("address")
            );
        } catch (SQLException e) {
            throw new SQLException("Lỗi ánh xạ dữ liệu từ ResultSet sang PublisherDTO: " + e.getMessage(), e);
        }
    }

    @Override
    public PublisherDTO findById(Long id) {
        String sql = "SELECT * FROM publisher WHERE id = ?";
        return genericDAL.queryForObject(sql, publisherRowMapper, id);
    }

    @Override
    public List<PublisherDTO> findAll() {
        String sql = "SELECT * FROM publisher";
        return genericDAL.queryForList(sql, publisherRowMapper);
    }

    @Override
    public Long create(PublisherDTO publisherDTO) {
        String sql = "INSERT INTO publisher (firstName, lastName, phone, address) VALUES (?, ?, ?, ?)";
        return genericDAL.insert(sql, publisherDTO.getFirstName(), publisherDTO.getLastName(), publisherDTO.getPhone(), publisherDTO.getAddress());
    }

    @Override
    public boolean update(PublisherDTO publisherDTO) {
        String sql = "UPDATE publisher SET firstName = ?, lastName = ?, phone = ?, address = ? WHERE id = ?";
        return genericDAL.update(sql, publisherDTO.getFirstName(), publisherDTO.getLastName(), publisherDTO.getPhone(), publisherDTO.getAddress(), publisherDTO.getId());
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM publisher WHERE id = ?";
        return genericDAL.delete(sql, id);
    }

    public long getCurrentID() {
        String sql = "SELECT MAX(id) FROM publisher";
        return genericDAL.getMaxID(sql);
    }
}