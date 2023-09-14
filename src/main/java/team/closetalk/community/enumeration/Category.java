package team.closetalk.community.enumeration;

public enum Category {
    // 임시 카테고리
    ALL("ALL"),
    SMALLTALK("SMALLTALK"),
    CLOSETALK("CLOSETALK");

    private final String category;

    Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}

