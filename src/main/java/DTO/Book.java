package DTO;

import BUS.AuthorBUS;
import DTO.Abstract.EntityBase;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;

@Getter
@Setter
public class Book extends EntityBase<Long> {
    private String name;
    private Long categoryId, authorId, publisherId;
    private int quantity;
    private Long unitPrice;
    private Year yearOfPublication;

    private PublisherDTO publisherDTO;
    private Category category;
    private AuthorDTO author;

    public AuthorDTO getAuthor() {
        for(DTO.AuthorDTO a : AuthorBUS.authorDTOList){
            if(a.getId() == this.authorId)
                return a;
        }
        return null;
    }
}