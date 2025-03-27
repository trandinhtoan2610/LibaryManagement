package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;
import DTO.AuthorDTO;
import DTO.Enum.Gender;
import java.sql.ResultSet;
import java.util.List;



public class AuthorDAL implements IRepositoryBase<AuthorDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<AuthorDTO> authorDTORowMapper = (ResultSet rs)-> new AuthorDTO(
            rs.getLong("id"),
            rs.getString("firstName"),
            rs.getString("lastName"),
            Gender.valueOf(rs.getString("gender")),
            rs.getString("phone"),
            rs.getString("address")
    );

    @Override
    public AuthorDTO findById(Long id) {
        String sql = "SELECT * FROM Author WHERE id = ? ";
        return genericDAL.queryForObject(sql,authorDTORowMapper,id);
    }

    @Override
    public List<AuthorDTO> findAll() {
        String sql = "SELET * FROM Author";
        return genericDAL.queryForList(sql,authorDTORowMapper);
    }

    @Override
    public Long create(AuthorDTO authorDTO) {
        String sql = "INSERT INTO Author (firstName, lastName, gender, phone, address)" +
                " VALUES (?, ?, ?, ?, ? )";
        return genericDAL.insert(sql,authorDTO.getFirstName(),authorDTO.getLastName(),authorDTO.getGender().toString()
                , authorDTO.getPhone(),authorDTO.getAddress());
    }

    @Override
    public boolean update(AuthorDTO authorDTO) {
        String sql = "UPDATE Author SET " +
                "firstName = ?, " +
                "lastName = ?, " +
                "gender = ?, " +
                "phone = ?, " +
                "address = ? " +
                "WHERE id = ?";
        return genericDAL.update(sql, authorDTO.getFirstName(), authorDTO.getLastName(), authorDTO.getGender().toString(),
                authorDTO.getPhone(), authorDTO.getAddress(), authorDTO.getId() );
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM Author WHERE id = ?";
        return genericDAL.delete(sql, id);
    }
}
