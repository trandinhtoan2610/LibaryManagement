package BUS;

import DAL.SupplierDAL;
import DTO.SupplierDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<SupplierDTO> getAllSuppliers() {
        return supplierList;
    }

    public void addSupplier(SupplierDTO supplier) {
        supplierDAL.create(supplier);
        supplierList.add(supplier);
    }

    public boolean deleteSupplier(SupplierDTO supplier) {
        boolean success = supplierDAL.delete(supplier.getId());
        if (success) {
            supplierList.removeIf(s -> s.getId().equals(supplier.getId()));
            return true; 
        } else {
            return false; 
        }
    }

    public boolean updateSupplier(SupplierDTO supplier) {
        boolean success = supplierDAL.update(supplier); 
        if (success) {
            supplierList.replaceAll(s -> s.getId().equals(supplier.getId()) ? supplier : s);
        }
        return success; 
    }

    public SupplierDTO findSupplierByID(SupplierDTO supplier) {
        return supplierDAL.findById(supplier.getId());
    }

    public long getCurrentID() {
        return supplierDAL.getCurrentID();
    }
    
    public void reloadSupplierList() {
        supplierList.clear();
        List<SupplierDTO> loadedList = supplierDAL.findAll();
        if (loadedList != null) {
            supplierList.addAll(loadedList);
        }
    }

    
    // public List<SupplierDTO> searchSuppliersByName(String name) {
    //     if (name == null || name.trim().isEmpty()) {
    //         return supplierList;
    //     }
    //     String lowerCaseName = name.trim().toLowerCase();
    //     return supplierList.stream()
    //             .filter(supplier -> supplier.getName().toLowerCase().contains(lowerCaseName))
    //             .collect(Collectors.toList());
    // }

   
    public List<SupplierDTO> searchSuppliersByAll(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return supplierList;
        }
        String lowerCaseKeyword = keyword.trim().toLowerCase();
        return supplierList.stream()
                .filter(supplier ->
                        supplier.getName().toLowerCase().contains(lowerCaseKeyword) ||
                        supplier.getPhone().toLowerCase().contains(lowerCaseKeyword) ||
                        supplier.getAddress().toLowerCase().contains(lowerCaseKeyword)
                )
                .collect(Collectors.toList());
    }
}