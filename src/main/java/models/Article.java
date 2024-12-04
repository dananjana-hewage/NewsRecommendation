package models;

public class Article {
    private int id; // Auto-increment ID from the database
    private String title;
    private String imageUrl;
    private String category;

    // Default constructor
    public Article() {
    }

    // Constructor without ID (used when creating a new article to save in the database)
    public Article(String title, String imageUrl, String category) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    // Constructor with ID (used when retrieving an article from the database)
    public Article(int id, String title, String imageUrl, String category) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
