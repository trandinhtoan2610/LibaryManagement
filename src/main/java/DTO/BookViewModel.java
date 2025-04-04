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
}
