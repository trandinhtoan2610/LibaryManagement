
package DAL;

import DAL.Interface.RowMapper;
import DTO.AuthorBookDTO;
import java.util.List;
import java.sql.ResultSet;
import java.time.Year;



public class AuthorBookDAL {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<AuthorBookDTO> BookRowMapper = (ResultSet rs) -> new AuthorBookDTO(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("categoryName"),
            rs.getString("authorName"),
            rs.getString("publisherName"),
            rs.getInt("quantity"),
            rs.getLong("unitprice"),
            Year.of(rs.getInt("yearOfpublication"))
    );
    
    
    public List<AuthorBookDTO> findBookByAuthorID(Long id){
        String sql = "SELECT \n" +
                    "	book.id, \n" +
                    "	book.name,\n" +
                    "	category.name AS categoryName,\n" +
                    "	CONCAT(author.lastName, ' ', author.firstName) AS authorName,\n" +
                    "	CONCAT(publisher.lastName, ' ', publisher.firstName) AS publisherName,\n" +
                    "	book.quantity,\n" +
                    "	book.unitprice,\n" +
                    "	book.yearOfpublication\n" +
                    "	\n" +
                    "FROM book\n" +
                    "JOIN category ON category.id = book.categoryId\n" +
                    "JOIN author ON author.id = book.authorId\n" +
                    "JOIN publisher ON publisher.id = book.publisherId\n" +
                    "WHERE book.authorId = ? ";
        return genericDAL.queryForList(sql, BookRowMapper, id);
    }
    
    
    
}
