package id.ac.umn.whizzie.main.Search;

public class SearchCard {
    private String profileName;
    private String profilePic;
    private String cardName;
    private String cardImage;
    private long cardCount;
    private long cardPrice;
    private boolean isProduct;
    private String itemKey;


    public SearchCard() {
    }

    public SearchCard(String profileName, String profilePic, String cardName, String cardImage, long cardCount, long cardPrice, boolean isProduct, String itemKey) {
        this.profileName = profileName;
        this.profilePic = profilePic;
        this.cardName = cardName;
        this.cardImage = cardImage;
        this.cardCount = cardCount;
        this.cardPrice = cardPrice;
        this.isProduct = isProduct;
        this.itemKey = itemKey;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
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
