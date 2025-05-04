package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.Statistics.PurchaseDateData;
import DTO.Statistics.PurchaseTimeData;
import DTO.Statistics.StatisticsPreciousData;

public class PurchaseStatisticsDAL {

    public PurchaseStatisticsDAL(){}

    public Long countPurchaseSheet(){
        Long countPurchase = 0L;
        String sql = "SELECT IFNULL(COUNT(p.id),0) AS 'purchaseSheet'\n" +
                "FROM purchaseorders AS p";
        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            try(ResultSet rs = pst.executeQuery()){
                if(rs.next()){
                    countPurchase += rs.getLong("purchaseSheet");
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return countPurchase;
    }

    public Long sumPurchaseFee(){
        Long purchaseFee = 0L;
        String sql = "SELECT IFNULL(SUM(p.totalAmount),0) AS 'purchaseFee'\n" +
                "FROM purchaseorders AS p" +
                "\n WHERE p.`status` = 'Hoàn_Thành'";
        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            try(ResultSet rs = pst.executeQuery()){
                if(rs.next()){
                    purchaseFee += rs.getLong("purchaseFee");
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return purchaseFee;
    }

    public Long sumBookPurchase(){
        Long countBook = 0L;
        String sql = "SELECT SUM(bookQuantity) AS bookQuantity\n" +
                "FROM (\n" +
                "    SELECT p.id, SUM(pd.quantity) AS bookQuantity\n" +
                "    FROM purchaseorders AS p\n" +
                "    JOIN purchaseorderdetails AS pd ON p.id = pd.purchaseOrderId\n" +
                "    WHERE p.`status` = 'Hoàn_Thành'\n" +
                "    GROUP BY p.id\n" +
                ") AS purchase_summary;\n";

        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            try(ResultSet rs = pst.executeQuery()){
                if(rs.next()){
                    countBook += rs.getLong("bookQuantity");
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return countBook;
    }

    public Long countSupplier(){
        Long countSupplier = 0L;
        String sql = "SELECT IFNULL(COUNT(s.id),0) AS 'countSupplier'\n" +
                "FROM supplier AS s\n" +
                "WHERE s.isActive = 1";
        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            try(ResultSet rs = pst.executeQuery()){
                if(rs.next()){
                    countSupplier += rs.getLong("countSupplier");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return countSupplier;
    }

    public List<StatisticsPreciousData<Long>> getEmployeePreciousData(String year){
        List<StatisticsPreciousData<Long>> employeeList = new ArrayList<>();
        String sql = "SELECT \n" +
                "    e.id AS employeeID,\n" +
                "\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 1 THEN p.totalAmount ELSE 0 END) AS totalQ1,\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 2 THEN p.totalAmount ELSE 0 END) AS totalQ2,\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 3 THEN p.totalAmount ELSE 0 END) AS totalQ3,\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 4 THEN p.totalAmount ELSE 0 END) AS totalQ4,\n" +
                "\n" +
                "    COUNT(CASE WHEN QUARTER(p.buyDate) = 1 THEN p.id ELSE NULL END) AS countQ1,\n" +
                "    COUNT(CASE WHEN QUARTER(p.buyDate) = 2 THEN p.id ELSE NULL END) AS countQ2,\n" +
                "    COUNT(CASE WHEN QUARTER(p.buyDate) = 3 THEN p.id ELSE NULL END) AS countQ3,\n" +
                "    COUNT(CASE WHEN QUARTER(p.buyDate) = 4 THEN p.id ELSE NULL END) AS countQ4\n" +
                "\n" +
                "FROM employee AS e\n" +
                "LEFT JOIN purchaseorders AS p \n" +
                "    ON p.employeeId = e.id \n" +
                "    AND p.status = 'Hoàn_Thành' \n" +
                "    AND YEAR(p.buyDate) = ? \n" +
                "\n" +
                "GROUP BY e.id;\n";

        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            pst.setString(1,year);
            try(ResultSet rs = pst.executeQuery()){
                while(rs.next()){
                    StatisticsPreciousData<Long> p = new StatisticsPreciousData<>(
                            rs.getLong("employeeID"),
                            rs.getLong("totalQ1"),
                            rs.getLong("totalQ2"),
                            rs.getLong("totalQ3"),
                            rs.getLong("totalQ4"),
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

    public List<StatisticsPreciousData<String>> getSupplierPreciousData(String year){
        List<StatisticsPreciousData<String>> supplierList = new ArrayList<>();
        String sql = "SELECT \n" +
                "    s.id AS 'supplierID',\n" +
                "\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 1 THEN p.totalAmount ELSE 0 END) AS totalQ1,\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 2 THEN p.totalAmount ELSE 0 END) AS totalQ2,\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 3 THEN p.totalAmount ELSE 0 END) AS totalQ3,\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 4 THEN p.totalAmount ELSE 0 END) AS totalQ4,\n" +
                "\n" +
                "    COUNT(CASE WHEN QUARTER(p.buyDate) = 1 THEN p.id ELSE NULL END) AS countQ1,\n" +
                "    COUNT(CASE WHEN QUARTER(p.buyDate) = 2 THEN p.id ELSE NULL END) AS countQ2,\n" +
                "    COUNT(CASE WHEN QUARTER(p.buyDate) = 3 THEN p.id ELSE NULL END) AS countQ3,\n" +
                "    COUNT(CASE WHEN QUARTER(p.buyDate) = 4 THEN p.id ELSE NULL END) AS countQ4\n" +
                "\n" +
                "FROM supplier AS s\n" +
                "LEFT JOIN purchaseorders AS p \n" +
                "    ON p.supplierId = s.id \n" +
                "    AND p.status = 'Hoàn_Thành' \n" +
                "    AND YEAR(p.buyDate) = ? \n" +
                "\n" +
                "GROUP BY s.id;\n";

        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql)
        ){
            pst.setString(1,year);
            try(ResultSet rs = pst.executeQuery()){
                while (rs.next()){
                    StatisticsPreciousData<String> p = new StatisticsPreciousData<>(
                            rs.getString("supplierID"),
                            rs.getLong("totalQ1"),
                            rs.getLong("totalQ2"),
                            rs.getLong("totalQ3"),
                            rs.getLong("totalQ4"),
                            rs.getLong("countQ1"),
                            rs.getLong("countQ2"),
                            rs.getLong("countQ3"),
                            rs.getLong("countQ4")
                    );
                    supplierList.add(p);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return supplierList;
    }

    public List<StatisticsPreciousData<Long>> getBookPreciousData(String year){
        List<StatisticsPreciousData<Long>> bookList = new ArrayList<>();
        String sql = "SELECT \n" +
                "    b.id AS bookID,\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 1 THEN pd.SubTotal ELSE 0 END) AS totalQ1,\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 2 THEN pd.SubTotal ELSE 0 END) AS totalQ2,\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 3 THEN pd.SubTotal ELSE 0 END) AS totalQ3,\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 4 THEN pd.SubTotal ELSE 0 END) AS totalQ4,\n" +
                "\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 1 THEN pd.quantity ELSE 0 END) AS countQ1,\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 2 THEN pd.quantity ELSE 0 END) AS countQ2,\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 3 THEN pd.quantity ELSE 0 END) AS countQ3,\n" +
                "    SUM(CASE WHEN QUARTER(p.buyDate) = 4 THEN pd.quantity ELSE 0 END) AS countQ4\n" +
                "\n" +
                "FROM book AS b\n" +
                "LEFT JOIN purchaseorderdetails AS pd ON pd.bookId = b.id\n" +
                "LEFT JOIN purchaseorders AS p \n" +
                "    ON p.id = pd.purchaseOrderId \n" +
                "    AND p.status = 'Hoàn_Thành' \n" +
                "    AND YEAR(p.buyDate) = ? \n" +
                "\n" +
                "GROUP BY b.id;\n";

        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql)
        ){
            pst.setString(1,year);
            try(ResultSet rs = pst.executeQuery()){
                while (rs.next()){
                    StatisticsPreciousData<Long> p = new StatisticsPreciousData<>(
                            rs.getLong("bookID"),
                            rs.getLong("totalQ1"),
                            rs.getLong("totalQ2"),
                            rs.getLong("totalQ3"),
                            rs.getLong("totalQ4"),
                            rs.getLong("countQ1"),
                            rs.getLong("countQ2"),
                            rs.getLong("countQ3"),
                            rs.getLong("countQ4")
                    );
                    bookList.add(p);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return bookList;
    }

    public List<PurchaseTimeData> getPurchaseMonthData(String year){
        List<PurchaseTimeData> monthDataList = new ArrayList<>();
        String sql = "WITH months AS (\n" +
                "\tSELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 \n" +
                "\tUNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12\n" +
                "),\n" +
                "\n" +
                "sheet_and_fee AS (\n" +
                "\tSELECT \n" +
                "\t\tMONTH(p.buyDate) AS 'buyMonth',\n" +
                "\t\tCOUNT(CASE WHEN p.`status`='Hoàn_Thành' THEN p.id ELSE NULL END) AS 'doneSheet',\n" +
                "\t\tSUM(CASE WHEN p.`status`='Hoàn_Thành' THEN p.totalAmount ELSE 0 END) AS 'totalFee',\n" +
                "\t\tCOUNT(CASE WHEN p.`status`='Đã_Hủy' THEN p.id ELSE NULL END) AS 'cancelSheet'\n" +
                "\tFROM purchaseorders AS p\n" +
                "\tWHERE YEAR(p.buyDate) = ? \n" +
                "\tGROUP BY MONTH(p.buyDate)\n" +
                "),\n" +
                "\n" +
                "book_purchase AS (\n" +
                "\tSELECT\n" +
                "\t\tMONTH(p.buyDate) AS 'buyMonth',\n" +
                "\t\tSUM(CASE WHEN p.`status`='Hoàn_Thành' THEN pd.quantity ELSE NULL END) AS 'bookQuantity'\n" +
                "\tFROM purchaseorders AS p\n" +
                "\tJOIN purchaseorderdetails AS pd ON pd.purchaseOrderId = p.id\n" +
                "\tWHERE YEAR(p.buyDate) = ? \n" +
                "\tGROUP BY buyMonth\n" +
                ")\n" +
                "\n" +
                "SELECT \n" +
                "\tmonths.month AS 'month',\n" +
                "\tIFNULL(s.totalFee,0) AS 'purchaseFee',\n" +
                "\tIFNULL(b.bookQuantity,0) AS 'bookQuantity',\n" +
                "\tIFNULL(s.doneSheet,0) AS 'doneSheet',\n" +
                "\tIFNULL(s.cancelSheet,0) AS 'cancelSheet'\n" +
                "\t\n" +
                "FROM months \n" +
                "LEFT JOIN sheet_and_fee AS s ON s.buyMonth = months.month \n" +
                "LEFT JOIN book_purchase AS b ON b.buyMonth = months.month\n" +
                "GROUP BY months.month\n";

        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql);
        ){
            pst.setString(1,year);
            pst.setString(2,year);
            try(ResultSet rs = pst.executeQuery()){
                while (rs.next()){
                    PurchaseTimeData p = new PurchaseTimeData(
                            rs.getInt("month"),
                            rs.getLong("purchaseFee"),
                            rs.getInt("bookQuantity"),
                            rs.getInt("doneSheet"),
                            rs.getInt("cancelSheet")
                    );

                    monthDataList.add(p);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return monthDataList;
    }

    public List<PurchaseTimeData> getPurchaseYearData(String beginYear, String endYear){
        List<PurchaseTimeData> yearDataList = new ArrayList<>();
        String sql = "WITH RECURSIVE year_list AS(\n" +
                "\tSELECT ? AS 'year'\n" +
                "\tUNION ALL\n" +
                "\tSELECT year+1 FROM year_list WHERE year+1 <= ?\n" +
                "),\n" +
                "\n" +
                "sheet_and_fee AS (\n" +
                "\tSELECT \n" +
                "\t\t\tYEAR(p.buyDate) AS 'year',\n" +
                "\t\t\tSUM(CASE WHEN p.`status` = 'Hoàn_thành' THEN p.totalAmount ELSE 0 END) AS 'purchaseFee',\n" +
                "\t\t\tCOUNT(CASE WHEN p.`status` = 'Hoàn_thành' THEN p.id ELSE NULL END) AS 'doneSheet',\n" +
                "\t\t\tCOUNT(CASE WHEN p.`status` = 'Đã_Hủy' THEN p.id ELSE NULL END) AS 'cancelSheet'\n" +
                "\tFROM purchaseorders AS p\n" +
                "\tGROUP BY YEAR(p.buyDate)\n" +
                "),\n" +
                "\n" +
                "book_purchase AS (\n" +
                "\tSELECT \n" +
                "\t\t\tYEAR(p.buyDate) AS 'year',\n" +
                "\t\t\tSUM(CASE WHEN p.`status` = 'Hoàn_Thành' THEN pd.quantity ELSE 0 END) AS 'bookQuantity'\n" +
                "\tFROM purchaseorders AS p\n" +
                "\tJOIN purchaseorderdetails AS pd ON pd.purchaseOrderId = p.id\n" +
                "\tGROUP BY YEAR(p.buyDate)\n" +
                ")\n" +
                "\n" +
                "\n" +
                "\n" +
                "SELECT \n" +
                "\t\tye.year AS 'year',\n" +
                "\t\tIFNULL(sf.purchaseFee,0) AS 'purchaseFee',\n" +
                "\t\tIFNULL(bp.bookQuantity,0) AS 'bookQuantity',\n" +
                "\t\tIFNULL(sf.doneSheet,0) AS 'doneSheet',\n" +
                "\t\tIFNULL(sf.cancelSheet,0) AS 'cancelSheet'\n" +
                "\t\t\n" +
                "FROM year_list AS ye\n" +
                "LEFT JOIN sheet_and_fee AS sf ON sf.year = ye.year\n" +
                "LEFT JOIN book_purchase AS bp ON bp.year = ye.year";

        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql)
        ){
            pst.setString(1,beginYear);
            pst.setString(2,endYear);
            try(ResultSet rs = pst.executeQuery()){
                while (rs.next()){
                    PurchaseTimeData p = new PurchaseTimeData(
                            rs.getInt("year"),
                            rs.getLong("purchaseFee"),
                            rs.getInt("bookQuantity"),
                            rs.getInt("doneSheet"),
                            rs.getInt("cancelSheet")
                    );
                    yearDataList.add(p);
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return yearDataList;
    }

    public List<PurchaseDateData> getPurchaseDateData(String beginDate, String endDate){
        List<PurchaseDateData> dateDataList = new ArrayList<>();
        String sql = "SELECT \n" +
                "    p.id,\n" +
                "    MAX(p.buyDate) AS buyDate,\n" +
                "    MAX(p.supplierId) AS supplierId,\n" +
                "    MAX(p.totalAmount) AS totalAmount,\n" +
                "    SUM(pd.quantity) AS totalQuantity\n" +
                "FROM purchaseorders AS p\n" +
                "JOIN purchaseorderdetails AS pd ON pd.purchaseOrderId = p.id\n" +
                "WHERE p.status = 'Hoàn_Thành' \n" +
                "  AND p.buyDate BETWEEN ? AND ? \n" +
                "GROUP BY p.id\n" +
                "ORDER BY p.buyDate ASC;\n";

        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql)
        ){
            pst.setString(1,beginDate);
            pst.setString(2,endDate);
            try(ResultSet rs = pst.executeQuery()){
                while (rs.next()){
                    PurchaseDateData p = new PurchaseDateData(
                            rs.getLong("id"),
                            rs.getDate("buyDate"),
                            rs.getString("supplierID"),
                            rs.getLong("totalAmount"),
                            rs.getLong("totalQuantity")
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

    public List<String> getActiveYears(){
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT YEAR(p.buyDate) AS years\n" +
                "FROM purchaseorders AS p\n" +
                "WHERE p.`status`='Hoàn_Thành'";

        try(Connection c = DatabaseConnection.getConnection();
            PreparedStatement pst = c.prepareStatement(sql)
        ){
            try(ResultSet rs = pst.executeQuery()){
                while (rs.next()){
                    list.add(rs.getString("years"));
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return list;
    }



}
