package DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.Year;

@Getter
@Setter
public class BookViewModel {
    private Long id;
    private String name;
    private String categoryName;
    private String authorName;
    private String publisherName;
    private int quantity;
    private Long unitPrice;
    private Year yearOfPublication;


    public BookViewModel(){};

    public BookViewModel(Long id, String name, String categoryName, String authorName, String publisherName, int quantity, Long unitPrice, Year yearOfPublication) {
        this.id = id;
        this.name = name;
        this.categoryName = categoryName;
        this.authorName = authorName;
        this.publisherName = publisherName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.yearOfPublication = yearOfPublication;
    }
}
