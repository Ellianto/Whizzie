package id.ac.umn.whizzie.main.Cart;

import java.util.List;

public class CartStoreCard {
    private String storeName;
    private String storeImage;
    private long storeTotal;
    private List<CartItemCard> cicList;

    public CartStoreCard() {
    }

    public CartStoreCard(String storeName, String storeImage, long storeTotal, List<CartItemCard> cicList) {
        this.storeName = storeName;
        this.storeImage = storeImage;
        this.storeTotal = storeTotal;
        this.cicList = cicList;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public long getStoreTotal() {
        return storeTotal;
    }

    public void setStoreTotal(long storeTotal) {
        this.storeTotal = storeTotal;
    }

    public List<CartItemCard> getCicList() {
        return cicList;
    }

    public void setCicList(List<CartItemCard> cicList) {
        this.cicList = cicList;
    }
}
