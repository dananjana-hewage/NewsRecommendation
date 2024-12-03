package models;

public class Preferences {
    private int userId;
    private int categoryId;

    // Constructor
    public void Preference(int userId, int categoryId) {
        this.userId = userId;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

}
