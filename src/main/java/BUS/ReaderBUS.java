package BUS;

import DAL.ReaderDAL;
import DTO.ReaderDTO;

import java.util.ArrayList;
import java.util.List;

public class ReaderBUS {
    ReaderDAL readerDAL;
    public static List<ReaderDTO> readerList;

    public ReaderBUS(){
        readerDAL = new ReaderDAL();
        readerList = new ArrayList<>();
        if(readerList.size() == 0)
            readerList = getReaderList();

    }

    public List<ReaderDTO> getReaderList(){
        List<ReaderDTO> list = readerDAL.findAll();
        return list;
    }

    public void addReader(ReaderDTO reader){
        readerDAL.create(reader);
        readerList.add(reader);
    }

    public void deleteReader(ReaderDTO reader) {
        boolean success = readerDAL.delete(reader.getId()); // Xóa khỏi DB
        if (success) {
            readerList.removeIf(r -> r.getId().equals(reader.getId())); // Xóa khỏi danh sách 
        }
    }

    public void updateReader(ReaderDTO reader){
        boolean success = readerDAL.update(reader);
        if (success){
            readerList.replaceAll(r -> r.getId()==reader.getId() ? reader : r);
        }
    }

    public ReaderDTO findReaderByID(ReaderDTO reader){
        return readerDAL.findById(reader.getId());
    }
    
    public long getCurrentID(){
        return readerDAL.getCurrentID();
    }
}