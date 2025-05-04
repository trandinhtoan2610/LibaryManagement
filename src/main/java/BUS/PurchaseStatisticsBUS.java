package BUS;

import DAL.PurchaseStatisticsDAL;
import DTO.Statistics.PurchaseDateData;
import DTO.Statistics.PurchaseTimeData;
import DTO.Statistics.StatisticsPreciousData;

import java.util.List;

public class PurchaseStatisticsBUS {
    private PurchaseStatisticsDAL purchaseStatisticsDAL;

    public PurchaseStatisticsBUS() {
        purchaseStatisticsDAL = new PurchaseStatisticsDAL();
    }

    public String countSheet() {
        String count = "";
        try {
            count = purchaseStatisticsDAL.countPurchaseSheet().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public String countBook() {
        String count = "";
        try {
            count = purchaseStatisticsDAL.sumBookPurchase().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public Long sumPurchaseFee() {
        try {
            return purchaseStatisticsDAL.sumPurchaseFee();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String countSupplier() {
        String count = "";
        try {
            count = purchaseStatisticsDAL.countSupplier().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    public List<StatisticsPreciousData<Long>> getEmployeePreciousData(String year) {
        try {
            return purchaseStatisticsDAL.getEmployeePreciousData(year);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<StatisticsPreciousData<String>> getSupplierPreciousData(String year) {
        try {
            return purchaseStatisticsDAL.getSupplierPreciousData(year);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<StatisticsPreciousData<Long>> getBookPreciousData(String year) {
        try {
            return purchaseStatisticsDAL.getBookPreciousData(year);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PurchaseTimeData> getPurchaseMonthData(String year) {
        try {
            return purchaseStatisticsDAL.getPurchaseMonthData(year);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PurchaseTimeData> getPurchaseYearData(String beginYear, String endYear) {
        try {
            return purchaseStatisticsDAL.getPurchaseYearData(beginYear, endYear);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PurchaseDateData> getPurchaseDateData(String beginDate, String endDate) {
        try {
            return purchaseStatisticsDAL.getPurchaseDateData(beginDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getActiveYears() {
        try {
            return purchaseStatisticsDAL.getActiveYears();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}



