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
        SubStatus subStatus = SubStatus.valueOf(rs.getString("subStatus"));
        return new BorrowDetailDTO(
                rs.getLong("bookId"),
                rs.getLong("borrowSheetId"),
                rs.getInt("quantity"),
                subStatus
        );
    }
    @Override
    public List<BorrowDetailDTO> findAll() {
        String sql = "SELECT * FROM borrowdetails";
        return genericDAL.queryForList(sql, borrowRowMapper);
    }

    @Override
    public Long create(BorrowDetailDTO borrowDetailDTO) {
        String sql = "INSERT INTO borrowdetails (bookId, borrowSheetId, quantity, status) VALUES (?, ?, ?, ?)";
        return genericDAL.insert(sql,
                borrowDetailDTO.getBookId(),
                borrowDetailDTO.getBorrowSheetId(),
                borrowDetailDTO.getQuantity(),
                borrowDetailDTO.getStatus().name()
        );
    }

    @Override
    public boolean update(BorrowDetailDTO borrowDetailDTO) {
        String sql = "UPDATE borrowdetails SET quantity = ?, status = ? WHERE bookid = ? AND borrowSheetId = ?";
        return genericDAL.update(sql,
                borrowDetailDTO.getQuantity(),
                borrowDetailDTO.getStatus().name(),
                borrowDetailDTO.getBookId(),
                borrowDetailDTO.getBorrowSheetId()
        );
    }
    @Override
    public boolean delete(BorrowDetailDTO borrowDetailDTO) {
        String sql = "DELETE FROM borrowdetails WHERE bookId = ? AND borrowSheetId = ? AND quantity = ? AND status = ?";
        return genericDAL.update(sql, borrowDetailDTO.getBookId(),
                borrowDetailDTO.getBorrowSheetId(),
                borrowDetailDTO.getQuantity(),
                borrowDetailDTO.getStatus().name()
        );
    }
}
