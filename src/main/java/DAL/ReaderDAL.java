package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;
import DTO.Enum.Gender;
import DTO.ReaderDTO;

import java.sql.ResultSet;
import java.util.List;

public class ReaderDAL implements IRepositoryBase<ReaderDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<ReaderDTO> readerRowMapper = (ResultSet rs) -> {
        Long id = rs.getLong("id");
        String firstName = rs.getString("firstName");
        String lastName = rs.getString("lastName");
        Gender gender = Gender.valueOf(rs.getString("gender"));
        String phone = rs.getString("phone");
        String address = rs.getString("address");
        return new ReaderDTO(id,firstName,lastName,gender,phone,address);
    };

    @Override
    public ReaderDTO findById(Long id) {
        String sql = "SELECT * FROM Reader WHERE id = ?";
        return genericDAL.queryForObject(sql, readerRowMapper, id);
    }

    @Override
    public List<ReaderDTO> findAll() {
        String sql = "SELECT * FROM Reader";
        return genericDAL.queryForList(sql,readerRowMapper);
    }

    @Override
    public Long create(ReaderDTO readerDTO) {
        String sql = "INSERT INTO Reader ( firstName, lastName, gender, phone, address) " +
                "Values(?,?,?,?,?)";
        return genericDAL.insert(sql, readerDTO.getFirstName(), readerDTO.getLastName(),
                readerDTO.getGender().toString(), readerDTO.getPhone(), readerDTO.getAddress());
    }

    @Override
    public boolean update(ReaderDTO readerDTO) {
        String sql = "UPDATE Reader " +
                "SET firstName = ?, " +
                "lastName = ?, " +
                "gender = ?, " +
                "phone = ?, " +
                "address = ?" +
                "WHERE id = ?";
        return genericDAL.update(sql, readerDTO.getFirstName(), readerDTO.getLastName(),
                readerDTO.getGender().toString(), readerDTO.getPhone(), readerDTO.getAddress(), readerDTO.getId());
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM Reader WHERE id = ? ";
        return genericDAL.delete(sql,id);
    }
}