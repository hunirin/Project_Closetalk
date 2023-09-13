package team.closetalk.issue.enumeration;

public enum Category {
    ALL("ALL"),
    ISSUE("ISSUE"),
    MAGAZINE("MAGAZINE");

    private final String category;
    Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
