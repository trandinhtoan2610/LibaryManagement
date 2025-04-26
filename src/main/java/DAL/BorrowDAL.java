package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;
import DTO.BorrowDTO;
import DTO.Enum.Status;
import DTO.Statistics.MonthData;
import DTO.Statistics.QuarterData;
import DTO.Statistics.StatusData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowDAL implements IRepositoryBase<BorrowDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<BorrowDTO> borrowRowMapper = this::mapRowToBorrow;

    private BorrowDTO mapRowToBorrow(java.sql.ResultSet rs) throws java.sql.SQLException {
        Status status = Status.valueOf(rs.getString("status"));
        return new BorrowDTO(
                rs.getLong("id"),
                rs.getLong("employeeId"),
                rs.getString("readerId"),
                rs.getDate("borrowedDate"),
                rs.getDate("duedate"),
                rs.getDate("actualReturnDate"),
                status
        );
    }

    @Override
    public BorrowDTO findById(Long id) {
        String sql = "SELECT * FROM borrow_in_sheet WHERE id = ?";
        return genericDAL.queryForObject(sql, borrowRowMapper, id);
    }

    @Override
    public List<BorrowDTO> findAll() {
        String sql = "SELECT * FROM borrow_in_sheet";
        return genericDAL.queryForList(sql, borrowRowMapper);
    }

    @Override
    public Long create(BorrowDTO borrowDTO) {
        String sql = "INSERT INTO borrow_in_sheet (employeeId, readerId ,borrowedDate, duedate, status) VALUES (?, ?, ?, ?, ?)";
        return genericDAL.insert(sql,
                borrowDTO.getEmployeeId(),
                borrowDTO.getReaderId(),
                borrowDTO.getBorrowedDate(),
                borrowDTO.getDuedate(),
                borrowDTO.getStatus().name()
        );
    }

    @Override
    public boolean update(BorrowDTO borrowDTO) {
        String sql = "UPDATE borrow_in_sheet SET employeeId = ?, readerId = ?, borrowedDate = ?, duedate = ?, actualReturnDate = ?, status = ? WHERE id = ?";
        Long id = Long.parseLong(borrowDTO.getId().substring(2));
        return genericDAL.update(sql,
                borrowDTO.getEmployeeId(),
                borrowDTO.getReaderId(),
                borrowDTO.getBorrowedDate(),
                borrowDTO.getDuedate(),
                borrowDTO.getActualReturnDate(),
                borrowDTO.getStatus().name(),
                id
        );
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM borrow_in_sheet WHERE id = ?";
        return genericDAL.delete(sql, id);
    }

    public long getCurrentID() {
        String sql = "SELECT MAX(id) FROM borrow_in_sheet";
        return genericDAL.getMaxID(sql);
    }
    public List<Integer> getListYear() {
        List<Integer> listYear = new ArrayList<>();
        try {
            PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("SELECT DATE_FORMAT(borrowedDate, '%Y') AS YearNumber FROM borrow_in_sheet GROUP BY YearNumber");
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                int year = rs.getInt("YearNumber");
                listYear.add(year);
            }
            p.close();
            rs.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listYear;
    }
    public List<QuarterData> getQuarterEmployeeData(int Year){
        List<QuarterData> qlist = new ArrayList<>();
        try{
            PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("SELECT QUARTER(borrowedDate) AS Quy, e.id AS Manhanvien, SUM(quantity) AS Soluong FROM borrow_in_sheet AS bs JOIN borrowdetails AS b ON bs.id = b.borrowSheetId JOIN employee AS e ON bs.employeeId = e.id WHERE DATE_FORMAT(borrowedDate, '%Y') = ? GROUP BY Quy, Manhanvien ORDER BY Quy ASC");
            p.setInt(1, Year);
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                int quarter = rs.getInt("Quy");
                int employeeId = rs.getInt("Manhanvien");
                int soluong = rs.getInt("Soluong");
                qlist.add(new QuarterData(quarter, employeeId, soluong));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return qlist;
    }
    public List<QuarterData> getQuarterReaderData(int Year){
        List<QuarterData> qlist = new ArrayList<>();
        try {
            PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("SELECT QUARTER(borrowedDate) AS Quy,r.id AS Madocgia,  SUM(quantity) AS Soluong FROM borrow_in_sheet AS bs JOIN borrowdetails AS b ON bs.id = b.borrowSheetId JOIN reader AS r ON bs.readerId = r.id WHERE DATE_FORMAT(borrowedDate, '%Y') = ? GROUP BY Quy, Madocgia ORDER BY Quy ASC");
            p.setInt(1, Year);
            ResultSet rs = p.executeQuery();
            while (rs.next()){
                int quarter = rs.getInt("Quy");
                int readerId = rs.getInt("Madocgia");
                int soluong = rs.getInt("Soluong");
                qlist.add(new QuarterData(quarter, readerId, soluong));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return qlist;
    }
    public List<QuarterData> getQuarterBookData(int Year){
        List<QuarterData> qlist = new ArrayList<>();
        try{
            PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("SELECT QUARTER(borrowedDate) AS Quy,\n" +
                    "b.id AS Masach,\n" +
                    "SUM(bd.quantity) AS Soluong\n" +
                    "FROM borrow_in_sheet AS bs\n" +
                    "JOIN borrowdetails AS bd ON bs.id = bd.borrowSheetId\n" +
                    "JOIN book AS b ON bd.bookId = b.id\n" +
                    "WHERE DATE_FORMAT(borrowedDate, '%Y') = ? \n" +
                    "GROUP BY Quy, Masach \n" +
                    "ORDER BY Quy ASC");
            p.setInt(1, Year);
            ResultSet rs = p.executeQuery();
            while (rs.next()){
                int Quy = rs.getInt("Quy");
                int BookId = rs.getInt("Masach");
                int soluong = rs.getInt("Soluong");
                qlist.add(new QuarterData(Quy, BookId, soluong));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return qlist;
    }
    public List<QuarterData> getQuarterBookDataByDate(Date startDate, Date endDate){
        List<QuarterData> qlist = new ArrayList<>();
        try {
            PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("SELECT QUARTER(borrowedDate) AS Quy,\n" +
                    "b.id AS Masach,\n" +
                    "SUM(bd.quantity) AS Soluong\n" +
                    "FROM borrow_in_sheet AS bs\n" +
                    "JOIN borrowdetails AS bd ON bs.id = bd.borrowSheetId\n" +
                    "JOIN book AS b ON bd.bookId = b.id\n" +
                    "WHERE borrowedDate BETWEEN ? AND ? + INTERVAL 1 Day\n" + // cộng thêm giờ
                    "GROUP BY Quy, Masach \n" +
                    "ORDER BY Quy ASC");
            p.setDate(1, new java.sql.Date(startDate.getTime()));
            p.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet rs = p.executeQuery();
            while (rs.next()){
                int Quy = rs.getInt("Quy");
                int BookId = rs.getInt("Masach");
                int soluong = rs.getInt("Soluong");
                qlist.add(new QuarterData(Quy, BookId, soluong));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return qlist;
    }
    public List<QuarterData> getQuarterReaderDataByDate(Date startDate, Date endDate){
        List<QuarterData> qlist = new ArrayList<>();
        try {
            PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("SELECT QUARTER(borrowedDate) AS Quy,\n" +
                    "r.id AS Madocgia,\n" +
                    "SUM(bd.quantity) AS Soluong\n" +
                    "FROM borrow_in_sheet AS bs\n" +
                    "JOIN borrowdetails AS bd ON bs.id = bd.borrowSheetId\n" +
                    "JOIN reader AS r ON bs.readerId = r.id\n" +
                    "WHERE borrowedDate BETWEEN ? AND ? + INTERVAL 1 day\n" +
                    "GROUP BY Quy, Madocgia \n" +
                    "ORDER BY Quy ASC");
            p.setDate(1, new java.sql.Date(startDate.getTime()));
            p.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet rs = p.executeQuery();
            while (rs.next()){
                int quy = rs.getInt("Quy");
                int readerId = rs.getInt("Madocgia");
                int soluong = rs.getInt("Soluong");
                qlist.add(new QuarterData(quy, readerId, soluong));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return qlist;
    }
    public List<QuarterData> getQuarterEmployeeDataByDate(Date startDate, Date endDate){
        List<QuarterData> qlist = new ArrayList<>();
        try {
            PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("SELECT QUARTER(borrowedDate) AS Quy,\n" +
                    "e.id AS Manhanvien,\n" +
                    "SUM(bd.quantity) AS Soluong\n" +
                    "FROM borrow_in_sheet AS bs\n" +
                    "JOIN borrowdetails AS bd ON bs.id = bd.borrowSheetId\n" +
                    "JOIN employee AS e ON bs.employeeId = e.id\n" +
                    "WHERE borrowedDate BETWEEN ? AND ? + INTERVAL 1 day\n" +
                    "GROUP BY Quy, Manhanvien \n" +
                    "ORDER BY Quy ASC");
            p.setDate(1, new java.sql.Date(startDate.getTime()));
            p.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet rs = p.executeQuery();
            while (rs.next()){
                int quy = rs.getInt("Quy");
                int employeeId = rs.getInt("Manhanvien");
                int soluong = rs.getInt("Soluong");
                qlist.add(new QuarterData(quy, employeeId, soluong));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return qlist;
    }
    public List<MonthData> getListMonthForYear(int year){
        List<MonthData> list = new ArrayList<>();
        try {
            PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("SELECT MONTH(borrowedDate) AS Thang,\n" +
                    "SUM(bd.quantity) AS Soluong\n" +
                    "FROM borrow_in_sheet AS bs\n" +
                    "JOIN borrowdetails AS bd ON bs.id = bd.borrowSheetId\n" +
                    "WHERE YEAR(borrowedDate) = ?\n" +
                    "GROUP BY Thang \n" +
                    "ORDER BY Thang ASC");
            p.setInt(1, year);
            ResultSet rs = p.executeQuery();
            while (rs.next()){
                int thang = rs.getInt("Thang");
                int soluong = rs.getInt("Soluong");
                list.add(new MonthData(thang, soluong));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    public List<MonthData> getListMonthByDate(Date startDate, Date endDate){
        List<MonthData> list = new ArrayList<>();
        try {
            PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("SELECT MONTH(borrowedDate) AS Thang,\n" +
                    "SUM(bd.quantity) AS Soluong\n" +
                    "FROM borrow_in_sheet AS bs\n" +
                    "JOIN borrowdetails AS bd ON bs.id = bd.borrowSheetId\n" +
                    "WHERE borrowedDate BETWEEN ? AND ? + INTERVAL 1 Day \n" +
                    "GROUP BY Thang\n" +
                    "ORDER BY Thang");
            p.setDate(1, new java.sql.Date(startDate.getTime()));
            p.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet rs = p.executeQuery();
            while (rs.next()){
                int thang = rs.getInt("Thang");
                int soluong = rs.getInt("Soluong");
                list.add(new MonthData(thang, soluong));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    public List<StatusData> getListStatusByYear(int year){
        List<StatusData> list = new ArrayList<>();
        try {
            PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("SELECT id, status \n" +
                    "FROM borrow_in_sheet\n" +
                    "WHERE YEAR(borrowedDate) = ?");
            p.setInt(1, year);
            ResultSet rs = p.executeQuery();
            while (rs.next()){
                Long id = rs.getLong("id");
                Status status = Status.valueOf(rs.getString("status"));
                list.add(new StatusData(id, status));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    public List<StatusData> getListStatusByDate(Date startDate, Date endDate){
        if (startDate == null || endDate == null) {
            return null;
        }
        List<StatusData> list = new ArrayList<>();
        try {
            PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("SELECT id, status \n" +
                    "FROM borrow_in_sheet\n" +
                    "WHERE borrowedDate BETWEEN ? AND ?");
            p.setDate(1, new java.sql.Date(startDate.getTime()));
            p.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet rs = p.executeQuery();
            while (rs.next()){
                Long id = rs.getLong("id");
                Status status = Status.valueOf(rs.getString("status"));
                list.add(new StatusData(id, status));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}
