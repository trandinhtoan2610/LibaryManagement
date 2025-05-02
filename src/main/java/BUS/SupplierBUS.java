package BUS;

import DAL.SupplierDAL;
import DTO.SupplierDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

public class SupplierBUS {
    private final SupplierDAL supplierDAL = new SupplierDAL();
    public static List<SupplierDTO> supplierList = new ArrayList<>();

    public SupplierBUS() {
        if (supplierList.isEmpty()) {
            supplierList = supplierDAL.findAll();
        }
    }
    
    public List<SupplierDTO> getAllSuppliers() {
        return supplierList;
    }
    public boolean addSupplier(SupplierDTO supplier) {
        if (true){
            supplierDAL.create(supplier);
            supplierList.add(supplier);
            return true;
        } else {
            return false;
        }
    }
    
    

    public boolean deleteSupplier(SupplierDTO supplier){
        boolean success = supplierDAL.delete(supplier.getId());
        if (success) {
            supplierList.removeIf(s -> s.getId().equals(supplier.getId()));
            return true; 
        } else {
            return false; 
        }
    }

    public boolean updateSupplier(SupplierDTO supplierDTO, String oldId) {
        boolean success = supplierDAL.update(supplierDTO, oldId); 
        if (success) {
            supplierList.replaceAll(s -> s.getId().equals(oldId) ? supplierDTO : s);
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
    public SupplierDTO getSupplierById(String supplierId) {
        if (supplierId == null) {
            throw new IllegalArgumentException("ID nhà cung cấp không được null!");
        }
    
        for (SupplierDTO supplier : supplierList) {
            if (supplier.getId().equals(supplierId)) {
                return supplier;
            }
        }
    
        return null; // không tìm thấy
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