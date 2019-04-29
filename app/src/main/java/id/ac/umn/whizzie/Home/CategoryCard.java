package id.ac.umn.whizzie.Home;

public class CategoryCard {
    private String categoryName;

    public CategoryCard() {
    }

    public CategoryCard(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
