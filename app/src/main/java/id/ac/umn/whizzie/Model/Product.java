package id.ac.umn.whizzie.Model;

public class Product {
    private String category;
    private String descProduct;
    private int massProduct;
    private String nameProduct;
    private String pictureProduct;
    private String timeProduct;
    private String uidUpProduct;
    private int wishesCount;

    public Product() {
    }

    // Constructor tidak termasuk wishesCount karena wishesCount akan di set manual melalui Query terpisah
    public Product(String category, String descProduct, int massProduct, String nameProduct, String pictureProduct, String timeProduct, String uidUpProduct) {
        this.category = category;
        this.descProduct = descProduct;
        this.massProduct = massProduct;
        this.nameProduct = nameProduct;
        this.pictureProduct = pictureProduct;
        this.timeProduct = timeProduct;
        this.uidUpProduct = uidUpProduct;
        this.wishesCount = 0;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescProduct() {
        return descProduct;
    }

    public void setDescProduct(String descProduct) {
        this.descProduct = descProduct;
    }

    public int getMassProduct() {
        return massProduct;
    }

    public void setMassProduct(int massProduct) {
        this.massProduct = massProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getPictureProduct() {
        return pictureProduct;
    }

    public void setPictureProduct(String pictureProduct) {
        this.pictureProduct = pictureProduct;
    }

    public String getTimeProduct() {
        return timeProduct;
    }

    public void setTimeProduct(String timeProduct) {
        this.timeProduct = timeProduct;
    }

    public String getUidUpProduct() {
        return uidUpProduct;
    }

    public void setUidUpProduct(String uidUpProduct) {
        this.uidUpProduct = uidUpProduct;
    }

    public int getWishesCount() {
        return wishesCount;
    }

    public void setWishesCount(int wishesCount) {
        this.wishesCount = wishesCount;
    }
}
