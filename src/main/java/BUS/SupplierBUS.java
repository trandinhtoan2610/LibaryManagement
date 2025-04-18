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
        if (supplierList.stream().anyMatch(s -> s.getId().equals(supplier.getId()))) {
            JOptionPane.showMessageDialog(null, "ID nhà cung cấp đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false; 
        }if (true) {
            supplierDAL.create(supplier);
            supplierList.add(supplier);
            return true;
        } else {
            return false;
        }
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