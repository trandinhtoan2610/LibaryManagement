package DTO;

import DTO.Abstract.EntityBase;

public class BorrowDetail extends EntityBase<Long> {
    private Long bookId, brorrowId;
    private int quantity;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getBrorrowId() {
        return brorrowId;
    }

    public void setBrorrowId(Long brorrowId) {
        this.brorrowId = brorrowId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
