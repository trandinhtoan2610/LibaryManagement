package BUS;

import DAL.SupplierDAL;
import DTO.SupplierDTO;

import java.util.ArrayList;
import java.util.List;

public class SupplierBUS {
    SupplierDAL supplierDAL;
    public List<SupplierDTO> supplierList; 

    public SupplierBUS() {
        supplierDAL = new SupplierDAL();
        supplierList = new ArrayList<>();
        loadSupplierList();
    }

    private void loadSupplierList() {
        List<SupplierDTO> loadedList = supplierDAL.findAll();
        if (loadedList != null) {
            supplierList.addAll(loadedList);
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
        boolean success = supplierDAL.delete(supplier.getId());
        if (success) {
            supplierList.removeIf(s -> s.getId().equals(supplier.getId()));
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