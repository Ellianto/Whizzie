package id.ac.umn.whizzie.Search;

public class SearchCard {
    private String profileName;
    private String cardName;
    private String cardDesc;
    private int cardCount;
    private int cardBudget;

    public SearchCard() {
    }

    public SearchCard(String profileName, String cardName, String cardDesc, int cardCount) {
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

    public int getCardCount() {
        // TODO : Implement query to get counts here
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public int getCardBudget() {
        return cardBudget;
    }

    public void setCardBudget(int cardBudget) {
        this.cardBudget = cardBudget;
    }
}
