
package GUI.Controller;


public class SearchCondition {
    public enum Field {EMPLOYEE, READER, AMOUNT, STATUS, PAY_DATE, PENALTY_DATE};
    private Field searchField;
    private Object searchValue;
    private boolean isAnd;

    public SearchCondition(Field searchField, Object searchValue, boolean isAnd) {
        this.searchField = searchField;
        this.searchValue = searchValue;
        this.isAnd = isAnd;
    }

    public Field getSearchField() {
        return searchField;
    }

    public Object getSearchValue() {
        return searchValue;
    }

    public boolean isAnd() {
        return isAnd;
    }

    public String debugString() {
        return String.format("[Field: %s | Value: %s | Type: %s | isAnd: %s]",
                searchField, searchValue, searchValue != null ? searchValue.getClass().getSimpleName() : "null", isAnd);
    }

}
