package DAL;

import DAL.Interface.IRepositoryDetails;
import DAL.Interface.RowMapper;
import DTO.Enum.PayStatus;
import DTO.PenaltyDTO;
import DTO.Statistics.LostBookPreciousData;
import DTO.Statistics.PenaltyDateData;
import DTO.Statistics.PenaltyTimeData;
import DTO.Statistics.PenaltyPreciousData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PenaltyDAL implements IRepositoryDetails<PenaltyDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<PenaltyDTO> penaltyDTORowMapper = rs -> new PenaltyDTO(
                rs.getString("id"),
                rs.getDate("penaltyDate"),
                PayStatus.valueOf(rs.getString("status")),
                rs.getLong("totalamount"),
                rs.getDate("payDate"),
                rs.getObject("employeeID") == null ? null : rs.getLong("employeeID")
            );



    @Override
    public PenaltyDTO findById(String id) {
        String sql = "SELECT * FROM penalty WHERE id = ? ";
        return genericDAL.queryForObject(sql, penaltyDTORowMapper, id);
    }

    @Override
    public List<PenaltyDTO> findAll() {
        String sql = "SELECT * FROM penalty";
        return genericDAL.queryForList(sql,penaltyDTORowMapper);
    }

    @Override
    public Long create(PenaltyDTO p){
        String sql = "INSERT INTO penalty (id, penaltyDate, totalamount, status, payDate, employeeID) " +
                     " VALUES(?, ?, ?, ?, ?, ?)";
        return genericDAL.insert(sql, p.getId(), p.getPenaltyDate(), p.getTotalAmount(), p.getPayStatus().name() ,p.getPayDate(), p.getEmployeeID());
    }

    @Override
    public boolean update(PenaltyDTO penaltyDTO) {
        String sql = "UPDATE penalty SET " +
                    " penaltyDate = ?, totalamount = ?, status = ?, payDate = ?, employeeID = ? WHERE id = ?";
        return genericDAL.update(sql,penaltyDTO.getPenaltyDate(), penaltyDTO.getTotalAmount(), penaltyDTO.getPayStatus().name(),
                                penaltyDTO.getPayDate(), penaltyDTO.getEmployeeID(), penaltyDTO.getId() );
    }

    @Override
    public boolean delete(PenaltyDTO penaltyDTO) {
        String sql = "DELETE FROM penalty WHERE id = ? ";
        return genericDAL.delete(sql, penaltyDTO.getId());
    }

    public Long getCurrentID(){
        String sql = "SELECT MAX(CAST(SUBSTRING(id,3) AS UNSIGNED)) FROM penalty";
        return genericDAL.getMaxID(sql);
    }

    public List<String> getActiveYears(){
        List<String> years = new ArrayList<>();
        String sql = "SELECT DISTINCT YEAR(p.penaltyDate) AS year \n" +
                "FROM penalty AS p\n" +
                "ORDER BY YEAR DESC";

        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            try(ResultSet rs = pst.executeQuery()){
                while (rs.next()){
                    String year = rs.getString("year");
                    years.add(year);
                }
            }catch (Exception e){
                System.out.println("Lỗi khi lấy các năm của phiếu phạt");
                e.printStackTrace();
            }
        }catch (SQLException e){
            System.out.println("Lỗi khi lấy các năm của phiếu phạt");
            e.printStackTrace();
        }
        return years;
    }

    public String countPenaltySheet(){
        String sql = "SELECT COUNT(p.id) AS 'penaltySheet' \n" +
                "FROM penalty AS p \n";
        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            try(ResultSet rs = pst.executeQuery()){
                if(rs.next())
                    return rs.getString("penaltySheet");
            }catch (SQLException e){
                System.out.println("Có lỗi khi lấy sl phiếu phạt");
                e.printStackTrace();
            }
        }catch (SQLException e){
            System.out.println("Có lỗi khi lấy sl phiếu phạt");
            e.printStackTrace();
        }

        return null;
    }

    public Long sumPenaltyFee(){
        String sql =  "SELECT SUM(p.totalamount) AS 'totalFee'\n" +
                "FROM  penalty AS p\n";
        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        )
        {
            try(ResultSet rs = pst.executeQuery())
            {
                if(rs.next())
                    return rs.getLong("totalFee");
            }catch (SQLException e){
                System.out.println("Lỗi khi lấy tổng tiền phạt");
                e.printStackTrace();
            }

        }catch (SQLException e){
            System.out.println("Lỗi khi lấy tổng tiền phạt");
            e.printStackTrace();
        }
        return null;
    }

    public String countEmployeeHandle(){
        String sql = "SELECT COUNT(DISTINCT p.employeeID) AS employees \n" +
                "FROM penalty AS p";

        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            try(ResultSet rs = pst.executeQuery()) {
                if(rs.next())
                    return rs.getString("employees");
            } catch (Exception e) {
                System.out.println("Lỗi khi lấy số lượng nhân viên xử lý phiếu phạt");
                e.printStackTrace();
            }
        }catch (SQLException e){
            System.out.println("Lỗi khi lấy số lượng nhân viên xử lý phiếu phạt");
            e.printStackTrace();
        }
        return null;
    }

    public String countLostBook(){
        String sql = "SELECT SUM(pd.bookQuantity) AS lostBook \n" +
                "FROM penaltydetails AS pd \n" +
                "WHERE pd.`name` LIKE 'Hư sách' OR pd.`name` LIKE 'Mất sách' ";
        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            try(ResultSet rs = pst.executeQuery()){
                if(rs.next()){
                    return rs.getString("lostBook");
                }
            }catch (SQLException e){
                System.out.println("DAL : Có lỗi khi lấy sl sách tổn thất");
                e.printStackTrace();
            }
        }catch (SQLException e){
            System.out.println("DAL : Có lỗi khi lấy sl sách tổn thất");
            e.printStackTrace();
        }

        return null;
    }

    //Lấy từng dòng ' Mã NV - Tiền Q1 - Tiền Q2 - Tiền Q3 - Tiền Q4 - SL phiếu Q1 - SL phiếu Q2 ....' :
    public List<PenaltyPreciousData<Long>> getPreciousEachEmployee(String year){
        List<PenaltyPreciousData<Long>> employeeList = new ArrayList<>();
        String sql = "SELECT \n" +
                "    p.employeeID AS 'employeeID',\n" +
                "    SUM(CASE WHEN QUARTER(p.payDate) = 1 THEN p.totalAmount ELSE 0 END) AS sumQ1,\n" +
                "    SUM(CASE WHEN QUARTER(p.payDate) = 2 THEN p.totalAmount ELSE 0 END) AS sumQ2,\n" +
                "    SUM(CASE WHEN QUARTER(p.payDate) = 3 THEN p.totalAmount ELSE 0 END) AS sumQ3,\n" +
                "    SUM(CASE WHEN QUARTER(p.payDate) = 4 THEN p.totalAmount ELSE 0 END) AS sumQ4,\n" +
                "    COUNT(CASE WHEN QUARTER(p.payDate) = 1 THEN p.id ELSE NULL END) AS countQ1,\n" +
                "    COUNT(CASE WHEN QUARTER(p.payDate) = 2 THEN p.id ELSE NULL END) AS countQ2,\n" +
                "    COUNT(CASE WHEN QUARTER(p.payDate) = 3 THEN p.id ELSE NULL END) AS countQ3,\n" +
                "    COUNT(CASE WHEN QUARTER(p.payDate) = 4 THEN p.id ELSE NULL END) AS countQ4\n" +
                "FROM penalty AS p\n" +
                "JOIN employee AS e ON e.id = p.employeeID\n" +
                "WHERE p.payDate IS NOT NULL AND p.employeeID IS NOT NULL AND YEAR(p.payDate) = ? \n" +
                "GROUP BY employeeID;\n";


        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            pst.setString(1,year);
            try(ResultSet rs = pst.executeQuery()){
                while(rs.next()){
                    PenaltyPreciousData<Long> p = new PenaltyPreciousData<>(
                            rs.getLong("employeeID"),
                            rs.getLong("sumQ1"),
                            rs.getLong("sumQ2"),
                            rs.getLong("sumQ3"),
                            rs.getLong("sumQ4"),
                            rs.getLong("countQ1"),
                            rs.getLong("countQ2"),
                            rs.getLong("countQ3"),
                            rs.getLong("countQ4")
                    );
                    employeeList.add(p);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return employeeList;
    }


    public List<PenaltyPreciousData<String>> getPreciousEachReader(String year){
        List<PenaltyPreciousData<String>> readerList = new ArrayList<>();
        String sql = "SELECT \n" +
                "    readerId AS readerID,\n" +
                "    SUM(CASE WHEN QUARTER(penaltyDate) = 1 THEN totalAmount ELSE 0 END) AS sumQ1,\n" +
                "    SUM(CASE WHEN QUARTER(penaltyDate) = 2 THEN totalAmount ELSE 0 END) AS sumQ2,\n" +
                "    SUM(CASE WHEN QUARTER(penaltyDate) = 3 THEN totalAmount ELSE 0 END) AS sumQ3,\n" +
                "    SUM(CASE WHEN QUARTER(penaltyDate) = 4 THEN totalAmount ELSE 0 END) AS sumQ4,\n" +
                "    COUNT(CASE WHEN QUARTER(penaltyDate) = 1 THEN id END) AS countQ1,\n" +
                "    COUNT(CASE WHEN QUARTER(penaltyDate) = 2 THEN id END) AS countQ2,\n" +
                "    COUNT(CASE WHEN QUARTER(penaltyDate) = 3 THEN id END) AS countQ3,\n" +
                "    COUNT(CASE WHEN QUARTER(penaltyDate) = 4 THEN id END) AS countQ4\n" +
                "FROM (\n" +
                "    SELECT DISTINCT p.id, p.penaltyDate, p.totalAmount, r.id AS readerId\n" +
                "    FROM penaltydetails pd\n" +
                "    JOIN penalty p ON p.id = pd.penaltyId\n" +
                "    JOIN borrow_in_sheet b ON b.id = pd.borrowId\n" +
                "    JOIN reader r ON r.id = b.readerId\n" +
                ") AS sub\n" +
                "WHERE YEAR(penaltyDate) = ? " +
                "GROUP BY readerID;";

        try(Connection c= DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            pst.setString(1,year);
            try(ResultSet rs = pst.executeQuery()){
                while (rs.next()){
                    PenaltyPreciousData<String> p = new PenaltyPreciousData<>(
                            rs.getString("readerID"),
                            rs.getLong("sumQ1"),
                            rs.getLong("sumQ2"),
                            rs.getLong("sumQ3"),
                            rs.getLong("sumQ4"),
                            rs.getLong("countQ1"),
                            rs.getLong("countQ2"),
                            rs.getLong("countQ3"),
                            rs.getLong("countQ4")
                    );
                    readerList.add(p);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return readerList;
    }



    public List<LostBookPreciousData> getPreciousLostBook(String year){
        List<LostBookPreciousData> lostBookList = new ArrayList<>();
        String sql = "SELECT bookID AS bookID,\n" +
                "\t\t SUM(CASE WHEN QUARTER(penaltyDate) = 1 THEN quantity ELSE 0 END) AS countQ1,\n" +
                "\t\t SUM(CASE WHEN QUARTER(penaltyDate) = 2 THEN quantity ELSE 0 END) AS countQ2,\n" +
                "\t\t SUM(CASE WHEN QUARTER(penaltyDate) = 3 THEN quantity ELSE 0 END) AS countQ3,\n" +
                "\t\t SUM(CASE WHEN QUARTER(penaltyDate) = 4 THEN quantity ELSE 0 END) AS countQ4\n" +
                "FROM (\n" +
                "\tSELECT pd.bookId AS bookID,\n" +
                "\t\t\t\tpd.bookQuantity AS quantity,\n" +
                "\t\t\t\tp.penaltyDate AS penaltyDate\n" +
                "\tFROM penaltydetails AS pd\n" +
                "\tJOIN book AS b ON pd.bookId = b.id\n" +
                "\tJOIN penalty AS p ON p.id = pd.penaltyId\n" +
                "\tWHERE pd.`name` IN ('Hư sách', 'Mất sách')\n AND YEAR(penaltyDate) = ?" +
                ") AS sub \n" +
                "GROUP BY bookID\n";

        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            pst.setString(1,year);
            try(ResultSet rs = pst.executeQuery()){
                while (rs.next()){
                    LostBookPreciousData p = new LostBookPreciousData(
                            rs.getLong("bookID"),
                            rs.getLong("countQ1"),
                            rs.getLong("countQ2"),
                            rs.getLong("countQ3"),
                            rs.getLong("countQ4")
                    );

                    lostBookList.add(p);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return lostBookList;
    }

    //Thống kê theo từng tháng của 1 năm
    public List<PenaltyTimeData> getMonthsData(String year){
        List<PenaltyTimeData> monthsDataList = new ArrayList<>();
        String sql = "WITH \n" +
                "details AS ( \n" +
                "\t\tSELECT DISTINCT p.id AS penaltyID, \n" +
                "\t\tMONTH(p.penaltyDate) AS 'month', \n" +
                "\t\tb.readerId AS penaltyReader,\n" +
                "\t\tSUM(CASE WHEN pd.`name`  IN ('Hư sách', 'Mất sách') THEN pd.bookQuantity ELSE 0 END) AS lostBook,\n" +
                "\t\tp.totalamount AS totalAmount\n" +
                "FROM penalty AS p\n" +
                "\n" +
                "JOIN penaltydetails AS pd ON p.id = pd.penaltyId\n" +
                "JOIN borrow_in_sheet AS b ON pd.borrowId = b.id\n" +
                "WHERE YEAR(p.penaltyDate) = ? \n" +
                "GROUP BY p.id ),\n" +
                "\n" +
                "months AS (\n" +
                "\t\tSELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6\n" +
                "    \tUNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 )\n" +
                "\n" +
                "\n" +
                "SELECT \n" +
                "\tmonths.month,\n" +
                "\tCOUNT(d.penaltyID) AS sheetQuantity,\n" +
                "\tCOUNT(DISTINCT d.penaltyReader) AS readerQuantity,\n" +
                "\tIFNULL(SUM(d.lostBook),0) AS lostBooks,\n" +
                "\tIFNULL(SUM(d.totalAmount),0) AS penaltyFee\n" +
                "FROM months \n" +
                "LEFT JOIN details AS d ON months.month = d.month\n" +
                "GROUP BY months.month\n";

        try (Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            pst.setString(1,year);
            try(ResultSet rs = pst.executeQuery()){
                while(rs.next()){
                    PenaltyTimeData p = new PenaltyTimeData(
                        rs.getInt("month"),
                        rs.getInt("sheetQuantity"),
                            rs.getInt("readerQuantity"),
                            rs.getInt("lostBooks"),
                            rs.getLong("penaltyFee")
                    );
                    monthsDataList.add(p);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return monthsDataList;
    }

    public List<PenaltyTimeData> getYearsData(String startYear, String endYear){
        List<PenaltyTimeData> yearsDataList = new ArrayList<>();
        String sql = "WITH RECURSIVE years AS (\n" +
                "    SELECT ? AS year\n" +
                "    UNION ALL\n" +
                "    SELECT year + 1 FROM years WHERE year + 1 <= ?\n" +
                "),\n" +
                "details AS (\n" +
                "    SELECT DISTINCT \n" +
                "        p.id AS penaltyID,\n" +
                "        YEAR(p.penaltyDate) AS year,\n" +
                "        b.readerId AS penaltyReader,\n" +
                "        SUM(CASE WHEN pd.name IN ('Mất sách', 'Hư sách') THEN pd.bookQuantity ELSE 0 END) AS lostBooks,\n" +
                "        p.totalamount AS 'totalAmount' \n" +
                "    FROM penalty AS p\n" +
                "    JOIN penaltydetails AS pd ON p.id = pd.penaltyId\n" +
                "    JOIN borrow_in_sheet AS b ON b.id = pd.borrowId\n" +
                "    GROUP BY p.id\n" +
                ")\n" +
                "\n" +
                "SELECT \n" +
                "    years.year,\n" +
                "    IFNULL(COUNT(d.penaltyID), 0) AS sheetQuantity,\n" +
                "    IFNULL(COUNT(DISTINCT d.penaltyReader), 0) AS readerQuantity,\n" +
                "    IFNULL(SUM(d.lostBooks), 0) AS lostBooks,\n" +
                "    IFNULL(SUM(d.totalAmount), 0) AS totalAmount\n" +
                "FROM years \n" +
                "LEFT JOIN details AS d ON d.year = years.year\n" +
                "GROUP BY years.year\n" +
                "ORDER BY years.year;\n";

        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            pst.setString(1,startYear);
            pst.setString(2,endYear);
            try(ResultSet rs = pst.executeQuery()){
                while(rs.next()) {
                    PenaltyTimeData p = new PenaltyTimeData(
                            rs.getInt("year"),
                            rs.getInt("sheetQuantity"),
                            rs.getInt("readerQuantity"),
                            rs.getInt("lostBooks"),
                            rs.getLong("totalAmount")
                    );
                    yearsDataList.add(p);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return yearsDataList;
    }

    public List<PenaltyDateData> getDatesDate(String startDate, String endDate){
        List<PenaltyDateData> dateDataList = new ArrayList<>();
        String sql = "SELECT DISTINCT p.id,\n" +
                "\t\tp.penaltyDate,\n" +
                "\t\tb.readerId,\n" +
                "\t\tSUM(CASE WHEN pd.`name` IN ('Hư sách', 'Mất sách') THEN pd.bookQuantity ELSE 0 END) AS 'lostBooks',\n" +
                "\t\tp.totalamount\n" +
                "\n" +
                "FROM penalty AS p\n" +
                "JOIN penaltydetails AS pd ON p.id = pd.penaltyId\n" +
                "JOIN borrow_in_sheet AS b ON b.id = pd.borrowId\n" +
                "WHERE p.penaltyDate BETWEEN ? AND ? \n" +
                "GROUP BY p.id";

        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            pst.setString(1,startDate);
            pst.setString(2,endDate);
            try(ResultSet rs = pst.executeQuery()){
                while (rs.next()){
                    PenaltyDateData p = new PenaltyDateData(
                            rs.getString("id"),
                            rs.getDate("penaltyDate"),
                            rs.getString("readerID"),
                            rs.getInt("lostBooks"),
                            rs.getLong("totalamount")
                    );
                    dateDataList.add(p);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return dateDataList;
    }

}