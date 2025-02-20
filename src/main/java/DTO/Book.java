package DTO;
import DTO.Abstract.EntityAuditNameBase;

import java.time.Year;

public class Book extends EntityAuditNameBase<Long> {
    private Long categoryId, authorId, bookshelfId, publisherId;
    private int quantity;
    private Year yearOfPublication;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getBookshelfId() {
        return bookshelfId;
    }

    public void setBookshelfId(Long bookshelfId) {
        this.bookshelfId = bookshelfId;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Year getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(Year yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }
}
