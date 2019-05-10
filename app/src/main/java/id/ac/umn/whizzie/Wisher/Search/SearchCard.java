package id.ac.umn.whizzie.Wisher.Search;

public class SearchCard {
    private String profileName;
    private String cardName;
    private String cardDesc;
    private long cardCount;
    private long cardPrice;

    public SearchCard() {
    }

    public SearchCard(String profileName, String cardName, String cardDesc, long cardCount, long cardPrice) {
        this.profileName = profileName;
        this.cardName = cardName;
        this.cardDesc = cardDesc;
        this.cardCount = cardCount;
        this.cardPrice = cardPrice;
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

    public String getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
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
}
