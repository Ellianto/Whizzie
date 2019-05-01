package id.ac.umn.whizzie.Home;

public class CategoryCard {
    private String categoryName;
    private int imageID;

    public CategoryCard() {

    }

    public CategoryCard(int imageID, String categoryName) {
        this.categoryName = categoryName;
        this.imageID = imageID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {this.categoryName = categoryName;}

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {this.imageID = imageID;}
}
