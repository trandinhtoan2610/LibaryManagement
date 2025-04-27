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
            rs.getString("lastName"),
            rs.getString("firstName"),
            rs.getLong("quantity")
    );

    @Override
    public AuthorDTO findById(Long id) {
        String sql = "SELECT * FROM Author WHERE id = ? AND isActive = 1";
        return genericDAL.queryForObject(sql,authorDTORowMapper,id);
    }

    @Override
    public List<AuthorDTO> findAll() {
        String sql = "SELECT * FROM Author WHERE isActive = 1";
        return genericDAL.queryForList(sql,authorDTORowMapper);
    }

    @Override
    public Long create(AuthorDTO authorDTO) {
        String sql = "INSERT INTO Author (firstName, lastName, quantity)" +
                " VALUES (?, ?, ?)";
        return genericDAL.insert(sql,authorDTO.getFirstName(),authorDTO.getLastName(), authorDTO.getProductQuantity());
    }

    @Override
    public boolean update(AuthorDTO authorDTO) {
        String sql = "UPDATE Author SET " +
                "firstName = ?, " +
                "lastName = ?, " +
                "quantity = ? " +
                "WHERE id = ?";
        return genericDAL.update(sql, authorDTO.getFirstName(), authorDTO.getLastName(), authorDTO.getProductQuantity(), authorDTO.getId() );
    }

    @Override
    public boolean delete(Long id) {
        String sql = "UPDATE Author SET isActive = 0 WHERE id = ?";
        return genericDAL.delete(sql, id);
    }

    public Long getMaxID(){
        String sql = "SELECT MAX(id) FROM Author";
        return genericDAL.getMaxID(sql);
    }
}
