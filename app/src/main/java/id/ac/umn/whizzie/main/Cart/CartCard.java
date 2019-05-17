package id.ac.umn.whizzie.main.Cart;

public class CartCard {
    String itemName;
    String imagePath;
    String itemKey;
    Long itemQty;
    Long itemPrice;

    public CartCard() {}

    public CartCard(String itemName, String imagePath, String itemKey, Long itemQty, Long itemPrice) {
        this.itemName = itemName;
        this.imagePath = imagePath;
        this.itemKey = itemKey;
        this.itemQty = itemQty;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public Long getItemQty() {
        return itemQty;
    }

    public void setItemQty(Long itemQty) {
        this.itemQty = itemQty;
    }

    public Long getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Long itemPrice) {
        this.itemPrice = itemPrice;
    }
}
