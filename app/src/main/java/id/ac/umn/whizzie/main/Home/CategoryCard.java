package id.ac.umn.whizzie.main.Home;

public class CategoryCard {
    private String categoryName;
    private String imageID;

    public CategoryCard() {

    }

    public CategoryCard(String imageID, String categoryName) {
        this.categoryName = categoryName;
        this.imageID = imageID;
    }

    public String getCategoryName() {
        return categoryName;
    }


    public void setCategoryName(String categoryName) {this.categoryName = categoryName;}

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {this.imageID = imageID;}
}
