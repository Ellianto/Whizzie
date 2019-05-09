package id.ac.umn.whizzie.Search;

public class SearchCard {
    private String profileName;
    private String cardName;
    private String cardDesc;
    private long cardCount;
    private int cardBudget;

    public SearchCard() {
    }

    public SearchCard(String profileName, String cardName, String cardDesc, long cardCount) {
        this.profileName = profileName;
        this.cardName = cardName;
        this.cardDesc = cardDesc;
        this.cardCount = cardCount;
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
        return this.cardCount;
    }

    public void setCardCount(long cardCount) {
        this.cardCount = cardCount;
    }

    public int getCardBudget() {
        return cardBudget;
    }

    public void setCardBudget(int cardBudget) {
        this.cardBudget = cardBudget;
    }
}
