package id.ac.umn.whizzie.Wisher.Wishes;

public class WishesCard {
    private String profileName;
    private String wishesDesc;
    private String wishesName;
    private int wishesBudget;
    private int wishesCount;

    public WishesCard(String profileName, String wishesDesc, String wishesName, int wishesBudget) {
        this.profileName = profileName;
        this.wishesDesc = wishesDesc;
        this.wishesName = wishesName;
        this.wishesBudget = wishesBudget;
    }

    public String getWishesName() {
        return wishesName;
    }

    public void setWishesName(String wishesName) {
        this.wishesName = wishesName;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getWishesDesc() {
        return wishesDesc;
    }

    public void setWishesDesc(String wishesDesc) {
        this.wishesDesc = wishesDesc;
    }

    public int getWishesBudget() {
        return wishesBudget;
    }

    public void setWishesBudget(int wishesBudget) {
        this.wishesBudget = wishesBudget;
    }

    public int getWishesCount() {
        // TODO : Implement query to get counts here
        return wishesCount;
    }

    public void setWishesCount(int wishesCount) {
        this.wishesCount = wishesCount;
    }
}
