package DAL;

import DAL.Interface.IDetailsBase;
import DAL.Interface.RowMapper;
import DTO.BorrowDetailDTO;
import DTO.Enum.SubStatus;

import java.sql.ResultSet;
import java.util.List;

public class BorrowDetailDAL implements IDetailsBase<BorrowDetailDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<BorrowDetailDTO> borrowRowMapper = this::mapRowToBorrowDetails;

    private BorrowDetailDTO mapRowToBorrowDetails(ResultSet rs) throws Exception {
        SubStatus subStatus = SubStatus.valueOf(rs.getString("substatus"));
        return new BorrowDetailDTO(
                rs.getLong("bookId"),
                rs.getLong("borrowSheetId"),
                rs.getInt("quantity"),
                subStatus,
                rs.getDate("actualReturnDate")
        );
    }
    @Override
    public List<BorrowDetailDTO> findAll() {
        String sql = "SELECT * FROM borrowdetails";
        return genericDAL.queryForList(sql, borrowRowMapper);
    }

    @Override
    public Long create(BorrowDetailDTO borrowDetailDTO) {
        String sql = "INSERT INTO borrowdetails (bookId, borrowSheetId, quantity, substatus, actualReturnDate) VALUES (?, ?, ?, ?, ?)";
        Long id = Long.parseLong(borrowDetailDTO.getBorrowSheetId().substring(2));
        return genericDAL.insert(sql,
                borrowDetailDTO.getBookId(),
                id,
                borrowDetailDTO.getQuantity(),
                borrowDetailDTO.getStatus().name(),
                borrowDetailDTO.getActualReturnDate()
        );
    }

    @Override
    public boolean update(BorrowDetailDTO borrowDetailDTO) {
        String sql = "UPDATE borrowdetails SET quantity = ?, substatus = ?,actualReturnDate = ? WHERE bookid = ? AND borrowSheetId = ?";
        Long id = Long.parseLong(borrowDetailDTO.getBorrowSheetId().substring(2));
        System.out.println(borrowDetailDTO.toString());
        return genericDAL.update(sql,
                borrowDetailDTO.getQuantity(),
                borrowDetailDTO.getStatus().name(),
                borrowDetailDTO.getActualReturnDate(),
                borrowDetailDTO.getBookId(),
                id
        );
    }
    @Override
    public boolean delete(BorrowDetailDTO borrowDetailDTO) {
        String sql = "DELETE FROM borrowdetails WHERE bookId = ? AND borrowSheetId = ? AND quantity = ? AND substatus = ? AND actualReturnDate = ?";
        return genericDAL.update(sql, borrowDetailDTO.getBookId(),
                borrowDetailDTO.getBorrowSheetId(),
                borrowDetailDTO.getQuantity(),
                borrowDetailDTO.getStatus().name(),
                borrowDetailDTO.getActualReturnDate()
        );
    }
    public List<BorrowDetailDTO> findByBorrowSheetId(Long borrowSheetId) {
        String sql = "SELECT * FROM borrowdetails WHERE borrowSheetId = ?";
        return genericDAL.queryForList(sql, borrowRowMapper, borrowSheetId);
    }

}
