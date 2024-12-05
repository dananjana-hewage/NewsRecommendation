package models;

public class Article {

    private String title;
    private int categoryId;

    // Constructor
    public Article(int articleId, String title) {

        this.title = title;
        this.categoryId = categoryId;
    }

    // Getters and Setters


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

}
