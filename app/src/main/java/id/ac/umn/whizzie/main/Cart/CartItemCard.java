package id.ac.umn.whizzie.main.Cart;

public class CartItemCard{
    private String itemName;
    private String imageURL;
    private long itemPrice;
    private long itemQty;
    private String itemID;

    public CartItemCard() {
    }

    public CartItemCard(String itemName, String imageURL, long itemPrice, long itemQty, String itemID) {
        this.itemName = itemName;
        this.imageURL = imageURL;
        this.itemPrice = itemPrice;
        this.itemQty = itemQty;
        this.itemID = itemID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(long itemPrice) {
        this.itemPrice = itemPrice;
    }

    public long getItemQty() {
        return itemQty;
    }

    public void setItemQty(long itemQty) {
        this.itemQty = itemQty;
    }


}
