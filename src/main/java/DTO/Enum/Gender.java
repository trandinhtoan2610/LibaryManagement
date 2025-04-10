package DTO.Enum;

public enum Gender {
    Nam("Nam"),
    Nữ("Nữ");
    private final String displayName;
    Gender(String displayName) {
        this.displayName = displayName;
    }
    public String toString() {
        return displayName;
    }
}
