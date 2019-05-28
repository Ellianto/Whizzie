package id.ac.umn.whizzie.main.Details;

public class OfferCard {
    private String cardName;
    private String cardImage;
    private long cardPrice;
    private String productKey;
    private String wishKey;

    public OfferCard() {
    }

    public OfferCard(String cardName, String cardImage, long cardPrice, String productKey, String wishKey) {
        this.cardName = cardName;
        this.cardImage = cardImage;
        this.cardPrice = cardPrice;
        this.productKey = productKey;
        this.wishKey = wishKey;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }

    public long getCardPrice() {
        return cardPrice;
    }

    public void setCardPrice(long cardPrice) {
        this.cardPrice = cardPrice;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getWishKey() {
        return wishKey;
    }

    public void setWishKey(String wishKey) {
        this.wishKey = wishKey;
    }
}
