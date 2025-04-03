package BUS;

import DAL.SupplierDAL;
import DTO.SupplierDTO;

import java.util.ArrayList;
import java.util.List;

public class SupplierBUS {
    SupplierDAL supplierDAL;
    public static List<SupplierDTO> supplierList;

    public SupplierBUS() {
        supplierDAL = new SupplierDAL();
        supplierList = new ArrayList<>();
        if (supplierList.size() == 0) {
            getSupplierList();
        }
    }

    public void getSupplierList() {
        supplierList = supplierDAL.findAll();
    }

    public void addSupplier(SupplierDTO supplier) {
        supplierDAL.create(supplier);
        supplierList.add(supplier);
    }

    public void deleteSupplier(SupplierDTO supplier) {
        boolean success = supplierDAL.delete(supplier.getId()); // Xóa khỏi DB
        if (success) {
            supplierList.removeIf(s -> s.getId().equals(supplier.getId())); // Xóa khỏi danh sách
        }
    }

    public void updateSupplier(SupplierDTO supplier) {
        boolean success = supplierDAL.update(supplier);
        if (success) {
            supplierList.replaceAll(s -> s.getId().equals(supplier.getId()) ? supplier : s);
        }
    }

    public SupplierDTO findSupplierByID(SupplierDTO supplier) {
        return supplierDAL.findById(supplier.getId());
    }

    public long getCurrentID() {
        return supplierDAL.getCurrentID();
    }
}