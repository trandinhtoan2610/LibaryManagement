package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.IRepositoryStringID;
import DAL.Interface.RowMapper;
import DTO.Enum.Gender;
import DTO.ReaderDTO;

import java.sql.ResultSet;
import java.util.List;

public class ReaderDAL implements IRepositoryStringID<ReaderDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<ReaderDTO> readerRowMapper = (ResultSet rs) -> {
        String id = rs.getString("id");
        String firstName = rs.getString("firstName");
        String lastName = rs.getString("lastName");
        Gender gender = Gender.valueOf(rs.getString("gender").toString());
        String phone = rs.getString("phone");
        String address = rs.getString("address");
        int count = rs.getInt("complianceCount");
        return new ReaderDTO(id,firstName,lastName,gender,phone,address,count);
    };

    @Override
    public ReaderDTO findById(String id) {
        String sql = "SELECT * FROM Reader WHERE id = ? AND isActive = 1";
        return genericDAL.queryForObject(sql, readerRowMapper, id);
    }

    @Override
    public List<ReaderDTO> findAll() {
        String sql = "SELECT * FROM Reader WHERE isActive = 1";
        return genericDAL.queryForList(sql,readerRowMapper);
    }

    @Override
    public Long create(ReaderDTO readerDTO) {
        String sql = "INSERT INTO Reader (id, firstName, lastName, gender, phone, address, complianceCount) " +
                "Values(?,?,?,?,?,?,?)";
        return genericDAL.insert(sql, readerDTO.getId(), readerDTO.getFirstName(), readerDTO.getLastName(),
                readerDTO.getGender().toString(), readerDTO.getPhone(), readerDTO.getAddress(), 3);
    }

    @Override
    public boolean update(ReaderDTO readerDTO) {
        String sql = "UPDATE Reader " +
                "SET id = ?, " +
                "firstName = ?, " +
                "lastName = ?, " +
                "gender = ?, " +
                "phone = ?, " +
                "address = ?" +
                "WHERE id = ?";
        return genericDAL.update(sql, readerDTO.getId(), readerDTO.getFirstName(), readerDTO.getLastName(),
                readerDTO.getGender().toString(), readerDTO.getPhone(), readerDTO.getAddress(), readerDTO.getId());
    }

    @Override
    public boolean delete(String id) {
        String sql = "UPDATE Reader SET isActive = 0 WHERE id = ? ";
        return genericDAL.delete(sql,id);
    }
    
    public Long getCurrentID(){
        String sql = "SELECT MAX(CAST(SUBSTRING(id, 3) AS UNSIGNED)) FROM Reader;";
        return genericDAL.getMaxID(sql);
    }
}