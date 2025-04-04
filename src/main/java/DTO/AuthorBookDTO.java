package DTO;

import java.time.Year;


public class AuthorBookDTO {
    private Long iD;
    private String name;
    private String category;
    private String authorName;
    private String publisherName;
    private int quantity;
    private Long unitPrice;
    private Year yearOfPublication;
    public AuthorBookDTO(Long iD, String name, String category, String authorName, String publisherName, int quantity, Long unitPrice, Year yearOfPublication) {
        this.iD = iD;
        this.name = name;
        this.category = category;
        this.authorName = authorName;
        this.publisherName = publisherName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.yearOfPublication = yearOfPublication;
    }

    public Long getiD() {
        return iD;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public Year getYearOfPublication() {
        return yearOfPublication;
    }

    public void setiD(Long iD) {
        this.iD = iD;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setYearOfPublication(Year yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }
    
    
}
