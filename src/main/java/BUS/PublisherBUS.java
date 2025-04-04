package BUS;


import DAL.PublisherDAL;
import DTO.PublisherDTO;

import java.util.List;

public class PublisherBUS {
    private final PublisherDAL publisherDAL = new PublisherDAL();
    public List<PublisherDTO> getAllPublishers() {
        return publisherDAL.findAll();
    }
    public long addPublisher(PublisherDTO publisherDTO) {
        return publisherDAL.create(publisherDTO);
    }
    public boolean updatePublisher(PublisherDTO publisherDTO) {
        try {
            return publisherDAL.update(publisherDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deletePublisher(long id) {
        try {
            return publisherDAL.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public long getCurrentID(){
        return publisherDAL.getCurrentID();
    }
}
