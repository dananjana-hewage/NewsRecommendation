package models;

public class Article {
    private int id;
    private String title;
    private String imageUrl;

    // No-argument constructor
    public Article() {}

    public Article(int id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public Article(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

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
}
