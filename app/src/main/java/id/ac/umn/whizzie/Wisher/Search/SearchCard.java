package id.ac.umn.whizzie.Wisher.Search;

public class SearchCard {
    private String profileName;
    private String cardName;
    private long cardCount;
    private long cardPrice;
    private boolean isProduct;
    private String itemKey;


    public SearchCard() {
    }

    public SearchCard(String profileName, String cardName, long cardCount, long cardPrice, boolean isProduct, String itemKey) {
        this.profileName = profileName;
        this.cardName = cardName;
        this.cardCount = cardCount;
        this.cardPrice = cardPrice;
        this.isProduct = isProduct;
        this.itemKey = itemKey;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public long getCardCount() {
        return cardCount;
    }

    public void setCardCount(long cardCount) {
        this.cardCount = cardCount;
    }

    public long getCardPrice() {
        return cardPrice;
    }

    public void setCardPrice(long cardPrice) {
        this.cardPrice = cardPrice;
    }

    public boolean isProduct() {
        return isProduct;
    }

    public void setProduct(boolean product) {
        isProduct = product;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }
}
